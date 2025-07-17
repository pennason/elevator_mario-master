package com.shmashine.api.service.fault.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.date.DateUtil;

import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.api.controller.maiXinMaintenancePlatform.vo.FaultMessageReqVO;
import com.shmashine.api.controller.maiXinMaintenancePlatform.vo.GovernFaultTypeEnum;
import com.shmashine.api.controller.maiXinMaintenancePlatform.vo.RealMessageData;
import com.shmashine.api.dao.BizElevatorDao;
import com.shmashine.api.dao.BizFaultTempDao;
import com.shmashine.api.dao.PeopleFlowStatisticsDao;
import com.shmashine.api.dao.TblSysFileDao;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.kafka.KafkaProducer;
import com.shmashine.api.kafka.KafkaTopicConstants;
import com.shmashine.api.module.fault.input.SearchFaultTempModule;
import com.shmashine.api.service.camera.TblCameraImageIdentifyServiceI;
import com.shmashine.api.service.fault.BizFaultTempService;
import com.shmashine.api.service.village.TblVillageServiceI;
import com.shmashine.api.util.MaiXinMaintenancePlatformUtil;
import com.shmashine.common.constants.RedisConstants;
import com.shmashine.common.constants.SystemConstants;
import com.shmashine.common.entity.TblCameraImageIdentifyEntity;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblFaultTemp;
import com.shmashine.common.message.MonitorMessage;
import com.shmashine.common.message.PeopleFlowStatisticsMessage;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.common.utils.SendMessageUtil;
import com.shmashine.common.utils.TimeUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 故障记录临时表(TblFaultTemp)表服务实现类
 *
 * @author makejava
 * @since 2020-06-29 18:40:09
 */
@Slf4j
@Service("bizFaultTempService")
public class BizFaultTempServiceImpl implements BizFaultTempService {

    private static Logger faultTempLogger = LoggerFactory.getLogger("faultTempLogger");

    @Resource
    private BizFaultTempDao bizFaultTempDao;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private TblSysFileDao fileDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TblVillageServiceI tblVillageServiceI;

    @Resource
    private BizElevatorDao bizElevatorDao;

    @Resource
    private TblCameraImageIdentifyServiceI imageIdentifyService;

    @Resource
    private PeopleFlowStatisticsDao peopleFlowStatisticsDao;

    @Resource
    private MaiXinMaintenancePlatformUtil maiXinMaintenancePlatformUtil;

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(2, 4,
            8L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "BizFaultTempService");

    @Override
    public PageListResultEntity searchFaultSList(SearchFaultTempModule searchFaultTempModule) {
        Integer pageIndex = searchFaultTempModule.getPageIndex();
        Integer pageSize = searchFaultTempModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        var mapPageInfo = new PageInfo<>(bizFaultTempDao.searchFaultList(searchFaultTempModule), pageSize);

        // 扩展小区信息
        tblVillageServiceI.extendVillageInfo(mapPageInfo.getList());

        // 封装分页数据结构
        var mapPageListResultEntity = new PageListResultEntity<>(pageIndex, pageSize, (int) mapPageInfo.getTotal(),
                mapPageInfo.getList());
        return mapPageListResultEntity;
    }

    /**
     * 通过ID查询单条数据
     *
     * @param vFaultId 主键
     * @return 实例对象
     */
    @Override
    public TblFaultTemp queryById(String vFaultId) {
        return bizFaultTempDao.queryById(vFaultId);
    }

    @Override
    public Map<String, Object> getFaultTempDetail(String vFaultId) {
        Map<String, Object> result = new HashMap<>();
        result.put("detail", bizFaultTempDao.getFaultDetail(vFaultId));
        result.put("historyVideo", fileDao.getVideoUrl(vFaultId, 2, 1));
        result.put("historyImage", fileDao.getByBusinessIdAndBusinessTypeAndFileType(vFaultId, 2, 0));
        return result;
    }

    @Override
    public void confirmFault(TblFaultTemp tblFaultTemp) {
        if (null == tblFaultTemp || StringUtils.isBlank(tblFaultTemp.getVFaultId())) {
            return;
        }
        // 确认标识 0-未确认，1-已确认故障， 2-已确认非故障
        tblFaultTemp.setIConfirmStatus(1);
        tblFaultTemp.setDtModifyTime(new Date());
        bizFaultTempDao.update(tblFaultTemp);
        tblFaultTemp = bizFaultTempDao.queryById(tblFaultTemp.getVFaultId());

        String faultType = String.valueOf(tblFaultTemp.getIFaultType());
        // 推送到FAULT_TOPIC(故障模块)
        kafkaProducer.sendMessageToKafkaWithFlush(KafkaTopicConstants.FAULT_TOPIC, tblFaultTemp.getVElevatorCode(),
                tblFaultTemp.getIFaultMessage());

        // 推送到sender模块
        pushToSend(faultType, tblFaultTemp);

        // 兼容推送到httpclient
        kafkaProducer.sendMessageToKafkaWithFlush("pro_netty_nezha", tblFaultTemp.getVElevatorCode(),
                tblFaultTemp.getIFaultMessage());
    }

    @Override
    public void cancelFault(TblFaultTemp tblFaultTemp) {
        // 状态（0:故障中、1:已恢复）
        tblFaultTemp.setIStatus(1);
        // 确认标识 0-未确认，1-已确认故障， 2-已确认非故障
        tblFaultTemp.setIConfirmStatus(2);
        // 手动恢复故障 -2 无任何操作，-1 手动恢复故障，需通知设备，0 失败，1成功，2故障不存在（后面三个都是通知终端后返回结果 ）
        tblFaultTemp.setIManualClear(-2);
        tblFaultTemp.setDtModifyTime(new Date());
        bizFaultTempDao.update(tblFaultTemp);
        // TODO 通知设备取消故障
        // KafkaProducer.sendMessageToKafka();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void faultConfirm(String faultId, int result) {

        //获取统计记录
        PeopleFlowStatisticsMessage peopleFlowStatistics = peopleFlowStatisticsDao.getById(faultId);

        //人数统计
        if (peopleFlowStatistics != null) {

            TblCameraImageIdentifyEntity imageIdentifyEntity = imageIdentifyService.getByCustomId(faultId);
            imageIdentifyEntity.setStatus(3).setIdentifyStatus(1).setIdentifyResult(String.valueOf(result));
            //更新识别记录
            imageIdentifyService.update(imageIdentifyEntity);

            //更新统计表
            peopleFlowStatistics.setIdentificationNumber(result);
            peopleFlowStatisticsDao.updateById(peopleFlowStatistics);

            return;
        }

        Long remove = redisTemplate.opsForZSet().remove(RedisConstants.HLS_IDENTIFICATION, faultId);

        if (result > 0) {

            //困人处理
            if (remove > 0) {
                TblFaultTemp faultTemp = bizFaultTempDao.queryById(faultId);
                //待确认状态发送短信
                if (faultTemp.getIConfirmStatus() == 0) {
                    //发送短信
                    sendFaultMessage(faultTemp, 0);
                }
                faultTempLogger.info("困人识别成功，故障id：" + faultId + "故障状态：" + faultTemp.getIConfirmStatus());

                //确认故障审核状态
                faultTemp.setRecognitionResult(1);
                bizFaultTempDao.update(faultTemp);

                //推送维保平台
                executorService.submit(() -> {
                    log.info("困人推送维保平台，message:{}", faultTemp);
                    RealMessageData data = RealMessageData.builder()
                            .serviceMode(1).collectionTime(faultTemp.getDtReportTime()).build();

                    TblElevator elevator = bizElevatorDao.getElevatorIdByCode(faultTemp.getVElevatorCode());
                    String registerNumber = elevator.getVEquipmentCode();

                    GovernFaultTypeEnum entrap = GovernFaultTypeEnum.ENTRAP;
                    if (faultTemp.getIFaultType() == 8) {
                        entrap = GovernFaultTypeEnum.ENTRAP2;
                    }

                    FaultMessageReqVO faultMessageReqVO = FaultMessageReqVO.builder()
                            .platformCode("MX201").elevatorCode(faultTemp.getVElevatorCode())
                            .registerNumber(registerNumber)
                            .equCode(registerNumber).alarmId(faultId).faultStatus(0)
                            .alarmChannel("S04").faultTypeEnum(entrap)
                            .faultDescription(entrap.getFaultName()).address(faultTemp.getVAddress())
                            .occurTime(faultTemp.getDtReportTime()).currentTime(new Date())
                            .data(data).build();

                    maiXinMaintenancePlatformUtil.pushEmergencyRescue(faultMessageReqVO);
                });

            }
        }
    }

    /**
     * 查询待确认的电动车乘梯
     */
    @Override
    public TblFaultTemp queryElectricMobileById(String faultId) {
        return bizFaultTempDao.queryElectricMobileById(faultId);
    }

    @Override
    public void confirmElectricMobileFault(TblFaultTemp tblFaultTemp) {

        if (null == tblFaultTemp) {
            return;
        }
        // 确认标识 0-未确认，1-已确认故障， 2-已确认非故障
        tblFaultTemp.setIConfirmStatus(1);
        tblFaultTemp.setDtModifyTime(new Date());
        bizFaultTempDao.updateElectricMobile(tblFaultTemp);

        // 推送到FAULT_TOPIC
        kafkaProducer.sendMessageToKafkaWithFlush(KafkaTopicConstants.FAULT_TOPIC, tblFaultTemp.getVElevatorCode(),
                tblFaultTemp.getIFaultMessage());
        // 推送到sender模块
        kafkaProducer.sendMessageToKafkaWithFlush("cube_fault", tblFaultTemp.getVElevatorCode(),
                tblFaultTemp.getIFaultMessage());

    }

    @Override
    public void cancelElectricMobileFault(TblFaultTemp tblFaultTemp) {
        // 状态（0:故障中、1:已恢复）
        tblFaultTemp.setIStatus(1);
        // 确认标识 0-未确认，1-已确认故障， 2-已确认非故障
        tblFaultTemp.setIConfirmStatus(2);
        // 手动恢复故障 -2 无任何操作，-1 手动恢复故障，需通知设备，0 失败，1成功，2故障不存在（后面三个都是通知终端后返回结果 ）
        tblFaultTemp.setIManualClear(-2);
        tblFaultTemp.setDtModifyTime(new Date());
        bizFaultTempDao.updateElectricMobile(tblFaultTemp);
    }

    @Override
    public void recoverPeopleTrappedFault() {
        bizFaultTempDao.recoverPeopleTrappedFault(DateUtil.today(), DateUtil.formatDate(DateUtil.yesterday()));
    }

    @Override
    public Boolean sendEntrap2Message(String faultId) {
        var faultTemp = bizFaultTempDao.queryById(faultId);
        //待确认状态发送短信
        if (faultTemp.getIConfirmStatus() == 0) {
            //发送短信
            var res = sendFaultMessage(faultTemp, 0);
            try {
                res.get();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    private Future<?> sendFaultMessage(TblFaultTemp faultTemp, int delay) {
        return executorService.submit(() -> {
            List<String> tels = bizFaultTempDao.getSeatsTel();
            Date reportTime = faultTemp.getDtReportTime();
            String time = TimeUtils.dateFormat(reportTime);
            if (null != tels && !tels.isEmpty()) {
                var elevator = bizElevatorDao.getElevatorByElevatorIdOrCode(faultTemp.getvElevatorId());
                // 未安装、停用不推送短信
                if (elevator.getIInstallStatus() == 1 || elevator.getIInstallStatus() == 3) {
                    for (String tel : tels) {
                        if (StringUtils.isNotBlank(tel)) {
                            String faultMessage = faultTemp.getIFaultMessage();
                            // base64报文数据解析
                            MonitorMessage monitorMessage = new MonitorMessage();
                            try {
                                JSONObject messageJson = JSONObject.parseObject(faultMessage);
                                monitorMessage.setFromBase64(messageJson);
                            } catch (Exception e) {
                                log.info("故障：{}，获取运行信息失败，error:{}", faultTemp.getVFaultId(),
                                        ExceptionUtils.getStackTrace(e));
                            }
                            String type = 7 == faultTemp.getIFaultType() ? "平层" : "非平层";
                            String occurrenceTime = DateUtil.format(DateUtil.parse(time), "yyyy年MM月dd日 HH:mm");
                            SendMessageUtil.sendEntrapMessage(tel, type, elevator.getVVillageName(),
                                    elevator.getVElevatorName(), occurrenceTime, monitorMessage.getFloor());
                        }
                    }

                }
            }
        });

    }

    //推送第三方平台
    private void pushToSend(String faultType, TblFaultTemp tblFaultTemp) {

        executorService.submit(() -> {

            Integer i = bizFaultTempDao.checkToSend(tblFaultTemp.getVElevatorCode(), faultType);

            //没有屏蔽
            if (i == null) {

                if ("7".equals(faultType) || "8".equals(faultType)) {
                    kafkaProducer.sendMessageToKafkaWithFlush("cube_trapped", tblFaultTemp.getVElevatorCode(),
                            tblFaultTemp.getIFaultMessage());
                } else {
                    // 推送故障数据
                    kafkaProducer.sendMessageToKafkaWithFlush("cube_fault", tblFaultTemp.getVElevatorCode(),
                            tblFaultTemp.getIFaultMessage());
                }
            }

        });
    }

}