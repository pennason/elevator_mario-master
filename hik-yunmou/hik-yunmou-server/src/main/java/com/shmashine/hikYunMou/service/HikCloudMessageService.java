// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hikYunMou.service;

import com.shmashine.hikYunMou.client.dto.HikCloudMessageResponseDTO;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/17 10:15
 * @since v1.0
 */

public interface HikCloudMessageService {

    /**
     * 处理设备上下线
     *
     * @param hikMessageInfo 消息
     */
    void dealDeviceOnOffline(HikCloudMessageResponseDTO.HikMessageInfo hikMessageInfo);

    /**
     * 云录制 转码录制结果事件
     *
     * @param hikMessageInfo 消息
     */
    void dealCloudVideoRecord(HikCloudMessageResponseDTO.HikMessageInfo hikMessageInfo);
}
