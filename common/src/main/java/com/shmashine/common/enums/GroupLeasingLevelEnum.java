// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/3/10 11:44
 * @since v1.0
 */

@AllArgsConstructor
public enum GroupLeasingLevelEnum {
    // 群租问题 群租等级
    NORMAL(0, 100, "不可能"),
    UNLIKELINESS(1, 150, "不大可能"),
    SUSPICIOUS(2, 200, "可疑"),
    VERY_SUSPICIOUS(3, Integer.MAX_VALUE, "很可疑");

    @Getter
    private final Integer level;
    @Getter
    private final Integer percent;
    @Getter
    private final String description;
}
