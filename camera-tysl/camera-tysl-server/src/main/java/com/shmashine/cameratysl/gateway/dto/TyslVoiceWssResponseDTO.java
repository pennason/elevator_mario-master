// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/11/22 15:48
 * @since v1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TyslVoiceWssResponseDTO extends TyslResponseDTO<TyslVoiceWssResponseDTO.WssInfo> {

    /**
     * WssInfo
     */
    @Data
    public static class WssInfo implements Serializable {
        /**
         * 语⾳汇聚⽹关websocket地址
         */
        private String wss;
        /**
         * 鉴权信息20秒失效
         */
        private String token;
        /**
         * 对讲会话ID。调⽤⽅在整个对讲⽣命周期，使⽤此会话ID进⾏对讲操作。每⼀个sessionId都是唯⼀的
         */
        private String sessionId;
        /**
         * 设备厂商
         */
        private String vendorCode;

        /**
         * 设备编码, 未后期补充，非原数据返回的
         */
        private String deviceCode;
    }
}
