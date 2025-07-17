// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway;

import com.shmashine.cameratysl.gateway.dto.VoiceTalkbackResponseDTO;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/11/21 15:54
 * @since v1.0
 */

public interface VoiceGateway {

    /**
     * 根据设备ID 获取 token信息
     *
     * @param deviceId 设备ID
     * @return 结果
     */
    VoiceTalkbackResponseDTO getTalkbackToken(String deviceId);
}
