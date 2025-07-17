// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 17:08
 * @since v1.0
 */

@Getter
@AllArgsConstructor
public enum TyslRotocolEnum {
    // 电信视联 平台协议枚举
    OTHER(0, "其他"),
    GB28181(1, "GB28181"),
    ONVIF(2, "ONVIF");

    private final Integer code;
    private final String name;

    public static TyslRotocolEnum getByCode(Integer code) {
        for (var value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
