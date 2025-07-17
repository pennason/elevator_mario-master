// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 17:19
 * @since v1.0
 */

@Getter
@AllArgsConstructor
public enum TyslEventTypeEnum {
    // 事件类型枚举
    STATUS_CHANGE("statusChange", "设备状态变更（⽬前⽀持在线、离线、新增、删除、未知故障的变更）"),
    HEARTBEAT("heartbeat", "平台⼼跳推送"),
    ZX_SUPER_PLATFORM_STATUS("zxSuperPlatformStatus", "中兴上级平台状态变更"),
    ZX_SUB_PLATFORM_STATUS("zxSubPlatformStatus", "中兴下级平台状态变更");

    private final String code;
    private final String name;

    public static TyslEventTypeEnum getByCode(String code) {
        for (var value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
