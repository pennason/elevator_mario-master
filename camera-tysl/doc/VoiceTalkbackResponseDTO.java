// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/11/21 15:58
 * @since v1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class VoiceTalkbackResponseDTO extends TyslResponseDTO<VoiceTalkbackResponseDTO.TalkbackInfo> {

    @Data
    public static class TalkbackInfo implements Serializable {
        /**
         * socket 地址
         */
        private String wss;
        /**
         * token
         */
        private String token;
        /**
         * sessionId
         */
        private String sessionId;
    }
}
