package com.shmashine.fault.task;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.constants.RedisConstants;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.fault.elevator.entity.TblElevator;
import com.shmashine.fault.elevator.service.TblElevatorService;
import com.shmashine.fault.fault.entity.TblFaultDefinition0902;
import com.shmashine.fault.fault.service.TblFaultDefinitionServiceI;
import com.shmashine.fault.message.handle.FaultHandle;
import com.shmashine.fault.message.handle.PreFaultHandle;
import com.shmashine.fault.mongo.entity.FaultMessageConfirm;
import com.shmashine.fault.mongo.utils.MongoTemplateUtil;
import com.shmashine.fault.redis.util.RedisUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 设备故障消息确认
 *
 * @author jiangheng
 * @version V1.0.0 - 2023/3/20 15:55
 */
@Slf4j
@Profile({"prod"})
@Component
public class FaultMessageConfirmTask {

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(2,
            4, 2, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "FaultMessageConfirmTask");

    private static Logger faultMessageLogger = LoggerFactory.getLogger("faultMessageLogger");

    @Resource
    private MongoTemplateUtil mongoTemplateUtil;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private TblElevatorService tblElevatorService;

    @Resource
    private TblFaultDefinitionServiceI faultDefinitionService;

    @Resource
    private FaultHandle faultHandle;

    @Resource
    private PreFaultHandle preFaultHandle;

    //CHECKSTYLE:OFF

    /**
     * 设备故障消息确认（每分钟一次）
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void checkFaultStatus() {

        log.info("设备故障消息确认任务开始");

        List<FaultMessageConfirm> faults = mongoTemplateUtil.findAllByASC(FaultMessageConfirm.class, "time");

        faults.forEach(fault -> executorService.submit(() -> {

            String faultType = fault.getFaultType();
            String stype = fault.getST();
            String elevatorCode = fault.getElevatorCode();

            RLock lock = redissonClient.getFairLock(RedisConstants.ELEVATOR_FAULT_MESSAGE_MARK_LOCK
                    + elevatorCode + faultType);

            try {

                //尝试加锁，上锁以后10分钟自动解锁
                if (lock.tryLock(1, 600, TimeUnit.SECONDS)) {

                    if (mongoTemplateUtil.findById(fault.getId(), FaultMessageConfirm.class) == null) {
                        faultMessageLogger.info("故障已清除，无需再生成故障", elevatorCode, faultType);
                        return;
                    }

                    try {

                        //获取redis中当前故障状态
                        String presentFaultType = redisUtils.hmGet(RedisConstants.ELEVATOR_FAULT_TYPE_MARK
                                + elevatorCode, faultType);

                        if (stype.equals(presentFaultType)) {

                            TblElevator tblElevator = tblElevatorService.getByElevatorCode(elevatorCode);
                            TblFaultDefinition0902 faultDefinition = faultDefinitionService.getByFaultType(faultType);

                            if (tblElevator == null || faultDefinition == null) {
                                mongoTemplateUtil.removeById(fault.getId(), FaultMessageConfirm.class);
                                return;
                            }

                            JSONObject messageJson = new JSONObject();
                            messageJson.put("elevatorCode", elevatorCode);
                            messageJson.put("fault_type", faultType);
                            messageJson.put("faultName", faultDefinition.getFaultName());
                            messageJson.put("uncivilizedBehaviorFlag", faultDefinition.getUncivilizedBehaviorFlag());
                            messageJson.put("fault_stype", fault.getFault_stype());
                            messageJson.put("sensorType", fault.getSensorType());
                            messageJson.put("faultId", fault.getId());
                            messageJson.put("time", fault.getTime());
                            messageJson.put("D", fault.getD());

                            int elevatorFilterFlag = tblElevator.getIFilterFlag();
                            int filterFlag = faultDefinition.getFilterFlag();

                            //待确认故障
                            if (elevatorFilterFlag == 1 || filterFlag == 1) {

                                //新增
                                if ("add".equals(stype)) {
                                    preFaultHandle.addFault(messageJson);
                                    faultMessageLogger.info("设备故障消息确认任务——待确认故障——新增，message{}", messageJson);
                                    return;
                                }

                                //消除
                                if ("disappear".equals(stype)) {

                                    preFaultHandle.disappearFault(messageJson);
                                    faultMessageLogger.info("设备故障消息确认任务——待确认故障——消除，message{}", messageJson);
                                }

                            } else {

                                //新增
                                if ("add".equals(stype)) {
                                    faultHandle.addFault(messageJson);
                                    faultMessageLogger.info("设备故障消息确认任务——故障——新增，message{}", messageJson);
                                    return;
                                }

                                //消除
                                if ("disappear".equals(stype)) {
                                    faultHandle.disappearFault(messageJson);
                                    faultMessageLogger.info("设备故障消息确认任务——故障——消除，message{}", messageJson);
                                }

                            }

                        } else {
                            mongoTemplateUtil.removeById(fault.getId(), FaultMessageConfirm.class);
                        }


                    } finally {
                        lock.unlock();
                    }

                }

            } catch (InterruptedException e) {
                //
            }

        }));


    }       //CHECKSTYLE:ON

}
