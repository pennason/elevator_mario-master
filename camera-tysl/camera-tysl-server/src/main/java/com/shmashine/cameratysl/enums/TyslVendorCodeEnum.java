// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 17:26
 * @since v1.0
 */

@Getter
@AllArgsConstructor
public enum TyslVendorCodeEnum {
    // 设备所属汇聚平台
    QQY("QQY", "⼤华"),
    TYBD("TYBD", "中兴"),
    TYYY("TYYY", "天翼云眼");

    private final String code;
    private final String name;

    public static TyslVendorCodeEnum getByCode(String code) {
        for (var value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
