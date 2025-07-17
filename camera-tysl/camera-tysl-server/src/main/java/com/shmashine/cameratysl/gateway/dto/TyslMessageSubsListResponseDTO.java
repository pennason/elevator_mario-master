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
 * @version v1.0  -  2023/8/21 16:01
 * @since v1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TyslMessageSubsListResponseDTO extends TyslResponseDTO<TyslMessageSubsListResponseDTO.SubScribeListDTO> {
    /**
     * 订阅列表
     */
    @Data
    public static class SubScribeListDTO implements Serializable {
        /**
         * 订阅列表
         */
        private List<SubscribeItem> list;
    }

    /**
     * 订阅项
     */
    @Data
    public static class SubscribeItem implements Serializable {
        /**
         * 订阅id ⽤于唯⼀标识⼀个订阅，在创建订阅时平台分配获得
         */
        private String subscrptionId;
        /**
         * 消息类型
         */
        private String eventType;
        /**
         * 回调地址
         */
        private String notifyUrl;
    }
}
