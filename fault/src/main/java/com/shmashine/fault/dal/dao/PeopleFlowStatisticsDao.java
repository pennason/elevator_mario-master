package com.shmashine.fault.dal.dao;

import org.apache.ibatis.annotations.Mapper;

import com.shmashine.common.message.PeopleFlowStatisticsMessage;

/**
 * 人流量统计DO
 *
 * @author jiangheng
 * @version V1.0.0 - 2024/1/8 17:13
 */
@Mapper
public interface PeopleFlowStatisticsDao {

    /**
     * 新增人流量统计记录
     *
     * @param peopleFlowStatisticsMessage 人流量统计消息
     */
    void insert(PeopleFlowStatisticsMessage peopleFlowStatisticsMessage);

}
