// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 17:29
 * @since v1.0
 */

@Getter
@AllArgsConstructor
public enum TyslOnlineStatusEnum {
    // 设备是否在线
    OFFLINE(0, "离线"),
    ONLINE(1, "在线");

    private final Integer code;
    private final String name;

    public static TyslOnlineStatusEnum getByCode(Integer code) {
        for (var value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
