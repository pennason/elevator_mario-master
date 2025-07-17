// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/23 15:33
 * @since v1.0
 */

@Getter
@AllArgsConstructor
public enum TyslDeviceTypeTyyyEnum {
    // 天翼云眼 deviceType 枚举
    YY_DEVICE(1, "云眼设备"),
    LQKJ_DEVICE(3, "拉取看家设备"),
    YY_NVR(4, "云眼NVR"),
    BIND_KJ_DEVICE(5, "绑定看家设备"),
    LQGB_DEVICE(6, "拉取国标设备"),
    BIND_GB_DEVICE(7, "绑定国标设备"),
    JLGB_DEVICE(8, "级联国标设备"),
    XYGJ_DEVICE_A(9, "⼩翼管家设备A"),
    XYGJ_DEVICE(10, "⼩翼管家设备"),
    SB_DEVICE(11, "社标设备"),
    PHONE_CAMERA(12, "⼿机摄像头");

    private final Integer code;
    private final String name;
}
