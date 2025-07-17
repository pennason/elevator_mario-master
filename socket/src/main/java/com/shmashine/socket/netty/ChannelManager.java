package com.shmashine.socket.netty;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.Maps;
import com.shmashine.common.utils.MessageUtils;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * netty channel连接管理
 *
 * @author little.li
 */
public class ChannelManager {


    public static final AttributeKey<String> CHANNEL_KEY = AttributeKey.valueOf("channel_key");

    private static Logger sendMessageLogger = LoggerFactory.getLogger("sendMessageLogger");

    /**
     * channel管理map
     * key:   Cube_MX3390 / CarRoof_MX3390 / MotorRoom_MX3390
     * value: channel
     */
    private static final Map<String, Channel> CHANNEL_MAP = Maps.newConcurrentMap();


    /**
     * 添加channel
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @param channel      channel
     * @return reconnection 设备是否为重连
     */
    public static boolean addChannel(String elevatorCode, String sensorType, Channel channel) {
        boolean reconnection = false;

        String key = sensorType + "_" + elevatorCode;
        // channel中记录key
        channel.attr(CHANNEL_KEY).set(key);
        // 关闭已经失效的channel连接
        if (CHANNEL_MAP.get(key) != null) {
            Channel badChannel = CHANNEL_MAP.get(key);
            badChannel.close();

            reconnection = true;
        }
        CHANNEL_MAP.put(key, channel);
        return reconnection;
    }


    /**
     * 移除channel
     *
     * @param channel channel
     * @return 设备标识 / Cube_MX3390
     */
    public static String delChannel(Channel channel) {
        // 获取channel中记录key
        String channelKey = channel.attr(CHANNEL_KEY).get();
        if (!CollectionUtils.isEmpty(CHANNEL_MAP) && StringUtils.hasText(channelKey)) {
            CHANNEL_MAP.remove(channelKey);
        }
        return channelKey;
    }


    /**
     * 发送消息到channel客户端
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @param message      消息体
     */
    public static void sendMessageToChannel(String elevatorCode, String sensorType, String message) {
        if (!StringUtils.hasText(message)) {
            return;
        }
        // 将消息转换为设备对应的类型
        message = MessageUtils.convertMessageToSensor(message, sensorType);

        // 拼接key 获取channel
        String key = sensorType + "_" + elevatorCode;
        Channel channel = CHANNEL_MAP.get(key);
        if (channel != null) {
            channel.writeAndFlush(message);
        }

        sendMessageLogger.info("{}, 给elevator[{}], sensorType[{}]发送消息, message{} ",
                new Date(), elevatorCode, sensorType, message);
    }


    /**
     * 根据channel获取电梯编号
     *
     * @param channel channel
     * @return 电梯编号
     */
    public static String getElevatorCodeByChannel(Channel channel) {
        // 获取channel中记录key
        String channelKey = channel.attr(CHANNEL_KEY).get();
        if (!StringUtils.hasText(channelKey)) {
            return "";
        }
        String[] valueSplit = channelKey.split("_");
        return valueSplit[1];
    }


    /**
     * 根据channel获取设备类型
     *
     * @param channel channel
     * @return 设备类型
     */
    public static String getSensorTypeByChannel(Channel channel) {
        // 获取channel中记录key
        String channelKey = channel.attr(CHANNEL_KEY).get();
        if (!StringUtils.hasText(channelKey)) {
            return "";
        }
        String[] valueSplit = channelKey.split("_");
        return valueSplit[0];
    }


    /**
     * 获取所有channel
     */
    public static Map<String, Channel> getAllChannel() {
        return CHANNEL_MAP;
    }

    public static String getChannelStatus(String elevatorCode, String sensorType) {

        String key = sensorType + "_" + elevatorCode;
        Channel channel = CHANNEL_MAP.get(key);
        // 关闭已经失效的channel连接
        if (channel != null && channel.isOpen()) {
            return "在线";
        } else {
            return "离线";
        }
    }

    public static Integer deviceIsOnline(String elevatorCode, String sensorType) {

        String key = sensorType + "_" + elevatorCode;
        Channel channel = CHANNEL_MAP.get(key);
        // 关闭已经失效的channel连接
        if (channel != null && channel.isOpen()) {
            return 1;
        } else {
            return 0;
        }
    }


    /**
     * 获取所有设备状态
     */
    public static Set<String> getAllChannelStatus() {

        Set<String> keys = CHANNEL_MAP.keySet();

        keys.stream().filter(it -> CHANNEL_MAP.get(it) != null && CHANNEL_MAP.get(it).isOpen());

        return keys;

    }

}
