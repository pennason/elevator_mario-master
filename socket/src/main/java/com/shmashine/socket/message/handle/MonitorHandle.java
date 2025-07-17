package com.shmashine.socket.message.handle;

import com.shmashine.socket.message.bean.MessageData;

/**
 * 监控消息处理
 *
 * @author jiangheng
 * @version V1.0.0 - 2022/8/30 13:39
 */
public interface MonitorHandle {

    /**
     * 获取协议版本
     */
    String getProtocalVersion();

    /**
     * 监控消息处理
     *
     * @param messageDate messageDate
     */
    void monitorHandle(MessageData messageDate);

}
