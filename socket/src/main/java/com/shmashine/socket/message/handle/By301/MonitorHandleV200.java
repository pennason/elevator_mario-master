package com.shmashine.socket.message.handle.By301;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Maps;
import com.shmashine.common.constants.SocketConstants;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.socket.elevator.service.TblElevatorService;
import com.shmashine.socket.kafka.KafkaProducer;
import com.shmashine.socket.kafka.KafkaTopicConstants;
import com.shmashine.socket.message.MessageHandle;
import com.shmashine.socket.message.bean.MessageData;
import com.shmashine.socket.message.bean.MonitorMessage;
import com.shmashine.socket.message.bean.MonitorMessageV200;
import com.shmashine.socket.message.handle.MonitorHandle;
import com.shmashine.socket.redis.RedisService;
import com.shmashine.socket.redis.utils.RedisUtils;
import com.shmashine.socket.websocket.WebSocketManager;

import lombok.extern.slf4j.Slf4j;


/**
 * monitor消息处理
 *
 * @author little.li
 * @version 2.0.0
 */
@Slf4j
@Component
public class MonitorHandleV200 implements MonitorHandle {

    private static final String PROTOCAL_VERSION = "2.0.0";

    private final RedisUtils redisUtils;

    private final RedisService redisService;

    private final TblElevatorService elevatorService;

    private final FaultHandleV200 faultHandle;

    private final KafkaProducer kafkaProducer;

    /**
     * 机房报文缓存 - 用于轿顶上报报文合并后统一上报
     */
    private Map<String, MonitorMessage> motorRoomMessageCache = Maps.newConcurrentMap();

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(2, 8,
            1L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "MonitorHandleV200");


    @Autowired
    public MonitorHandleV200(RedisUtils redisUtils, RedisService redisService, TblElevatorService elevatorService,
                             FaultHandleV200 faultHandle, KafkaProducer kafkaProducer) {

        this.redisService = redisService;
        this.elevatorService = elevatorService;
        this.faultHandle = faultHandle;
        this.kafkaProducer = kafkaProducer;
        this.redisUtils = redisUtils;
    }

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
     * 监控消息处理
     *
     * @param messageDate messageDate
     */
    @Override
    public void monitorHandle(MessageData messageDate) {

        String sensorType = messageDate.getSensorType();
        String elevatorCode = messageDate.getElevatorCode();
        JSONObject messageJson = messageDate.getMessageJson();

        MonitorMessageV200 monitorMessage = convertMessage(messageJson, elevatorCode, sensorType);

        // 推送到kafka ：monitor
        messageDate.setMonitorMessage(monitorMessage);

        executorService.submit(() -> kafkaProducer.sendMessageToKafka(KafkaTopicConstants.MONITOR_TOPIC,
                elevatorCode, JSONObject.toJSONString(messageDate)));

        // 向浏览器发送消息
        MonitorMessageV200 finalMonitorMessage = monitorMessage;
        executorService.submit(() ->
                WebSocketManager.sendMessageToBrowsers(
                        elevatorCode, SocketConstants.MESSAGE_TYPE_MONITOR,
                        messageDate.getSensorType(),
                        JSONObject.toJSONString(finalMonitorMessage)
                ));

        // 合并机房轿顶信息后推送到 合并的 topic:
        executorService.submit(() -> kafkaProducer.sendMessageToKafka(KafkaTopicConstants.CUBE_MONITOR,
                elevatorCode, JSONObject.toJSONString(messageDate)));
    }


    /// //////////////////////////////private方法/////////////////////////


    private MonitorMessageV200 convertMessage(JSONObject messageJson, String elevatorCode, String sensorType) {

        MonitorMessageV200 monitorMessage = new MonitorMessageV200();
        // base64报文数据解析
        monitorMessage.setFromBase64ForSingleBox(messageJson);
        String deviceFloor = monitorMessage.getFloor();

        // 当前楼层处理
        String floor = elevatorService.getRightFloor(elevatorCode, deviceFloor, messageJson);
        monitorMessage.setFloor(floor);

        // 停止服务模式处理
        /*Integer modeStatus;
        Integer deviceModeStatus = monitorMessage.getModeStatus();
        if (deviceModeStatus == 1) {
            modeStatus = redisService.carRoofUpdateModeStatus(elevatorCode, 2);
        } else {
            modeStatus = redisService.carRoofUpdateModeStatus(elevatorCode, 0);
        }
        monitorMessage.setModeStatus(modeStatus);*/

        // 添加设备类型
        monitorMessage.setSensorType(sensorType);
        if (monitorMessage.getNowStatus() != null && 0 == monitorMessage.getNowStatus()) {
            // 当前设备状态为正常，清空所有故障
            faultHandle.cleanAllFault(elevatorCode, sensorType);
        }
        return monitorMessage;

    }


}
