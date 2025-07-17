package com.shmashine.socket.message.handle.Escalator100;

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
import com.shmashine.socket.message.bean.MonitorMessageV100E;
import com.shmashine.socket.message.handle.MonitorHandle;
import com.shmashine.socket.redis.RedisService;
import com.shmashine.socket.redis.utils.RedisUtils;
import com.shmashine.socket.websocket.WebSocketManager;

import lombok.extern.slf4j.Slf4j;


/**
 * 上海浮奈(电信扶梯)monitor消息处理
 *
 * @author little.li
 */
@Slf4j
@Component
public class MonitorHandleV100E implements MonitorHandle {

    private static final String PROTOCAL_VERSION = "1.0.0E";

    private final RedisUtils redisUtils;

    private final RedisService redisService;

    private final TblElevatorService elevatorService;

    private final FaultHandleV100E faultHandle;

    private final KafkaProducer kafkaProducer;

    /**
     * 机房报文缓存 - 用于轿顶上报报文合并后统一上报
     */
    private Map<String, MonitorMessage> motorRoomMessageCache = Maps.newConcurrentMap();

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(2, 8,
            1L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "MonitorHandleV100E");


    @Autowired
    public MonitorHandleV100E(RedisUtils redisUtils, RedisService redisService, TblElevatorService elevatorService,
                              FaultHandleV100E faultHandle, KafkaProducer kafkaProducer) {

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

        // 扶梯监控消息处理
        MonitorMessageV100E monitorMessage = convertEscalatorMessage(messageJson, elevatorCode, sensorType);

        // 推送到kafka ：monitor
        messageDate.setMonitorMessage(monitorMessage);

        executorService.submit(() -> kafkaProducer.sendMessageToKafka(KafkaTopicConstants.MONITOR_TOPIC,
                elevatorCode, JSONObject.toJSONString(messageDate)));

        // 向浏览器发送消息
        MonitorMessageV100E finalMonitorMessage = monitorMessage;
        executorService.submit(() ->
                WebSocketManager.sendMessageToBrowsers(
                        elevatorCode, SocketConstants.MESSAGE_TYPE_MONITOR,
                        messageDate.getSensorType(),
                        JSONObject.toJSONString(finalMonitorMessage)
                ));

        // 推送北向
        kafkaProducer.sendMessageToKafka(KafkaTopicConstants.CUBE_MONITOR, elevatorCode,
                JSONObject.toJSONString(messageDate));
    }


    ////////////////////////////////private方法///////////////////////////

    /**
     * 扶梯监控消息处理
     *
     * @param messageJson  消息
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     */
    private MonitorMessageV100E convertEscalatorMessage(JSONObject messageJson, String elevatorCode,
                                                        String sensorType) {

        MonitorMessageV100E monitorMessage = new MonitorMessageV100E();
        // 扶梯设备base64报文数据解析
        monitorMessage.setFromBase64ForEscalator(messageJson);

        // 添加设备类型
        monitorMessage.setSensorType(sensorType);

        if (monitorMessage.getFaultStatus() == 0) {
            // 当前设备状态为正常，清空所有故障
            faultHandle.cleanAllFault(elevatorCode, sensorType);
        }

        return monitorMessage;

    }


}
