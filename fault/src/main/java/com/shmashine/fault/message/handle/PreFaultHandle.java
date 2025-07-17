package com.shmashine.fault.message.handle;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.constants.MessageConstants;
import com.shmashine.common.constants.RedisConstants;
import com.shmashine.common.constants.SocketConstants;
import com.shmashine.common.message.MonitorMessage;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.common.utils.SendMessageUtil;
import com.shmashine.common.utils.TimeUtils;
import com.shmashine.fault.elevator.entity.TblElevator;
import com.shmashine.fault.elevator.service.TblElevatorService;
import com.shmashine.fault.fault.entity.TblFault;
import com.shmashine.fault.fault.entity.TblFaultDefinition0902;
import com.shmashine.fault.fault.entity.TblFaultTemp;
import com.shmashine.fault.fault.service.TblFaultDefinitionServiceI;
import com.shmashine.fault.fault.service.TblFaultServiceI;
import com.shmashine.fault.fault.service.TblFaultTempService;
import com.shmashine.fault.kafka.KafkaProducer;
import com.shmashine.fault.kafka.KafkaTopicConstants;
import com.shmashine.fault.mongo.entity.FaultMessageConfirm;
import com.shmashine.fault.mongo.utils.MongoTemplateUtil;
import com.shmashine.fault.redis.util.RedisKeyUtils;
import com.shmashine.fault.redis.util.RedisUtils;
import com.shmashine.fault.utils.MaiXinMaintenancePlatformUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 待处理故障落库（故障临时表）处理
 */
@Slf4j
@Component
public class PreFaultHandle {

    @Autowired
    private TblFaultServiceI faultService;

    @Autowired
    private DefaultFaultHandle faultHandle;

    @Autowired
    private TblFaultTempService faultTempService;

    @Autowired
    private TblFaultDefinitionServiceI faultDefinitionService;

    @Autowired
    private TblElevatorService elevatorService;

    @Autowired
    private RedissonClient redissonClient;

    @Resource(type = RedisUtils.class)
    private RedisUtils redisUtils;

    @Resource
    private MongoTemplateUtil mongoTemplateUtil;

    @Resource
    private KafkaProducer kafkaProducer;

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(2, 4,
            8L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "PreFaultHandle");

    //CHECKSTYLE:OFF

    /**
     * 新增故障
     */
    public void addFault(JSONObject messageJson) {
        log.info("收到待确认故障-新增，message：{}", messageJson);
        String elevatorCode = messageJson.getString("elevatorCode");
        String faultType = messageJson.getString("fault_type");
        String faultSecondType = messageJson.getString("fault_stype");
        String sensorType = messageJson.getString(MessageConstants.SENSOR_TYPE);
        String faultId = messageJson.getString("faultId");

        try {
            // 处理正式表： 故障表中是否存在故障中的故障（临时表确认后生成）
            TblFault fault;
            if (sensorType.equals(SocketConstants.SENSOR_TYPE_FRONT)) {
                fault = faultService.getInFaultByFaultType(elevatorCode, faultType, faultSecondType);
            } else {
                fault = faultService.getInFaultByFaultType(elevatorCode, faultType);
            }
            if (fault != null) {
                // 正式故障表中存在故障(故障已记录且确认过)
                faultHandle.addFault(messageJson);
            }

            // 处理临时表：  临时故障表中是否 存在未确认的故障
            TblFaultTemp faultTemp = faultTempService.getInFaultByFaultType(elevatorCode, faultType, faultSecondType);
            if (faultTemp == null) {

                //故障报文转化
                faultTemp = convertPreFaultByMessage(messageJson, elevatorCode, faultType, faultSecondType);

                String registerNumber = faultTemp.getRegisterNumber();
                String address = faultTemp.getVAddress();
                String floor = String.valueOf(faultTemp.getFloor());
                Date reportTime = faultTemp.getDtReportTime();

                //电动车乘梯
                if ("37".equals(faultType)) {

                    /*
                    电信翼智 不走二次识别直接推送 FAULT
                    需求：【【运维】--【电梯】后端对推送平台为“电信”的电梯的电动车二次识别做特殊配置】
                                https://www.tapd.cn/42601120/prong/stories/view/1142601120001000498
                     */

                    if (StringUtils.hasText(faultTemp.getHttpPtCodes())
                            && faultTemp.getHttpPtCodes().contains("shanghaiYizhi")) {

                        // 新增故障
                        faultHandle.addFault(messageJson);
                        // 推送到sender模块
                        kafkaProducer.sendAndFlush(KafkaTopicConstants.CUBE_FAULT_TOPIC, messageJson.toJSONString());
                    } else {
                        faultTempService.insertUncivilizedBehavior37(faultTemp);
                    }
                    //抓图
                    faultHandle.getImage(elevatorCode, faultType, faultTemp.getVFaultId());
                    return;

                } else {
                    faultTempService.insert(faultTemp);
                }

                log.info("待确认故障-推送取证，message：{}", messageJson);
                if ("7".equals(faultType) || "8".equals(faultType)) {

                    //平层困人图像识别成功后才短信通知
                    //                sendFaultMessage(faultTemp, 0);
                    redisUtils.addZSet(RedisConstants.HLS_IMAGE, faultTemp.getVFaultId(),
                            (double) System.currentTimeMillis() / 1000);

                    //防止重复下载
                    RLock lock = redissonClient.getLock(RedisConstants.AFRESDOWNLOADIMAGE_LOCK
                            + faultTemp.getVFaultId());
                    //尝试加锁，最多等待300ms，上锁以后30秒自动解锁
                    try {
                        if (lock.tryLock(300, 30000, TimeUnit.MILLISECONDS)) {
                            faultHandle.getImage(elevatorCode, faultType, faultTemp.getVFaultId());
                        }
                    } catch (InterruptedException e) {
                        log.error(ExceptionUtil.stacktraceToString(e));
                    }
                    return;
                }
                // 取证
                faultHandle.saveFaultHistoryFlie(elevatorCode, faultType, faultTemp.getVFaultId(),
                        messageJson.getDate("time"), null);
            } else {
                // 故障次数 + 1
                faultTemp.setIFaultNum(faultTemp.getIFaultNum() + 1);
                faultTempService.update(faultTemp);
            }
        } finally {
            //移除mongo故障记录
            mongoTemplateUtil.removeById(faultId, FaultMessageConfirm.class);
        }

    }       //CHECKSTYLE:ON


    /**
     * 消除故障
     */
    public void disappearFault(JSONObject messageJson) {

        String faultType = messageJson.getString("fault_type");

        String faultId = messageJson.getString("faultId");
        String time = messageJson.getString("time");

        // 消除正式表中的故障信息
        faultHandle.disappearFault(messageJson);

        //电瓶车故障不处理
        if ("37".equals(faultType)) {
            mongoTemplateUtil.removeById(faultId, FaultMessageConfirm.class);
            return;
        }

        if ("7".equals(faultType) || "8".equals(faultType)) {
            //推送维保平台
            executorService.submit(() -> {
                MaiXinMaintenancePlatformUtil.pushEmergencyRescueCancel(faultId);
            });
        }

        String elevatorCode = messageJson.getString("elevatorCode");
        String faultSecondType = messageJson.getString("fault_stype");
        // 更新临时表中故障信息
        TblFaultTemp faultTemp = faultTempService.getInFaultByFaultType(elevatorCode, faultType, faultSecondType);
        if (faultTemp != null) {
            // 消除故障
            disappearPreFaultByMessage(messageJson, faultTemp);
            return;
        }

        //是否有add任务未处理
        List<FaultMessageConfirm> fault = mongoTemplateUtil.queryByElevatorAndFault(elevatorCode, faultType,
                "add", FaultMessageConfirm.class);

        if (fault == null || fault.isEmpty()) {
            mongoTemplateUtil.removeById(faultId, FaultMessageConfirm.class);
        }
    }


    /**
     * 根据故障报文，新增临时表故障记录
     *
     * @param messageJson  故障报文
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     */
    private TblFaultTemp convertPreFaultByMessage(JSONObject messageJson, String elevatorCode,
                                                  String faultType, String faultSecondType) {

        String sensorType = messageJson.getString(MessageConstants.SENSOR_TYPE);

        TblFaultDefinition0902 faultDefinition;
        if (sensorType.equals(SocketConstants.SENSOR_TYPE_FRONT)) {
            faultDefinition = faultDefinitionService.getByFaultTypeAndSecondType(faultType, faultSecondType);
        } else {
            faultDefinition = faultDefinitionService.getByFaultType(faultType);
        }

        // base64报文数据解析
        MonitorMessage monitorMessage = new MonitorMessage();
        monitorMessage.setFromBase64(messageJson);

        TblElevator elevator = elevatorService.getByElevatorCode(elevatorCode);
        TblFaultTemp tblFault = new TblFaultTemp();
        tblFault.setVFaultId(messageJson.getString("faultId"));
        tblFault.setVElevatorId(elevator.getVElevatorId());
        tblFault.setVElevatorCode(elevatorCode);
        tblFault.setRegisterNumber(elevator.getVEquipmentCode());
        tblFault.setDtReportTime(messageJson.getDate("time"));
        //楼层信息
        tblFault.setFloor(Integer.parseInt(monitorMessage.getFloor()));
        tblFault.setVAddress(elevator.getVAddress());
        tblFault.setIFaultType(faultType);
        tblFault.setIFaultNum(1);
        tblFault.setVFaultName(messageJson.getString("faultName"));
        tblFault.setILevel(faultDefinition.getLevel());
        tblFault.setILevelName(faultDefinition.getLevelName());
        tblFault.setIStatus(0);
        // 不文明行为标识
        tblFault.setIUncivilizedBehaviorFlag(faultDefinition.getUncivilizedBehaviorFlag());
        tblFault.setIFaultMessage(messageJson.toJSONString());
        //推送平台
        tblFault.setHttpPtCodes(elevator.getVHttpPtCodes());

        tblFault.setDtCreateTime(new Date());
        tblFault.setIConfirmStatus(0);
        // 服务模式
        try {
            String statusKey = RedisKeyUtils.getElevatorStatus(elevatorCode);
            String status = redisUtils.hmGet(statusKey, "mode_status");
            if (!StringUtils.hasText(status)) {
                status = "0";
                redisUtils.hmSet(statusKey, "mode_status", status);
            }
            tblFault.setIModeStatus(Integer.valueOf(status));
        } catch (Exception e) {
            //
        }
        return tblFault;
    }

    public void disappearPreFaultByMessage(JSONObject messageJson, TblFaultTemp fault) {
        // 计算故障持续时间
        //long duration = time.getTime() - startTime;
        // 改变故障状态为已恢复
        fault.setIStatus(1);
        fault.setIConfirmStatus(3);
        if ("37".equals(fault.getIFaultType())) {
            //需要二次识别确认或手动确认
            fault.setIConfirmStatus(0);
        }
        var time = messageJson.getDate("time");
        fault.setDtEndTime(time);
        fault.setDtModifyTime(time);
        faultTempService.update(fault);

        //移除mongo故障记录
        mongoTemplateUtil.removeById(messageJson.getString("faultId"), FaultMessageConfirm.class);
    }

    //CHECKSTYLE:OFF
    private void sendFaultMessage(final TblFaultTemp faultTemp, String faultType, TblElevator elevator) {
        executorService.submit(() -> {
            String faultId = faultTemp.getVFaultId();
            TblFaultTemp tblFaultTemp = faultTempService.queryById(faultId);
            if (tblFaultTemp != null && tblFaultTemp.getIStatus() == 0) {
                List<String> tels = faultTempService.getSeatsTel();
                Date reportTime = faultTemp.getDtReportTime();
                String time = TimeUtils.dateFormat(reportTime);
                if (null != tels && !tels.isEmpty()) {
                    for (String tel : tels) {
                        if (StringUtils.hasText(tel)) {

                            // base64报文数据解析‘
                            MonitorMessage monitorMessage = new MonitorMessage();
                            try {
                                String faultMessage = faultTemp.getIFaultMessage();
                                JSONObject messageJson = JSONObject.parseObject(faultMessage);
                                monitorMessage.setFromBase64(messageJson);
                            } catch (Exception e) {
                                log.info("故障：{}，获取运行信息失败，error:{}", faultTemp.getVFaultId(),
                                        ExceptionUtil.stacktraceToString(e));
                            }

                            if ("7".equals(faultType) || "8".equals(faultType)) {

                                String type = "7".equals(faultType) ? "平层" : "非平层";
                                String occurrenceTime = DateUtil.format(DateUtil.parse(time), "yyyy年MM月dd日 HH:mm");
                                SendMessageUtil.sendEntrapMessage(tel, type, elevator.getVillageName(),
                                        elevator.getVElevatorName(), occurrenceTime, monitorMessage.getFloor());
                            } else {
                                SendMessageUtil.sendFaultMessageWithFloor(tel, faultTemp.getVElevatorCode(),
                                        faultTemp.getVAddress(), faultTemp.getVFaultName(),
                                        monitorMessage.getFloor(), time);
                            }
                        }
                    }
                }
            }
        });

    }       //CHECKSTYLE:ON
}
