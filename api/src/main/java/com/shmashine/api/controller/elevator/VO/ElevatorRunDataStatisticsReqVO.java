package com.shmashine.api.controller.elevator.VO;

import com.shmashine.api.module.fault.input.FaultStatisticsModule;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 运行数据统计请求体
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/8/5 17:26
 * @Since: 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ElevatorRunDataStatisticsReqVO extends FaultStatisticsModule {

    /**
     * 数据统计维度
     * 1：按时间展示（默认值）
     * 2：按电梯降序展示
     */
    private Integer dataDimension = 1;

}
