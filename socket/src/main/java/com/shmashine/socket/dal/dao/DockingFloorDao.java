package com.shmashine.socket.dal.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.socket.dal.dto.PeopleFlowStatisticsDO;

/**
 * 楼层停靠|人流量信息查询接口
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/12/11 15:53
 * @Since: 1.0.0
 */
@Mapper
public interface DockingFloorDao {

    /**
     * 获取楼层停靠人流量统计
     *
     * @param elevatorCode 电梯编号
     * @param startDate    开始日期
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @return 人流量统计数据
     */
    List<PeopleFlowStatisticsDO> peopleFlowStatistics(@Param("elevatorCode") String elevatorCode,
                                                      @Param("startDate") Date startDate,
                                                      @Param("startTime") String startTime,
                                                      @Param("endTime") String endTime);

}
