// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/13 16:07
 * @since v1.0
 */

@AllArgsConstructor
public enum GroupLeasingResultEnum {
    // 群租问题 状态枚举
    DEFAULT(0, "未确认，默认"),
    TAKING_EVIDENCE(1, "取证中"),
    CONFIRMED(2, "已确认");

    @Getter
    private final Integer status;
    @Getter
    private final String description;

}
