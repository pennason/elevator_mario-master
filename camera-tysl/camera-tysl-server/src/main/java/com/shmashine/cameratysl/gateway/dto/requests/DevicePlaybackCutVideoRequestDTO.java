// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.dto.requests;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/23 16:01
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DevicePlaybackCutVideoRequestDTO implements Serializable {
    /**
     * 统⼀设备管理平台中设备通道唯⼀标识
     */
    @Schema(title = "统⼀设备管理平台中设备通道唯⼀标识", description = "统⼀设备管理平台中设备通道唯⼀标识", required = true)
    private String guid;

    /**
     * 事件id，⽤于分辨事件
     */
    @Schema(title = "事件id", description = "事件id，⽤于分辨事件", required = true)
    private String uid;

    /**
     * 时间, yyyyMMddHHmmss
     */
    @Schema(title = "时间", description = "时间, yyyyMMddHHmmss", required = true)
    private String time;

    /**
     * 接受剪辑⽂件下载地址回调地址
     */
    @Schema(title = "接受剪辑⽂件下载地址回调地址", description = "接受剪辑⽂件下载地址回调地址", required = true)
    private String callbackUrl;
}
