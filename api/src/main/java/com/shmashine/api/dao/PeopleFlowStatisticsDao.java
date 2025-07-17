package com.shmashine.api.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.api.controller.peopleFlowStatistics.vo.PedestrianFlowStatisticsByVillageReqVO;
import com.shmashine.api.controller.peopleFlowStatistics.vo.PedestrianFlowStatisticsReqVO;
import com.shmashine.api.service.peopleFlowStatistics.dto.PedestrianFlowStatisticsDTO;
import com.shmashine.common.message.PeopleFlowStatisticsMessage;

/**
 * @author  jiangheng
 * @version 2024/1/8 17:13
 * @description: 人流量统计
 */
@Mapper
public interface PeopleFlowStatisticsDao {

    /**
     * 新增人流量统计记录
     *
     * @param peopleFlowStatisticsMessage
     */
    void insert(PeopleFlowStatisticsMessage peopleFlowStatisticsMessage);

    /**
     * 根据id查询记录
     *
     * @param id
     * @return
     */
    PeopleFlowStatisticsMessage getById(String id);

    /**
     * 获取上一条记录
     *
     * @param peopleFlowStatistics
     * @return
     */
    PeopleFlowStatisticsMessage getPreviousRecord(PeopleFlowStatisticsMessage peopleFlowStatistics);

    /**
     * 更新记录
     *
     * @param peopleFlowStatistics
     * @return
     */
    Integer updateById(PeopleFlowStatisticsMessage peopleFlowStatistics);

    /**
     * 获取上下行人流量统计-时间段分组
     *
     * @param reqVO
     * @return
     */
    List<PedestrianFlowStatisticsDTO> pedestrianFlowStatistics(PedestrianFlowStatisticsReqVO reqVO);


    /**
     * 获取当前时间段记录
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    List<PeopleFlowStatisticsMessage> getListByTriggerTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 获取人流量统计-楼层分组
     *
     * @param reqVO
     * @return
     */
    List<PedestrianFlowStatisticsDTO> pedestrianFlowStatisticsGroupByFloor(@Param("reqVO") PedestrianFlowStatisticsReqVO reqVO, @Param("groupBy") Integer groupBy);

    /**
     * 获取小区电梯人流量统计 时间段分组
     *
     * @param reqVO
     * @return
     */
    List<PedestrianFlowStatisticsDTO> pedestrianFlowStatisticsByVillage(PedestrianFlowStatisticsByVillageReqVO reqVO);
}
