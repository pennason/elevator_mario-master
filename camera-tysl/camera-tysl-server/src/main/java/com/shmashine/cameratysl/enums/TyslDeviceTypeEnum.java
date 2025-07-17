// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 17:17
 * @since v1.0
 */

@Getter
@AllArgsConstructor
public enum TyslDeviceTypeEnum {
    // 设备接入类型
    OTHER(0, "其他"),
    DVR(1, "DVR"),
    IPC(2, "IPC"),
    NVR(3, "NVR");

    private final Integer code;
    private final String name;

    public static TyslDeviceTypeEnum getByCode(Integer code) {
        for (var value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
