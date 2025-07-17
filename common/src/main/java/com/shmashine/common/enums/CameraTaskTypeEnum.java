// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.enums;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/9 11:14
 * @since v1.0
 */

@AllArgsConstructor
public enum CameraTaskTypeEnum {
    // 摄像头图像视频下载任务类型
    FAULT(1, "fault", "告警"),
    NIGHT_WATCH(2, "night-watch", "夜间守护"),
    GROUP_LEASING(3, "group-leasing", "群租"),
    EMERGENCY_REPAIR_FIRST_AID(4, "emergency-repair-first-aid", "急修急救"),
    ELECTRIC_BIKE_IDENTIFY(5, "electric-bike-identify", "电动车识别"),
    PEOPLE_FLOW_STATISTICS(6, "people_flow_statistics", "人流量统计"),
    OTHER(0, "other", "未指定");

    @Getter
    private final Integer code;

    @Getter
    private final String path;

    @Getter
    private final String description;

    public static CameraTaskTypeEnum getByCode(Integer code) {
        for (var item : values()) {
            if (Objects.equals(item.getCode(), code)) {
                return item;
            }
        }
        return OTHER;
    }

    public static String getPathByCode(Integer code) {
        for (var item : values()) {
            if (Objects.equals(item.getCode(), code)) {
                return item.getPath();
            }
        }
        throw new RuntimeException("请在com.shmashine.common.enums.CameraTaskTypeEnum 中定义相关任务类型");
    }
}
