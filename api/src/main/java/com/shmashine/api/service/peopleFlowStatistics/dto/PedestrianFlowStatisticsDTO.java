package com.shmashine.api.service.peopleFlowStatistics.dto;

import lombok.Data;

/**
 * @author  jiangheng
 * @version 2024/1/16 13:46
 * @description: 人流量上下行统计查询结果
 */
@Data
public class PedestrianFlowStatisticsDTO {

    private String groupData;

    private Integer groupBy;

    private Integer throughput;

}
