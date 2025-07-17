// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.dto.requests;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 15:54
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageSubscribeRequestDTO implements Serializable {
    /**
     * 消息类型。应⽤可通过消息类型接收平台推送的对应通知消息（具体请参考数据字典）。
     * deviceEvent 设备事件通知
     * heartbeatEvent ⼼跳存活通知
     * zxSuperPlatformStatus 中兴上级平台状态变更
     * zxSubPlatformStatus 中兴下级平台状态变更
     */
    private String eventType;
    /**
     * 订阅的回调地址，⽤于接收对应类型的通知消息。
     */
    private String notifyUrl;
    /**
     * 1：公⽹；2：B平⾯；3：ENI；4：专⽹（其他）
     */
    private Integer networkType;
}
