// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 15:03
 * @since v1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TyslDeviceStreamUrlResponseDTO extends TyslResponseDTO<TyslDeviceStreamUrlResponseDTO.DeviceStreamUrlDTO> {

    /**
     * URL
     */
    @Data
    public static class DeviceStreamUrlDTO implements Serializable {
        /**
         * 视频流url
         */
        private String url;
    }
}
