// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 默认说明
 *
 * @param <T> 比较数据
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/26 18:27
 * @since v1.0
 */

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class KpiProjectBaseStatisticsDTO<T> implements Serializable {

    /**
     * 项目ID
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
     * 摄像头总数
     */
    private Integer cameraTotal;
    /**
     * 实时离线电梯
     */
    private Integer elevatorOfflineRealtime;
    /**
     * 实时故障电梯
     */
    private Integer elevatorFaultRealtime;
    /**
     * 实时离线摄像头
     */
    private Integer cameraOfflineRealtime;
    /**
     * 是否为异常项目 true:异常项目 false:正常项目， 达到告警阈值则为异常
     */
    private Boolean abnormalStatus;

    /**
     * 今日与昨日
     */
    private T dayCompare;
    /**
     * 本周与上周
     */
    private T weekCompare;
    /**
     * 本月与上月
     */
    private T monthCompare;
}
