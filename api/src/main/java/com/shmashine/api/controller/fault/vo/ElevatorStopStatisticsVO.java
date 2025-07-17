package com.shmashine.api.controller.fault.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取电梯 停梯率 困人率 故障率
 *
 * @author jiangheng
 * @version v1.0.0 - 2024/3/25 16:21
 * @since v1.0.0
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ElevatorStopStatisticsVO {

    /**
     * 电梯编号
     */
    private String elevatorCode;

    /**
     * 停梯时间 ms
     */
    private Long stoppingTime;

    /**
     * 运行时间 ms
     */
    private Long runningTime;

    /**
     * 运行次数
     */
    private Integer runningNum;

    /**
     * 困人次数
     */
    private Long peopleTrappedNum;

    /**
     * 故障次数
     */
    private Long faultNum;
}
