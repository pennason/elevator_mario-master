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
 * @version v1.0  -  2023/8/21 14:59
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceStreamUrlRequestDTO implements Serializable {
    /**
     * 统⼀设备管理平台中设备通道唯⼀标识
     */
    @Schema(title = "统⼀设备管理平台中设备通道唯⼀标识", description = "统⼀设备管理平台中设备通道唯⼀标识", required = true)
    private String guid;
    /**
     * 视频协议 1:rtsp 2:hls 3:flv 4:rtmp，不填默认rtsp。 ⼤华：⽀持rtsp、hls 中兴：⽀持rtsp 天翼云眼：⽀持rtsp、hls
     */
    @Schema(title = "视频协议",
            description = "视频协议 1:rtsp 2:hls 3:flv 4:rtmp，不填默认rtsp。 ⼤华：⽀持rtsp、hls 中兴：⽀持rtsp 天翼云眼：⽀持rtsp、hls",
            example = "2", defaultValue = "2")
    private Integer proto;
    /**
     * 访问类型，接⼊⽹络类型 0：内⽹，1：公⽹，2: 其他
     */
    @Schema(title = "访问类型", description = "访问类型，接⼊⽹络类型 0：内⽹，1：公⽹，2: 其他", example = "1", defaultValue = "1")
    private Integer accessType;
    /**
     * 码流类型 0：主码流、1：⼦码流、 2：第三码流。 不填默认为主码流。 天翼云眼：0：⾼清，1：标清
     */
    @Schema(title = "码流类型",
            description = "码流类型 0：主码流、1：⼦码流、 2：第三码流。 不填默认为主码流。 天翼云眼：0：⾼清，1：标清", example = "0", defaultValue = "0")
    private Integer streamType;
}
