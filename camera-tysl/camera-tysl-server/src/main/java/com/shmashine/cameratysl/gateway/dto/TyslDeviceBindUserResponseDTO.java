// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 15:34
 * @since v1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TyslDeviceBindUserResponseDTO extends TyslResponseDTO<TyslDeviceBindUserResponseDTO.BindUserDTO> {

    /**
     * 用户电话
     */
    @Data
    public static class BindUserDTO implements Serializable {
        /**
         * 统⼀设备管理平台中设备通道唯⼀标识
         */
        private String guid;
        /**
         * 用户手机号码
         */
        private String phone;
    }
}
