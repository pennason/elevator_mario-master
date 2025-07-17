// Copyright (C) 2024 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.message.send;

import com.shmashine.common.message.PeriodicMessage;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/6/20 15:51
 * @since v1.0
 */

public interface PeriodicSend {

    /**
     * 获取电推送的平台CODE
     */
    String getPtCode();

    /**
     * 根据不同消息类型，分支处理
     */
    void handlePeriodic(PeriodicMessage message);
}
