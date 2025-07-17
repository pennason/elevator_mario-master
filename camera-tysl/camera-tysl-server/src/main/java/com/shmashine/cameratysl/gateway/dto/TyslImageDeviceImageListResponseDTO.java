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
 * @version v1.0  -  2023/8/21 16:18
 * @since v1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TyslImageDeviceImageListResponseDTO
        extends TyslResponseDTO<TyslImageDeviceImageListResponseDTO.ImageDeviceImageListDTO> {

    /**
     * 照片列表
     */
    @Data
    public static class ImageDeviceImageListDTO implements Serializable {
        /**
         * 查询总数
         */
        private String count;
        /**
         * 查询上⼀次返回结果中sort值数组(⾮⾸次查询)
         */
        private String sortArr;
        /**
         * 分⻚列表
         */
        private List<DeviceImageDataDtoItem> list;
    }

    /**
     * 设备图
     */
    @Data
    public static class DeviceImageDataDtoItem implements Serializable {
        /**
         * 统⼀设备管理平台中设备通道唯⼀标识
         */
        private String guid;
        /**
         * 图⽚下载地址
         */
        private String src;
        /**
         * 图⽚id
         */
        private String imageId;
        /**
         * 图⽚类型: 机动⻋:9, ⼈脸:11, 背景: 14
         */
        private Integer imageType;
        /**
         * 抓拍时间yyyy-MM-dd HH:mm:ss.000毫秒
         */
        private String time;
        /**
         * 图⽚组id, ⽐如: ⼈脸id/机动⻋id
         */
        private String imageGroupId;
        /**
         * 特征值
         */
        private String features;
        /**
         * 0:特征组，14:背景组
         */
        private Integer imageTypeGroup;
    }
}
