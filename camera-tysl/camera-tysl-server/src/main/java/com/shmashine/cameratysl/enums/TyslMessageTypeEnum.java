// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 17:23
 * @since v1.0
 */

@Getter
@AllArgsConstructor
public enum TyslMessageTypeEnum {
    // 消息类型
    DEVICE_EVENT("deviceEvent", "设备事件通知"),
    HEARTBEAT_EVENT("heartbeatEvent", "⼼跳存活通知"),
    ZX_SUPER_PLATFORM_STATUS("zxSuperPlatformStatus", "中兴上级平台状态变更"),
    ZX_SUB_PLATFORM_STATUS("zxSubPlatformStatus", "中兴下级平台状态变更");

    private final String code;
    private final String name;

    public static TyslMessageTypeEnum getByCode(String code) {
        for (var value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
