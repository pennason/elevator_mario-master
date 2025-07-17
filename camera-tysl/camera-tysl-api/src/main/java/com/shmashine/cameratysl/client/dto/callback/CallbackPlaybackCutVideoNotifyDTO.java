// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.client.dto.callback;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 回调请求⽅式
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/23 16:20
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "云回看剪辑文件下载回调通知", description = "云回看剪辑文件下载回调通知")
public class CallbackPlaybackCutVideoNotifyDTO implements Serializable {
    /**
     * 回调状态返回码， 200：成功，其他：失败
     */
    private Integer code;
    private String msg;
    private DownloadFileInfo data;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DownloadFileInfo implements Serializable {
        /**
         * 剪辑⽂件下载地址，过期时间5分钟
         */
        @Schema(title = "剪辑⽂件下载地址", description = "剪辑⽂件下载地址，过期时间5分钟")
        private String downloadUrl;
        /**
         * 剪辑⽂件名
         */
        @Schema(title = "剪辑⽂件名", description = "剪辑⽂件名")
        private String fileName;
        /**
         * 本系统设备通道唯⼀标识
         */
        @Schema(title = "本系统设备通道唯⼀标识", description = "本系统设备通道唯⼀标识", required = true)
        private String guid;
        /**
         * 事件id，⽤于分辨事件
         */
        @Schema(title = "事件id", description = "事件id，⽤于分辨事件", required = true)
        private String uid;
        /**
         * 请求剪辑时间
         */
        @Schema(title = "请求剪辑时间", description = "请求剪辑时间", required = true)
        private String time;
    }
}
