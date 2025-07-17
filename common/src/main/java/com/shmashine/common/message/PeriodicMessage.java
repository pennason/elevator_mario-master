// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.message;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/6/20 11:22
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PeriodicMessage implements Serializable {

    /**
     * 电梯code
     */
    private String elevatorCode;

    /**
     * 设备类型 Cube：轿顶（初版设备），CarRoof：轿顶，MotorRoom：机房，FRONT：前装设备
     */
    private String sensorType;


    /**
     * 上报时间 yyyy-MM-dd HH:mm:ss
     */
    private String time;
}
