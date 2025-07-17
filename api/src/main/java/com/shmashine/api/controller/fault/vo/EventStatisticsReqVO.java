package com.shmashine.api.controller.fault.vo;

import com.shmashine.api.module.fault.input.FaultStatisticsModule;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 上下线统计查询参数
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/6/24 16:41
 * @Since: 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EventStatisticsReqVO extends FaultStatisticsModule {

    /**
     * 数据分组类型 i_type：上下线类型 v_reason：上下线原因
     */
    private String groupBy = "i_type";

}
