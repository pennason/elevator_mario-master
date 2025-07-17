package com.shmashine.api.entity;

import lombok.Data;

/**
 * 电梯上下线统计查询结果
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/6/24 15:45
 * @Since: 1.0.0
 */
@Data
public class ElevatorEventStatistics {

    /**
     * 电梯编号
     */
    private String elevatorCode;

    /**
     * 统计数量
     */
    private Integer eventNumCount;

    /**
     * 上下线类型或原因
     */
    private String type;

    /**
     * 发生时间
     */
    private String happenTime;

}
