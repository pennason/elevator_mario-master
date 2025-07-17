// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.enums;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/18 15:43
 * @since v1.0
 */

@AllArgsConstructor
public enum WecomNotifyTypeEnum {
    // 电梯监控通知类型
    ELEVATOR_ONLINE_RATE("ElevatorOnlineRate", "电梯在线率低于阈值"),
    ELEVATOR_FAULT_RATE("ElevatorFaultRate", "电梯故障率高于阈值"),
    CAMERA_ONLINE_RATE("CameraOnlineRate", "摄像头在线率低于阈值"),
    ELEVATOR_ONLINE_LOWER("ElevatorOnlineLower", "电梯在线数下降"),
    ELEVATOR_FAULT_HIGHER("ElevatorFaultHigher", "电梯故障数上升"),
    CAMERA_ONLINE_LOWER("CameraOnlineLower", "摄像头在线数下降"),

    // 北向推送通知类型
    NORTH_PUSH_ONLINE_RATE("NorthPushOnlineRate", "北向推送在线率低于阈值"),
    NORTH_PUSH_ONLINE_LOWER("NorthPushOnlineLower", "北向推送在线数下降");

    @Getter
    private final String slug;
    @Getter
    private final String description;

    public static String getDescriptionBySlug(String slug) {
        for (var item : values()) {
            if (Objects.equals(item.getSlug(), slug)) {
                return item.getDescription();
            }
        }
        throw new RuntimeException("请在com.shmashine.common.enums.WecomNotifyTypeEnum 中定义相关任务类型");
    }
}
