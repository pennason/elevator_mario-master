// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 17:31
 * @since v1.0
 */

@Getter
@AllArgsConstructor
public enum TyslStreamTypeEnum {
    // 码流类型
    MAIN(0, "主码流"),
    SUB(1, "子码流"),
    THIRD(2, "第三码流");

    private final Integer code;
    private final String name;

    public static TyslStreamTypeEnum getByCode(Integer code) {
        for (var value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
