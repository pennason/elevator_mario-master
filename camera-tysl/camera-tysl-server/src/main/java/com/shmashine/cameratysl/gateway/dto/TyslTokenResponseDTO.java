// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 11:12
 * @since v1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TyslTokenResponseDTO extends TyslResponseDTO<TyslTokenResponseDTO.TyslTokenDTO> {

    /**
     * token
     */
    @Data
    public static class TyslTokenDTO implements Serializable {
        private String accessToken;
        private Long expiresTime;
        private String memo;
    }
}
