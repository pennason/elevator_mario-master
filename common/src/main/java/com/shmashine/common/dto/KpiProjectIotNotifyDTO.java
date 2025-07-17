// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 项目监控KPI发送通知支持的类型
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/12 15:53
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KpiProjectIotNotifyDTO implements Serializable {

    /**
     * 项目id
     */
    private String projectId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 电梯总数
     */
    private Integer elevatorTotal;
    /**
     * 电梯当日实时离线数
     */
    private Integer elevatorOfflineRealtime;
    /**
     * 电梯当日实时故障数
     */
    private Integer elevatorFaultRealtime;
    /**
     * 摄像头总数
     */
    private Integer cameraTotal;
    /**
     * 摄像头当日实时离线数
     */
    private Integer cameraOfflineRealtime;

    // 昨日信息

    /**
     * 电梯昨日最大离线数
     */
    private Integer yesterdayElevatorOfflineMax;
    /**
     * 电梯昨日最大故障数
     */
    private Integer yesterdayElevatorFaultMax;
    /**
     * 摄像头昨日最大离线数
     */
    private Integer yesterdayCameraOfflineMax;

    // 符合条件的通知信息


    /**
     * 设备在线率通知
     */
    private String elevatorOnlineRateNotify;
    /**
     * 设备在线比上日低x个通知
     */
    private String elevatorOnlineLowerNotify;
    /**
     * 设备故障率通知
     */
    private String elevatorFaultRateNotify;
    /**
     * 设备故障比上日高x个通知
     */
    private String elevatorFaultHigherNotify;
    /**
     * 摄像头在线率通知
     */
    private String cameraOnlineRateNotify;
    /**
     * 摄像头在线比上日低x个通知
     */
    private String cameraOnlineLowerNotify;
}
