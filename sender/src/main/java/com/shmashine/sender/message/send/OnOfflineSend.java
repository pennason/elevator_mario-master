// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.message.send;

import com.shmashine.common.message.OnOfflineMessage;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/24 13:33
 * @since v1.0
 */

public interface OnOfflineSend {

    /**
     * 获取电推送的平台CODE
     */
    String getPtCode();

    /**
     * 在线离线消息处理
     *
     * @param message 在线离线消息
     */
    void handleOnOfflineStatus(OnOfflineMessage message);
}
