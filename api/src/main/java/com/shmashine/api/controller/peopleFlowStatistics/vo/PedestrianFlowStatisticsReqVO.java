package com.shmashine.api.controller.peopleFlowStatistics.vo;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author  jiangheng
 * @version 2024/1/16 11:25
 * @description: 电梯上下行人流量统计请求实体
 */
@Data
@Valid
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PedestrianFlowStatisticsReqVO extends PeopleFlowStatisticsBaseVO {

    /**
     * 电梯编号
     */
    @NotBlank(message = "电梯编号不能为空")
    private String elevatorCode;

    /**
     * 查询开始时间
     */
    private Date selectStartTime;

    /**
     * 查询结束时间
     */
    private Date selectEndTime;

}
