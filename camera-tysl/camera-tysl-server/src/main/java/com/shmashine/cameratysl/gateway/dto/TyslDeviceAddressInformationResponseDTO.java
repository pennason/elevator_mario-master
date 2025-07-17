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
 * @version v1.0  -  2023/8/21 15:43
 * @since v1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TyslDeviceAddressInformationResponseDTO
        extends TyslResponseDTO<TyslDeviceAddressInformationResponseDTO.DeviceInfo> {

    /**
     * 总览
     */
    @Data
    public static class DeviceInfo implements Serializable {
        /**
         * 响应码
         */
        private String code;
        /**
         * 响应消息
         */
        private String msg;
        /**
         * 结果集
         */
        private List<DeviceInfoItem> data;
    }

    /**
     * 详情
     */
    @Data
    public static class DeviceInfoItem implements Serializable {

        private String uid;
        /**
         * 设备CTEI码
         */
        private String ctei;
        /**
         * 请求IP
         */
        private String reqIp;
        /**
         * 请求端口
         */
        private String reqPort;
    }
}
