// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/26 18:27
 * @since v1.0
 */

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class KpiProjectStatisticsDTO implements Serializable {

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
     * 是否为异常项目 true:异常项目 false:正常项目， 达到告警阈值则为异常
     */
    private Boolean abnormalStatus;

    /**
     * 离线电梯
     */
    private Statistics elevatorOfflineStatistics;
    /**
     * 故障电梯
     */
    private Statistics elevatorFaultStatistics;
    /**
     * 离线摄像头
     */
    private Statistics cameraOfflineStatistics;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Statistics {
        /**
         * 实时数量
         */
        private Integer realtime;
        /**
         * 当日最大数量
         */
        private Integer todayMax;
        /**
         * 昨日最大数量
         */
        private Integer yesterdayMax;
        /**
         * 本周平均
         */
        private Integer weekAvg;
        /**
         * 上周平均
         */
        private Integer lastWeekAvg;
        /**
         * 本月平均
         */
        private Integer monthAvg;
        /**
         * 上月平均
         */
        private Integer lastMonthAvg;
    }
}
