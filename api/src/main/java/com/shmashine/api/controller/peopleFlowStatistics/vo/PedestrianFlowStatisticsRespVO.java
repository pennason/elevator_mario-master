package com.shmashine.api.controller.peopleFlowStatistics.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  jiangheng
 * @version 2024/1/16 11:26
 * @description: 电梯上下行人流量统计响应实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedestrianFlowStatisticsRespVO {

    /**
     * x轴
     */
    private XAxis xAxis;
    /**
     * y轴数据
     */
    private Series series;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class XAxis {
        private String type;
        private List<String> data;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Series {
        private List<Rest> restList;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Rest {
        private String type;
        private List<Integer> data;
    }
}
