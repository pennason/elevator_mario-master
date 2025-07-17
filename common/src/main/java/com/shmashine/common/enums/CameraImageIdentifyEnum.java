// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/10/13 16:26
 * @since v1.0
 */

@AllArgsConstructor
public enum CameraImageIdentifyEnum {
    // 图像识别类型
    IMAGE_IDENTIFY_ELECTRIC_BIKE_IDENTIFY(1, "electricBike", "自研电动车识别"),
    IMAGE_IDENTIFY_ELEVATOR_PERSON_IDENTIFY(2, "elevatorPerson", "自研乘梯人员（数量，年龄等）"),
    IMAGE_IDENTIFY_POSTURE_IDENTIFY(3, "posture", "自研姿态识别"),

    IMAGE_LABEL_ELECTRIC_BIKE(37, "markElectricBike", "标注助动车类型");

    @Getter
    private final Integer code;
    @Getter
    private final String slug;
    @Getter
    private final String desc;

    public static CameraImageIdentifyEnum getByCode(Integer code) {
        for (var item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
