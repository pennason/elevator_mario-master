package com.shmashine.socket.message.handle.Escalator100;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.constants.MessageConstants;
import com.shmashine.common.constants.SocketConstants;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.common.utils.TimeUtils;
import com.shmashine.socket.device.service.TblDeviceService;
import com.shmashine.socket.message.ElevatorStatusHandle;
import com.shmashine.socket.message.MessageHandle;
import com.shmashine.socket.message.bean.MessageData;
import com.shmashine.socket.message.bean.ProtocalVersionCache;
import com.shmashine.socket.message.handle.LoginHandle;
import com.shmashine.socket.netty.ChannelManager;
import com.shmashine.socket.redis.RedisService;
import com.shmashine.socket.websocket.WebSocketManager;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * 上海浮奈(电信扶梯)登录消息处理
 *
 * @author little.li
 */
@Slf4j
@Component
public class LoginHandleV100E implements LoginHandle {

    private static final String PROTOCAL_VERSION = "1.0.0E";

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(8, 64,
            3L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "LoginHandleV100E");

    private final ElevatorStatusHandle elevatorStatusHandle;

    private final TblDeviceService deviceService;

    private final RedisService redisService;

    private final ProtocalVersionCache protocalVersionCache;

    @Autowired
    public LoginHandleV100E(ElevatorStatusHandle elevatorStatusHandle, TblDeviceService deviceService,
                            RedisService redisService, ProtocalVersionCache protocalVersionCache) {

        this.elevatorStatusHandle = elevatorStatusHandle;
        this.deviceService = deviceService;
        this.redisService = redisService;
        this.protocalVersionCache = protocalVersionCache;
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
     * ST:login - 登录消息处理
     *
     * @param messageData messageData
     * @param channel     channel
     */
    @Override
    public boolean loginHandle(MessageData messageData, Channel channel) {
        JSONObject messageJson = messageData.getMessageJson();
        String sensorType = messageData.getSensorType();

        // 获取登录消息中携带的elevatorCode
        String elevatorCode = messageJson.getString(SocketConstants.MESSAGE_EID);
        messageData.setElevatorCode(elevatorCode);
        String reason = messageJson.getString(SocketConstants.MESSAGE_REASON);
        String sensorIp = messageJson.getString(SocketConstants.SENSOR_IP);

        // 校验设备是否存在
        boolean result = deviceService.checkDevice(elevatorCode, sensorType);

        if (result) {
            try {

                // ChannelMap来管理channel
                boolean reconnection = ChannelManager.addChannel(elevatorCode, sensorType, channel);

                //添加设备协议
                executorService.submit(() -> protocalVersionCache.refreshProtocalVersion(elevatorCode
                        + sensorType, messageJson.getString(SocketConstants.PROTOCAL_VERSION)));

                //开启monitor
                executorService.submit(() -> startMonitor(sensorType, elevatorCode));

                // 更新设备信息
                executorService.submit(() -> deviceInfoHandle(messageData, channel));

                // 添加设备上线记录
                executorService.submit(() -> {

                    if (reconnection) {
                        // 设备重连
                        deviceService.insertDeviceEventRecord(elevatorCode, sensorType, 3, reason);
                    } else {
                        // 设备上线
                        deviceService.insertDeviceEventRecord(elevatorCode, sensorType, 1, reason);
                    }
                });

                // 更新数据库和Redis中在线状态
                executorService.submit(() -> elevatorStatusHandle.onLineHandle(elevatorCode, sensorType, sensorIp));

                //取消设备离线告警
                executorService.submit(() -> elevatorStatusHandle.cancelDeviceTimeOutEvent(elevatorCode, sensorType));

                return true;

            } catch (Exception e) {
                e.printStackTrace();
                channel.close();
            }
        } else {
            channel.close();
        }

        return false;
    }


    /**
     * ST:deviceInfo 设备信息消息处理
     *
     * @param messageData messageData
     * @param channel     channel
     */
    @Override
    public void deviceInfoHandle(MessageData messageData, Channel channel) {
        JSONObject messageJson = messageData.getMessageJson();
        // 获取电梯编号
        String elevatorCode = ChannelManager.getElevatorCodeByChannel(channel);
        String sensorType = messageData.getSensorType();
        // 更新设备信息
        try {
            deviceService.updateDevice(messageJson, elevatorCode, sensorType);
        } catch (Exception e) {
            log.error("{}--- 更新设备信息失败 --- {}_{} --- {}---error:{}",
                    TimeUtils.nowTime(), sensorType, elevatorCode, messageData, e.getMessage());
        }
    }


    ////////////////////////////////////private method///////////////////////////


    /**
     * 添加channel并开启监控
     *
     * @param sensorType   设备类型
     * @param elevatorCode dainty编号
     */
    private void startMonitor(String sensorType, String elevatorCode) {

        // 返回设备登录成功
        /** 2020年9月30日14:23:43 添加机房和轿顶互通内网ip start */
        String loginSuccess = MessageConstants.LOGIN_SUCCESS;
        if (SocketConstants.SENSOR_TYPE_CAR_ROOF.equals(sensorType)
                || SocketConstants.SENSOR_TYPE_MOTOR_ROOM.equals(sensorType)) {

            loginSuccess = addSensorIpsToMessage(elevatorCode, MessageConstants.LOGIN_SUCCESS);
        }

        //下发登录成功消息
        ChannelManager.sendMessageToChannel(elevatorCode, sensorType, loginSuccess);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /** 2020年9月30日14:23:43 添加机房和轿顶互通内网ip end */
        // 开启Monitor监控
        if (WebSocketManager.sessionListIsEmpty(elevatorCode, SocketConstants.MESSAGE_TYPE_MONITOR, sensorType)) {
            ChannelManager.sendMessageToChannel(elevatorCode, sensorType, MessageConstants.MONITOR_START_5000);
        } else {
            ChannelManager.sendMessageToChannel(elevatorCode, sensorType, MessageConstants.MONITOR_START_500);
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 更新设备时间
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TY", "Update");
        jsonObject.put("ST", "rtc");
        jsonObject.put("daytime", TimeUtils.nowTime());
        ChannelManager.sendMessageToChannel(elevatorCode, sensorType, jsonObject.toJSONString());

    }

    private String addSensorIpsToMessage(String elevatorCode, String message) {
        try {

            JSONObject messageJson = JSONObject.parseObject(message);

            messageJson.put(SocketConstants.SENSOR_TYPE_CAR_ROOF, redisService.getDeviceSensorIp(elevatorCode,
                    SocketConstants.SENSOR_TYPE_CAR_ROOF));

            messageJson.put(SocketConstants.SENSOR_TYPE_MOTOR_ROOM, redisService.getDeviceSensorIp(elevatorCode,
                    SocketConstants.SENSOR_TYPE_MOTOR_ROOM));

            message = messageJson.toJSONString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }


}
