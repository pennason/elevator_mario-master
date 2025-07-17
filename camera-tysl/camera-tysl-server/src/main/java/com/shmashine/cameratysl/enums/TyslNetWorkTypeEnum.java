// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/22 9:20
 * @since v1.0
 */

@Getter
@AllArgsConstructor
public enum TyslNetWorkTypeEnum {
    // 网络类型
    PUBLIC_NETWORK(1, "公网"),
    B_PLANE(2, "B平面"),
    ENI(3, "ENI"),
    SPECIAL_NETWORK(4, "专网（其他）");

    private final Integer code;
    private final String name;
}
