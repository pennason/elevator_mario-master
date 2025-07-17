// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 15:15
 * @since v1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TyslDevicePlaybackUrlResponseDTO
        extends PageResponseDTO<TyslDevicePlaybackUrlResponseDTO.PlayBackUrlData> {

    /**
     * url
     */
    @Data
    public static class PlayBackUrlData implements Serializable {
        /**
         * 视频流url。各平台情况如下： ⼤华：rtsp或hls流地址。⼤华暂时没有录像⽂件下载地址 中兴：rtsp流地址 天翼云眼：rtsp或hls流地址
         */
        private String url;
        /**
         * 流媒体标识，通过视频回看接⼝获取
         */
        private String ssrc;
    }
}
