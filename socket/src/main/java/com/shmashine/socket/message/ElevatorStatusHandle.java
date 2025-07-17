package com.shmashine.socket.message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.hutool.core.exceptions.ExceptionUtil;

import com.alibaba.fastjson2.JSON;
import com.shmashine.common.constants.RedisConstants;
import com.shmashine.common.constants.SocketConstants;
import com.shmashine.common.message.OnOfflineMessage;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.common.utils.IpUtils;
import com.shmashine.common.utils.TimeUtils;
import com.shmashine.socket.device.service.TblDeviceService;
import com.shmashine.socket.elevator.service.TblElevatorService;
import com.shmashine.socket.kafka.KafkaProducer;
import com.shmashine.socket.kafka.KafkaTopicConstants;
import com.shmashine.socket.netty.ChannelManager;
import com.shmashine.socket.redis.RedisService;
import com.shmashine.socket.redis.utils.RedisKeyUtils;

import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 电梯状态处理
 *
 * @author little.li
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ElevatorStatusHandle {
    private final RedisService redisService;

    private final TblDeviceService deviceService;

    private final TblElevatorService elevatorService;

    private final StringRedisTemplate stringRedisTemplate;
    private final KafkaProducer kafkaProducer;

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(8, 32,
            3L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(300), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "ElevatorStatusHandle");


    /**
     * channel连接处理
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     */
    public void onLineHandle(String elevatorCode, String sensorType, String sensorIp) {
        // 获取服务器ip和当前格式化时间
        String serverIp = IpUtils.getLocalIP();
        String nowTime = TimeUtils.nowTime();


        try {

            // 更新设备状态
            deviceService.updateOnLineStatus(elevatorCode, sensorType, serverIp, nowTime);

            // 更新Redis中的状态
            redisService.updateOnLineStatus(elevatorCode, sensorType, serverIp, sensorIp, nowTime);

            boolean updateElevatorOffLine = isUpdateElevatorOffLine(elevatorCode, sensorType);

            if (updateElevatorOffLine) {
                // 更新电梯状态
                elevatorService.updateOnlineStatus(elevatorCode, 1);
            }
            // 发送上线状态消息
            sendOnlineStatus(elevatorCode, sensorType, nowTime, true);

        } catch (Exception e) {
            log.error("onLineHandle {}, {}", e.getMessage(), ExceptionUtil.stacktraceToString(e));
        }
    }


    /**
     * channel断开处理
     *
     * @param channel channel
     */
    public void offLineHandle(Channel channel, String reason) {

        executorService.submit(() -> {

            // 删除map中元素 并获取被删除的电梯编号和设备类型
            String channelKey = ChannelManager.delChannel(channel);
            // 关闭channel
            channel.close();
            if (!StringUtils.hasText(channelKey)) {
                return;
            }

            String elevatorCode = channelKey.split("_")[1];
            String sensorType = channelKey.split("_")[0];

            String nowTime = TimeUtils.nowTime();
            // 更新Redis中设备、电梯离线状态
            boolean updateOffLineStatus = redisService.updateOffLineStatus(elevatorCode, sensorType, nowTime);
            // 新建设备离线记录
            deviceService.insertDeviceEventRecord(elevatorCode, sensorType, 2, reason);
            updateOnlineStatus(elevatorCode, sensorType, nowTime, updateOffLineStatus);

            deviceTimeOutEvent(elevatorCode, sensorType);

            // 发送下线状态消息
            sendOnlineStatus(elevatorCode, sensorType, nowTime, false);

        });

    }

    void updateOnlineStatus(String elevatorCode, String sensorType, String nowTime, boolean updateOffLineStatus) {
        // 更新设备离线状态
        deviceService.updateOffLineStatus(elevatorCode, sensorType, nowTime);
        // 更新电梯离线状态
        if (updateOffLineStatus) {
            elevatorService.updateOnlineStatus(elevatorCode, 0);
        }
    }

    /*添加设备离线告警*/
    public void deviceTimeOutEvent(String elevatorCode, String sensorType) {
        //离线一小时告警
        long millis = System.currentTimeMillis() / 1000 + 3600;
        String value = elevatorCode + ":" + sensorType;
        stringRedisTemplate.opsForZSet().add(RedisConstants.TIMEOUT_DEVICE, value, millis);
    }

    /*取消设备离线告警*/
    public void cancelDeviceTimeOutEvent(String elevatorCode, String sensorType) {

        String value = elevatorCode + ":" + sensorType;

        Double score = stringRedisTemplate.opsForZSet().score(RedisConstants.TIMEOUT_DEVICE, value);

        //移除记录
        stringRedisTemplate.opsForZSet().remove(RedisConstants.TIMEOUT_DEVICE, value);

        if (score == null || score >= ((double) System.currentTimeMillis() / 1000)) {
            //恢复故障
            deviceService.cancelDeviceTimeOutEvent(elevatorCode, sensorType);
        }

    }

    /**
     * 根据不同设备类型，判断是否将电梯设置为离线
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @return 是否将电梯设置为离线
     */
    private boolean isUpdateElevatorOffLine(String elevatorCode, String sensorType) {
        switch (sensorType) {
            case SocketConstants.SENSOR_TYPE_CUBE, SocketConstants.SENSOR_TYPE_ESCALATOR,
                 SocketConstants.SENSOR_TYPE_SINGLEBOX, SocketConstants.SENSOR_TYPE_FRONT,
                 SocketConstants.SENSOR_TYPE_CAR_DOOR -> {
                return true;
            }
            case SocketConstants.SENSOR_TYPE_CAR_ROOF -> {
                String deviceStatus
                        = RedisKeyUtils.getDeviceStatus(elevatorCode, SocketConstants.SENSOR_TYPE_MOTOR_ROOM);

                String onLine = redisService.getDeviceStatus(deviceStatus, "online");
                if ("1".equals(onLine)) {
                    return true;
                }
                break;
            }
            case SocketConstants.SENSOR_TYPE_MOTOR_ROOM -> {
                String deviceStatus = RedisKeyUtils.getDeviceStatus(elevatorCode, SocketConstants.SENSOR_TYPE_CAR_ROOF);
                String onLine = redisService.getDeviceStatus(deviceStatus, "online");
                if ("1".equals(onLine)) {
                    return true;
                }
                break;
            }
            default -> {
                return false;
            }
        }
        return false;
    }

    private void sendOnlineStatus(String elevatorCode, String sensorType, String nowTime, boolean onlineOffline) {
        // 发送上线状态消息
        var message = OnOfflineMessage.builder()
                .elevatorCode(elevatorCode)
                .sensorType(sensorType)
                .online(onlineOffline)
                .time(nowTime)
                .build();
        kafkaProducer.sendMessageToKafka(KafkaTopicConstants.CUBE_ONLINE_OFFLINE, JSON.toJSONString(message));
    }

    /**
     * 人流量统计是否开启
     *
     * @param elevatorCode 电梯编号
     */
    public boolean getDetectedPeopleNumsIsOpen(String elevatorCode) {
        return elevatorService.getDetectedPeopleNumsIsOpen(elevatorCode);
    }
}
