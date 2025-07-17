// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 16:37
 * @since v1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TyslImageImageDetailResponseDTO extends TyslResponseDTO<TyslImageImageDetailResponseDTO.ImageDetail> {

    /**
     * 图片详情
     */
    @Data
    public static class ImageDetail implements Serializable {
        /**
         * 统⼀设备管理平台中设备通道唯⼀标识
         */
        private String guid;
        /**
         * 背景⼤图下载地址
         */
        private String backSrc;
        /**
         * 抓拍时间yyyy-MM-dd HH:mm:ss.000毫秒
         */
        private String time;
        /**
         * 图⽚组id, ⽐如: ⼈脸id/机动⻋id
         */
        private String imageGroupId;
        /**
         * 扣出来的⼩图下载地址
         */
        private List<String> digSrc;
    }
}
