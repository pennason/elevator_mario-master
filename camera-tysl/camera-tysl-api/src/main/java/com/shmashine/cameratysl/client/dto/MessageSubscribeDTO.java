// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.client.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/22 17:51
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "消息订阅请求参数MessageSubscribeRequestDTO", description = "消息订阅请求参数")
public class MessageSubscribeDTO {
    /**
     * 订阅消息类型 statusChange: 设备状态变更（⽬前⽀持在线、离线、新增、删除、未知故障的变更）; heartbeat: 平台⼼跳推送; zxSuperPlatformStatus: 中兴上级平台状态变更; zxSubPlatformStatus:中兴下级平台状态变更
     */
    @Schema(title = "订阅消息类型",
            description = "订阅消息类型 statusChange: 设备状态变更（⽬前⽀持在线、离线、新增、删除、未知故障的变更）; heartbeat: 平台⼼跳推送; zxSuperPlatformStatus: 中兴上级平台状态变更; zxSubPlatformStatus:中兴下级平台状态变更",
            example = "statusChange", required = true)
    private String eventType;
}

