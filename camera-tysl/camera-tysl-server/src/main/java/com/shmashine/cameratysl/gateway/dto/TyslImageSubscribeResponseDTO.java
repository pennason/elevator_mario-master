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
 * @version v1.0  -  2023/8/21 16:47
 * @since v1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TyslImageSubscribeResponseDTO extends TyslResponseDTO<TyslImageSubscribeResponseDTO.SubscribeInfo> {

    /**
     * 文件列表
     */
    @Data
    public static class SubscribeInfo implements Serializable {
        /**
         * 失败设备列表
         */
        private List<FailDeviceItem> list;
    }

    /**
     * 文件信息
     */
    @Data
    public static class FailDeviceItem implements Serializable {
        /**
         * 统⼀设备管理平台中设备通道唯⼀标识
         */
        private String guid;
        /**
         * 错误码，0为设备⽆权限
         */
        private String failCode;
    }
}
