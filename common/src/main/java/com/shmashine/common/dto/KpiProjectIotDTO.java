// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 按项目获取相关KPI数据
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/11 18:07
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(name = "KpiProjectIotDTO", description = "按项目获取相关KPI数据")
public class KpiProjectIotDTO implements Serializable {

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 日期 yyyy-MM-dd
     */
    private String day;
    /**
     * 电梯总数
     */
    private Integer elevatorTotal;
    /**
     * 电梯当日最大离线数
     */
    private Integer elevatorOfflineMax;
    /**
     * 电梯平均离线数
     */
    private Integer elevatorOfflineAvg;
    /**
     * 电梯当日最大故障数
     */
    private Integer elevatorFaultMax;
    /**
     * 电梯平均故障数
     */
    private Integer elevatorFaultAvg;
    /**
     * 摄像头总数
     */
    private Integer cameraTotal;
    /**
     * 摄像头当日最大离线数
     */
    private Integer cameraOfflineMax;
    /**
     * 摄像头平均离线数
     */
    private Integer cameraOfflineAvg;
}
