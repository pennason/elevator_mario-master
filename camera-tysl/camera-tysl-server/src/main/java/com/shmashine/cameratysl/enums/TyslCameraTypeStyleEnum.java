// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 17:13
 * @since v1.0
 */

@Getter
@AllArgsConstructor
public enum TyslCameraTypeStyleEnum {
    // 摄像头类型枚举
    OTHER(0, "其他"),
    GUN(1, "枪机"),
    DOME(2, "球机"),
    HEMISPHERE(3, "半球");

    private final Integer code;
    private final String name;

    public static TyslCameraTypeStyleEnum getByCode(Integer code) {
        for (var value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
