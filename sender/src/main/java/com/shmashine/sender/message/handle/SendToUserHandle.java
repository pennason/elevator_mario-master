package com.shmashine.sender.message.handle;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.sender.entity.SendToUser;
import com.shmashine.sender.entity.SendToUserCache;
import com.shmashine.sender.message.kafka.KafkaProducer;
import com.shmashine.sender.server.ElevatorCacheServiceI;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 推送对接用户处理类
 *
 * @author chenxue
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class SendToUserHandle {
    private final ElevatorCacheServiceI elevatorCacheService;
    private final KafkaProducer kafkaProducer;

    private static final Logger SEND_TO_USER_LOGGER = LoggerFactory.getLogger("sendToUserLogger");

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(500, 1000,
            3L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "SendToUserHandle");

    /**
     * 推送实时消息
     *
     * @param elevatorCode 电梯编号
     * @param message      消息
     */
    public void monitorMessage(String elevatorCode, String message) {

        executorService.submit(() -> {
            String topic = "SEND_TO_USER_MONITOR_";
            pushMessage(elevatorCode, message, topic);
        });

    }

    /**
     * 推送故障消息
     *
     * @param elevatorCode 电梯编号
     * @param message      消息
     */
    public void faultMessage(String elevatorCode, String message) {

        executorService.submit(() -> {
            String topic = "SEND_TO_USER_FAULT_";
            pushMessage(elevatorCode, message, topic);
        });

    }

    private void pushMessage(String elevatorCode, String message, String topic) {
        //获取需要推送的对接用户
        List<SendToUser> users = SendToUserCache.getSendCache(elevatorCode);
        if (users == null) {
            return;
        }

        var jsonObject = JSON.parseObject(message);

        //推送用户
        for (SendToUser user : users) {

            jsonObject.put("registrtionCode", user.getRegistrtionCode() == null ? "" : user.getRegistrtionCode());
            jsonObject.put("manufacturerCode", user.getManufacturerCode() == null ? "" : user.getManufacturerCode());
            jsonObject.put("leaveFactoryNumber", user.getLeaveFactoryNumber() == null
                    ? "" : user.getLeaveFactoryNumber());

            kafkaProducer.sendMessageToKafka(topic + user.getUserName(), jsonObject.toJSONString());

            SEND_TO_USER_LOGGER.info("sendToUSer To : {}, Elevator: {}, message: {}", topic + user.getUserName(),
                    elevatorCode, jsonObject.toJSONString());

            // 上海第六人民医院的需要做缓存
            if ("shdc".equals(user.getUserName())) {
                if (topic.contains("FAULT")) {
                    cacheFaultMessage(elevatorCode, jsonObject);
                } else {
                    cacheMonitorMessage(elevatorCode, jsonObject);
                }
            }
        }
    }

    private <T> void cacheMonitorMessage(String elevatorCode, T message) {
        elevatorCacheService.setMonitorCache(elevatorCode, message);
    }

    private <T> void cacheFaultMessage(String elevatorCode, T message) {
        elevatorCacheService.saveFaultMessage(elevatorCode, message);
    }
}
