package com.shmashine.socket.message.handle;

import com.shmashine.socket.message.bean.MessageData;

import io.netty.channel.Channel;

/**
 * 登录消息处理
 *
 * @author jiangheng
 * @version 2022/8/30 13:55
 */
public interface LoginHandle {

    /**
     * 获取协议版本
     */
    String getProtocalVersion();

    /**
     * ST:login - 登录消息处理
     *
     * @param messageData messageData
     * @param channel     channel
     */
    boolean loginHandle(MessageData messageData, Channel channel);

    /**
     * ST:deviceInfo 设备信息消息处理
     *
     * @param messageData messageData
     * @param channel     channel
     */
    void deviceInfoHandle(MessageData messageData, Channel channel);

}
