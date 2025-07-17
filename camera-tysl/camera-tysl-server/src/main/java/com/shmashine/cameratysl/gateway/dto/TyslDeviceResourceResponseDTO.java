// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 15:39
 * @since v1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TyslDeviceResourceResponseDTO extends TyslResponseDTO<TyslDeviceResourceResponseDTO.ResourceDTO> {

    /**
     * 详情
     */
    @Data
    public static class ResourceDTO implements Serializable {
        /**
         * 统⼀设备管理平台中设备通道唯⼀标识
         */
        private String guid;
        /**
         * 设备名称
         */
        private String name;
        /**
         * 设备地址
         */
        private String address;
        /**
         * 通道级联编码(对应麦信的channel_code)
         */
        private String channelNo;
        /**
         * 通道号 对应麦信的 channelNo
         */
        private Integer channelSeq;

        private String identity;

        private Integer status;
        /**
         * 摄像头类型
         */
        private Integer cameraType;
        /**
         * 设备码
         */
        private String deviceCode;
        /**
         * 经度
         */
        private String lng;
        /**
         * 纬度
         */
        private String lat;
        /**
         * 设备类型
         */
        private Integer deviceType;
        /**
         * 设备所属汇聚平台 QQY:⼤华, TYBD:中兴, TYYY:天翼云眼
         */
        private String vendorCode;
        /**
         * 厂商ID
         */
        private Integer platformid;
        /**
         * 厂商名称
         */
        private String platformName;
    }
}
