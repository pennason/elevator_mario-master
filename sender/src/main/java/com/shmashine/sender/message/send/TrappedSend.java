package com.shmashine.sender.message.send;

import com.shmashine.common.message.FaultMessage;


/**
 * 困人 消息处理类
 */
public interface TrappedSend {

    /**
     * 获取电推送的平台CODE
     */
    String getPtCode();

    /**
     * 困人消息处理
     *
     * @param faultMessage 消息json格式
     */
    void handleTrapped(FaultMessage faultMessage);
}
