// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 建议使用 KpiProjectStatisticsDTO
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/29 10:03
 * @since v1.0
 */

@Deprecated
@Data
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(name = "KpiProjectNorthPushStatisticsDTO", title = "北向推送KPI统计")
public class KpiProjectNorthPushStatisticsDTO
        extends KpiProjectBaseStatisticsDTO<KpiProjectNorthPushStatisticsDTO.NorthPushStatistics> {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class NorthPushStatistics {
        /**
         * 电梯最大离线数
         */
        private Integer elevatorOfflineLastTime;
        /**
         * 电梯实时离线数
         */
        private Integer elevatorOfflineRealtime;
    }
}
