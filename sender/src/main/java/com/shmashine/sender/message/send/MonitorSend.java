package com.shmashine.sender.message.send;

import com.shmashine.common.message.MessageData;


/**
 * 消息处理类
 */
public interface MonitorSend {

    /**
     * 获取电推送的平台CODE
     */
    String getPtCode();

    /**
     * 故障消息处理
     *
     * @param messageData 监控消息
     */
    void handleMonitor(MessageData messageData);
}
