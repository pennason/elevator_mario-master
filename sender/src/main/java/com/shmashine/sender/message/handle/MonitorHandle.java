package com.shmashine.sender.message.handle;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.message.MessageData;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.sender.message.send.MonitorSend;
import com.shmashine.sender.server.elevator.BizElevatorService;

import lombok.extern.slf4j.Slf4j;


/**
 * 告警消息消息处理类
 *
 * @author yanjie.wu
 */
@Slf4j
@Component
public class MonitorHandle {

    /**
     * 第三方平台code ： 第三方平台推送处理类
     */
    private static HashMap<String, MonitorSend> senders = new HashMap<>();

    private final ExecutorService sendToUserHandleExecutorService = new ShmashineThreadPoolExecutor(4, 16,
            10L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("sendToUserHandle"), "MonitorHandle");

    private final ExecutorService dispatchExecutorService = new ShmashineThreadPoolExecutor(32, 64,
            10L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(4096), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("dispatch"), "MonitorHandle");

    @Autowired
    private SendToUserHandle sendToUserHandle;

    @Autowired
    private BizElevatorService bizElevatorService;

    /**
     * 需要监听故障消息的 推送到第三方平台的处理类
     */
    public static void register(MonitorSend sender) {
        if (null != sender && StringUtils.isNotBlank(sender.getPtCode())) {
            senders.put(sender.getPtCode(), sender);
        }
    }

    /**
     * 将kafka的故障消息转发到具体的第三方推送平台
     */
    public void handle(String message) {

        if (StringUtils.isBlank(message)) {
            return;
        }

        try {

            log.info("get message : " + message);

            // step1 : check message
            MessageData messageData = JSON.parseObject(message, MessageData.class);
            if (null == messageData || StringUtils.isBlank(messageData.getElevatorCode())
                    || null == messageData.getMonitorMessage()) {
                // 无效报文落日志
                log.info("invalid message : " + message);
                return;
            }

            // 推送到对接用户
            sendToUserHandleExecutorService.submit(() ->
                    sendToUserHandle.monitorMessage(messageData.getElevatorCode(), JSON.toJSONString(messageData)));


            /*String timeStr = messageData.getTime();
            long time = DateUtils.formatDate(timeStr).getTime();
            if (System.currentTimeMillis() - time > 120 * 1000) {
                log.info("time is more than 120 seconds : " + message);
                return;
            }*/

            // step2 : 分发
            dispatch(messageData);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 消息按类型分发
     */
    public void dispatch(MessageData messageData) {
        String elevatorCode = messageData.getElevatorCode();

        // 获取电梯的推送平台code
        TblElevator elevator = bizElevatorService.getByElevatorCode(elevatorCode);
        if (elevator == null) {
            return;
        }
        String ptCodeStr = elevator.getVHttpPtCodes();
        if (StringUtils.isBlank(ptCodeStr)) {
            return;
        }
        String[] ptCodeList = ptCodeStr.split(",");
        for (String ptCode : ptCodeList) {
            MonitorSend sender = senders.get(ptCode);
            if (null != sender) {
                dispatchExecutorService.submit(() -> sender.handleMonitor(messageData));
            }
        }
    }

}
