package com.shmashine.socket.dal.dto;

import java.util.Date;

import lombok.Data;

/**
 * 人流量统计DO
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/12/11 15:56
 * @Since: 1.0.0
 */
@Data
public class PeopleFlowStatisticsDO {

    /**
     * 统计日期
     */
    private Date triggerDate;

    /**
     * 楼层
     */
    private Integer floor;

    /**
     * 轿厢运行方向 0：折返，1：上行，-1：下行
     */
    private Integer direction;

    /**
     * 人流量(吞吐量)
     */
    private Integer throughput;
}
