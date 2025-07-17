// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/11/22 15:03
 * @since v1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TyslVoiceTokenResponseDTO extends TyslResponseDTO<TyslVoiceTokenResponseDTO.StreamTokenDTO> {

    /**
     * token信息
     */
    @Data
    public static class StreamTokenDTO implements Serializable {
        /**
         * 对讲鉴权token
         */
        private String streamToken;
    }
}
