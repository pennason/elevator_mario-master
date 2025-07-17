package com.shmashine.api.entity;

import com.shmashine.common.entity.TblElevator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author  jiangheng
 * @version 2022/11/24 15:13
 * @description: 电梯配置
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TblElevatorConf extends TblElevator {

    /**
     * 最大乘客数
     */
    private Integer largestPassengers;

    /**
     * 负载系数
     */
    private Double loadFactor;

    /**
     * 往返次数
     */
    private Long backAndForthCount;
}
