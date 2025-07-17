package com.shmashine.sender.message.send;

import com.shmashine.common.message.FaultMessage;


/**
 * 消息处理类
 */
public interface FaultSend {

    /**
     * 获取电推送的平台CODE
     */
    String getPtCode();

    /**
     * 根据不同消息类型，分支处理
     *
     * @param faultMessage 故障消息
     */
    void handleFault(FaultMessage faultMessage);
}
