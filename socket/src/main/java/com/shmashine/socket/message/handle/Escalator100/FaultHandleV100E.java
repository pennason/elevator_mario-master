package com.shmashine.socket.message.handle.Escalator100;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.shmashine.common.constants.MessageConstants;
import com.shmashine.common.constants.SocketConstants;
import com.shmashine.common.utils.SnowFlakeUtils;
import com.shmashine.common.utils.TimeUtils;
import com.shmashine.socket.camera.service.TblCameraServiceI;
import com.shmashine.socket.elevator.entity.TblElevator;
import com.shmashine.socket.elevator.service.TblElevatorService;
import com.shmashine.socket.fault.service.TblFaultDefinitionServiceI;
import com.shmashine.socket.fault.service.TblFaultService;
import com.shmashine.socket.feign.RemoteCameraServer;
import com.shmashine.socket.file.service.FileService;
import com.shmashine.socket.kafka.KafkaProducer;
import com.shmashine.socket.kafka.KafkaTopicConstants;
import com.shmashine.socket.message.MessageHandle;
import com.shmashine.socket.message.bean.FaultShieldCache;
import com.shmashine.socket.message.bean.MessageData;
import com.shmashine.socket.message.bean.SensorFaultShieldCache;
import com.shmashine.socket.message.handle.FaultHandle;
import com.shmashine.socket.mongo.entity.FaultMessageConfirm;
import com.shmashine.socket.mongo.utils.MongoTemplateUtil;
import com.shmashine.socket.netty.ChannelManager;
import com.shmashine.socket.redis.RedisService;
import com.shmashine.socket.websocket.WebSocketManager;

import lombok.extern.slf4j.Slf4j;

/**
 * 上海浮奈(电信扶梯)故障消息处理
 *
 * @author little.li
 */
@Slf4j
@Component
public class FaultHandleV100E implements FaultHandle {

    private static final String PROTOCAL_VERSION = "1.0.0E";

    private static Logger faultMessageLogger = LoggerFactory.getLogger("faultMessageLogger");

    private final TblFaultService faultService;

    private final TblFaultDefinitionServiceI faultDefinitionService;

    private final RedisService redisService;

    private final KafkaProducer kafkaProducer;

    private final TblElevatorService tblElevatorService;

    private final TblCameraServiceI cameraService;

    private final FileService fileService;

    @Autowired
    public FaultHandleV100E(TblFaultService faultService, TblFaultDefinitionServiceI faultDefinitionService,
                            RedisService redisService, KafkaProducer kafkaProducer,
                            TblCameraServiceI cameraService, FileService fileService,
                            TblElevatorService tblElevatorService) {

        this.faultService = faultService;
        this.faultDefinitionService = faultDefinitionService;
        this.redisService = redisService;
        this.kafkaProducer = kafkaProducer;
        this.cameraService = cameraService;
        this.fileService = fileService;
        this.tblElevatorService = tblElevatorService;
    }

    @Autowired
    RemoteCameraServer remoteCameraServer;

    @Resource
    private MongoTemplateUtil mongoTemplateUtil;

    @PostConstruct
    public void registerHandle() {
        // 注册到监控、故障、困人消息的处理流程
        MessageHandle.register(this);
    }

    @Override
    public String getProtocalVersion() {
        return PROTOCAL_VERSION;
    }

    /**
     * 设备新增/消除故障 处理
     *
     * @param faultType    故障类型 以,分割
     * @param elevatorCode 电梯code
     */
    @Override
    public void process(String faultId, JSONObject messageJson, String elevatorCode, String sensorType,
                        String faultType, String secondType, String stype) {

        TblElevator tblElevator = tblElevatorService.getByElevatorCode(elevatorCode);

        // 验证该电梯-故障类型的故障是否屏蔽(0:不屏蔽，1:北向屏蔽，，2：平台屏蔽)
        int shielded = checkShielded(elevatorCode, faultType, stype, faultId, tblElevator.getIElevatorType());

        // 检修状态不用上报故障 2020-11-23
        int modeStatus = redisService.getElevatorModeStatus(elevatorCode);

        //消除故障不屏蔽
        if (MessageConstants.STYPE_ADD.equals(stype)) {

            int filter = redisService.checkFaultFilter(elevatorCode, faultType);
            shielded = shielded >= filter ? shielded : filter;

            if (shielded == 2) {

                faultMessageLogger.info("{} -- Fault has been shielded ! elevatorCode[{}]  faultType[{}]  faultID[{}]",
                        TimeUtils.nowTime(), elevatorCode, faultType, faultId);

                //移除故障
                mongoTemplateUtil.removeById(faultId, FaultMessageConfirm.class);
                return;
            }

            //手动检修状态
            if (modeStatus >= 1) {

                faultMessageLogger.info("{} -- 检修模式不推送故障( no send fault on ModeStatus=1) ! "
                                + "elevatorCode[{}]  faultType[{}]",
                        TimeUtils.nowTime(), elevatorCode, faultType);

                //移除故障
                mongoTemplateUtil.removeById(faultId, FaultMessageConfirm.class);
                return;
            }

            //电梯停用不上报故障
            if (tblElevator.getIInstallStatus() == 2) {

                faultMessageLogger.info("{} --设备处于停用状态，不推送故障! elevatorCode[{}]  faultType[{}]",
                        TimeUtils.nowTime(), elevatorCode, faultType);

                //移除故障
                mongoTemplateUtil.removeById(faultId, FaultMessageConfirm.class);
            }

        } else {

            if (shielded == 2 || modeStatus >= 1 || tblElevator.getIInstallStatus() == 2) {
                mongoTemplateUtil.removeById(faultId, FaultMessageConfirm.class);
            }

        }

        //故障名称
        String faultName = faultType;
        //不文明行为标识，0：故障，1：不文明行为
        int uncivilizedBehaviorFlag = 0;

        //消息补充添加故障协议
        messageJson.put("protocalVersion", PROTOCAL_VERSION);
        // 添加故障id
        messageJson.put("faultId", faultId);
        // 添加故障名称
        messageJson.put("faultName", faultName);
        messageJson.put("uncivilizedBehaviorFlag", uncivilizedBehaviorFlag);
        messageJson.put("time", TimeUtils.nowTime());
        String faultMessage = messageJson.toJSONString();
        messageJson.put(MessageConstants.FAULT_TYPE, faultType);
        String webFaultMessage = messageJson.toJSONString();

        faultMessageLogger.info("{} -- 推送故障( send fault =====> kafka) ! elevatorCode[{}]  faultMessage[{}]",
                TimeUtils.nowTime(), elevatorCode, faultMessage);

        // 推送到kafka
        kafkaProducer.sendMessageToKafka(KafkaTopicConstants.FAULT_TOPIC, faultMessage);
        // 推送到浏览器
        WebSocketManager.sendMessageToBrowsers(elevatorCode, MessageConstants.TYPE_MONITOR,
                sensorType, webFaultMessage);

        // 更新Redis中电梯故障状态和故障详情
        redisService.updateFaultDetail(elevatorCode, sensorType, faultType, stype);

        //北向屏蔽不推送故障
        if (shielded == 1) {
            faultMessageLogger.info("{} --北向屏蔽不推送故障! elevatorCode[{}]  faultType[{}]", TimeUtils.nowTime(),
                    elevatorCode, faultType);

            return;
        }

        // 推送故障数据
        kafkaProducer.sendAndFlush(KafkaTopicConstants.CUBE_FAULT, faultMessage);


    }


    /**
     * ST：clear 手动清除故障处理
     * 手动清除故障 通知设备后，设备的返回status 0 失败，1成功，2故障不存在
     *
     * @param faultJson    故障报文
     * @param faultType    故障类型
     * @param elevatorCode 电梯编号
     */
    @Override
    public void cleanFaultHandle(JSONObject faultJson, String faultType, String elevatorCode) {
        Integer manualClear = faultJson.getInteger(MessageConstants.STATUS);
        if (manualClear != null && faultType != null) {
            int faultNum = Integer.parseInt(faultType);
            // 更新手动恢复故障状态
            faultService.updateManualClear(elevatorCode, faultNum, manualClear);
        }
    }


    /**
     * 处理设备重连后上传的当前电梯故障状态
     *
     * @param faultType    故障类型 以,分割
     * @param elevatorCode 电梯code
     */
    @Override
    public void updateFaultHandle(String faultType, String faultSecondType, String elevatorCode, String sensorType) {
        try {

            // 获取所有故障中的故障（mysql表中记录）
            List<String> oldFaultTypeList = faultService.getFaultTypeByCode(elevatorCode);
            // 设备当前状态（重连时上报的故障状态）
            List<String> faultTypeList = null;
            if (StringUtils.isNotBlank(faultType)) {
                faultTypeList = Lists.newArrayList(faultType.split(","));
            } else {    //不存在故障，更新电梯故障状态
                redisService.updateFaultStatus(elevatorCode, 0);
                tblElevatorService.updateFaultStatus(elevatorCode, 0);
            }

            final List<String> nowFaultTypeList = faultTypeList;
            oldFaultTypeList.forEach(value -> {
                // 处理原有遗留故障
                if (nowFaultTypeList == null || !nowFaultTypeList.contains(value)) {

                    // 当前状态已经不包含该故障 -> 故障已消除
                    JSONObject message = convertFaultMessage(elevatorCode, value, "",
                            MessageConstants.STYPE_DISAPPEAR, sensorType);

                    process(SnowFlakeUtils.nextStrId(), message, elevatorCode, sensorType, value, "",
                            MessageConstants.STYPE_DISAPPEAR);

                }
            });

            if (nowFaultTypeList != null && nowFaultTypeList.size() > 0) {
                nowFaultTypeList.forEach(value -> {
                    value = value.trim();
                    // 处理当前状态的故障
                    if (!oldFaultTypeList.contains(value)) {
                        // 原有故障中不包含该故障 -> 新增故障
                        JSONObject message = convertFaultMessage(elevatorCode, value, "",
                                MessageConstants.STYPE_ADD, sensorType);

                        process(SnowFlakeUtils.nextStrId(), message, elevatorCode, sensorType, value, "",
                                MessageConstants.STYPE_ADD);

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            faultMessageLogger.info("updateFaultHandle exception: [{}]", e.getMessage());
        }
    }


    /**
     * 收到故障消息后反馈给设备
     *
     * @param messageData messageData
     * @param sensorType  设备类型
     * @param status      0 失败，1 成功
     * @param faultType   故障类型
     */
    @Override
    public void faultResponse(MessageData messageData, String sensorType, int status, String faultType) {
        Map<String, Object> map = new HashMap<>();
        map.put("TY", "Fault");
        map.put("ST", messageData.getST());
        map.put("status", status);
        if (SocketConstants.SENSOR_TYPE_FRONT.equals(sensorType)) {
            map.put(MessageConstants.FAULT_TYPE, faultType);
        } else {
            map.put(MessageConstants.FAULT_TYPE, Integer.parseInt(faultType));
        }
        // 反馈给设备，标识已接收故障
        ChannelManager.sendMessageToChannel(messageData.getElevatorCode(), messageData.getSensorType(),
                JSONObject.toJSONString(map));
    }

    @Override
    public void sensorFaultResponse(MessageData messageData, String sensorType, int status, String faultType) {
        Map<String, Object> map = new HashMap<>();
        map.put("TY", "SensorFault");
        map.put("ST", messageData.getST());
        map.put("status", status);
        map.put(MessageConstants.FAULT_TYPE, faultType);

        // 反馈给设备，标识已接收故障
        ChannelManager.sendMessageToChannel(messageData.getElevatorCode(), messageData.getSensorType(),
                JSONObject.toJSONString(map));

    }

    /**
     * 收到模式切换消息反馈
     *
     * @param messageData messageData
     * @param status      状态
     * @param eventType   事件类型
     */
    @Override
    public void eventResponse(MessageData messageData, int status, String eventType) {
        Map<String, Object> map = new HashMap<>();
        map.put("TY", "Event");
        map.put("ST", messageData.getST());
        map.put("status", status);
        map.put("event_type", Integer.parseInt(eventType));

        // 反馈给设备，标识已接收故障
        ChannelManager.sendMessageToChannel(messageData.getElevatorCode(), messageData.getSensorType(),
                JSONObject.toJSONString(map));

    }


    /**
     * 验证该电梯-故障类型是否在黑名单，黑名单中的故障不予上报
     *
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     * @return 0:不屏蔽，1:北向屏蔽，，2：平台屏蔽
     */
    public int checkShielded(String elevatorCode, String faultType, String stype, String faultId,
                             Integer elevatorType) {

        // 屏蔽故障名单
        if (StringUtils.isNotBlank(faultType)) {
            // 冲顶、蹲底、楼层信息丢失、温感屏蔽故障名单(扶梯不屏蔽)
            List<String> black = Arrays.asList("1", "2", "18", "19", "28", "29", "30", "31", "32");
            if (black.contains(faultType) && elevatorType == 1) {
                return 2;
            }

            //传感器关联故障是否屏蔽
            if (SensorFaultShieldCache.checkFaultShield(elevatorCode, Integer.parseInt(faultType))) {
                faultMessageLogger.info("-------传感器关联故障已屏蔽,不做处理------elevator{}, faultCode:{}",
                        elevatorCode, faultType);

                return 2;
            }

            // 电梯-故障 屏蔽规则名单
            return FaultShieldCache.checkFailure(elevatorCode, faultType);

        }
        return 0;
    }


    /**
     * 将电梯当前故障清除
     *
     * @param elevatorCode 电梯code
     * @param sensorType   设备类型
     */
    @Override
    public void cleanAllFault(String elevatorCode, String sensorType) {
        try {

            // 获取所有故障中的故障
            Set<String> oldFaultTypeList = redisService.getFaultStatusByCode(sensorType, elevatorCode);

            oldFaultTypeList.forEach(value -> {
                String faultType = "";
                String faultSecondType = "";
                // 前装设备放入Redis中的故障类型特殊处理
                if (SocketConstants.SENSOR_TYPE_FRONT.contains(sensorType)) {
                    String[] split = value.split("-@-");
                    faultType = split[0];
                    faultSecondType = split[1];
                } else {
                    faultType = value;
                }
                // 处理原有遗留故障
                JSONObject message = convertFaultMessage(elevatorCode, faultType, faultSecondType,
                        MessageConstants.STYPE_DISAPPEAR, sensorType);

                process(SnowFlakeUtils.nextStrId(), message, elevatorCode, sensorType, faultType, faultSecondType,
                        MessageConstants.STYPE_DISAPPEAR);

                redisService.updateFaultDetail(elevatorCode, sensorType, value, MessageConstants.STYPE_DISAPPEAR);
                faultMessageLogger.info("{} -- 未收到Fault的disappear消息，但Monitor中状态为正常，消除故障!"
                                + " elevatorCode[{}]  faultType[{}]",
                        TimeUtils.nowTime(), elevatorCode, faultType);

            });

        } catch (Exception e) {
            e.printStackTrace();
            log.info("cleanAllFault exception: [{}]", e.getMessage());
        }
    }

    /////////////////////////////private method/////////////////////////////


    /**
     * 构建故障消息
     *
     * @param elevatorCode    电梯编号
     * @param faultType       故障类型
     * @param faultSecondType 故障子类型
     * @param stype           消息子类型
     * @param sensorType      设备类型
     */
    private JSONObject convertFaultMessage(String elevatorCode, String faultType, String faultSecondType,
                                           String stype, String sensorType) {

        JSONObject messageJson = new JSONObject();
        messageJson.put(MessageConstants.MESSAGE_TYPE, MessageConstants.TYPE_FAULT);

        //扶梯故障类型转换
        if ("Escalator".equals(sensorType)) {
            messageJson.put(MessageConstants.MESSAGE_TYPE, MessageConstants.TYPE_FAULT_ESCALATOR);
        }
        messageJson.put(MessageConstants.MESSAGE_STYPE, stype);
        messageJson.put(MessageConstants.FAULT_TYPE, faultType);
        messageJson.put(MessageConstants.FAULT_STYPE, faultSecondType);
        messageJson.put(MessageConstants.ELEVATOR_CODE, elevatorCode);
        messageJson.put("time", TimeUtils.nowTime());
        messageJson.put(MessageConstants.SENSOR_TYPE, sensorType);
        return messageJson;
    }

}
