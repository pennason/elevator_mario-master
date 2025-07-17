package com.shmashine.api.controller.wuye.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  jiangheng
 * @version 2023/10/12 10:52
 * @description: 物业小程序动态菜单返回数据
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WuyeMenuListRespVO {

    /**
     * 电梯状态统计
     */
    private ElevatorStatusCount elevatorStatusCount;

    /**
     * 历史记录查询
     */
    private HistoricalRecordQuery historicalRecordQuery;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class ElevatorStatusCount {

        /**
         * 电梯数量
         */
        @lombok.Builder.Default
        private boolean elevatorNum = true;

        /**
         * 本月困人
         */
        @lombok.Builder.Default
        private boolean peopleTrapped = true;

        /**
         * 维保逾期
         */
        @lombok.Builder.Default
        private boolean maintenanceOverdue = false;

        /**
         * 当日急修
         */
        @lombok.Builder.Default
        private boolean emergencyRepair = false;

        /**
         * 当日隐患
         */
        @lombok.Builder.Default
        private boolean fault = true;

        /**
         * 电梯年检
         */
        @lombok.Builder.Default
        private boolean annualInspection = false;


    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class HistoricalRecordQuery {

        /**
         * 困人记录
         */
        @lombok.Builder.Default
        private boolean peopleTrapped = true;

        /**
         * 维保记录
         */
        @lombok.Builder.Default
        private boolean maintenanceOverdue = false;

        /**
         * 急修记录
         */
        @lombok.Builder.Default
        private boolean emergencyRepair = false;

    }

}