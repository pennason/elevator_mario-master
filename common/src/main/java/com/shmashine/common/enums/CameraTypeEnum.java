// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.enums;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/9 11:26
 * @since v1.0
 */

@AllArgsConstructor
public enum CameraTypeEnum {
    // 摄像头类型枚举
    HIK_EZVIZ(1, "hikEzviz", "海康萤石"),
    XIONG_MAI(2, "xiongMai", "雄迈"),
    HIK_CLOUD(4, "hikCloud", "海康云眸"),
    TYYY(5, "TYYY", "天翼云眼"),
    TYBD(6, "TYBD", "中兴"),
    OTHER(0, "other", "未知");

    @Getter
    private final Integer code;

    @Getter
    private final String slug;

    @Getter
    private final String name;

    public static CameraTypeEnum getByCode(Integer code) {
        for (var item : values()) {
            if (Objects.equals(item.getCode(), code)) {
                return item;
            }
        }
        return OTHER;
    }

    public static CameraTypeEnum getBySlug(String slug) {
        for (var item : values()) {
            if (Objects.equals(item.getSlug(), slug)) {
                return item;
            }
        }
        return null;
    }

}
