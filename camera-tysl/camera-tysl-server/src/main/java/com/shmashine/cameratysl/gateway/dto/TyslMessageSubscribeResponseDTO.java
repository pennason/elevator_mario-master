// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 15:56
 * @since v1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TyslMessageSubscribeResponseDTO extends TyslResponseDTO<TyslMessageSubscribeResponseDTO.SubscribeDTO> {

    /**
     * 订阅信息
     */
    @Data
    public static class SubscribeDTO {
        /**
         * 订阅id
         */
        private String subscriptionId;
    }
}
