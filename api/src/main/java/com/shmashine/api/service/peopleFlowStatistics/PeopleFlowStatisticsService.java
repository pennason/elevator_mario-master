package com.shmashine.api.service.peopleFlowStatistics;

import com.shmashine.api.controller.peopleFlowStatistics.vo.PedestrianFlowStatisticsByVillageReqVO;
import com.shmashine.api.controller.peopleFlowStatistics.vo.PedestrianFlowStatisticsReqVO;
import com.shmashine.api.controller.peopleFlowStatistics.vo.PedestrianFlowStatisticsRespVO;

/**
 * @author  jiangheng
 * @version 2024/1/16 11:29
 * @description: com.shmashine.api.service.peopleFlowStatistics
 */
public interface PeopleFlowStatisticsService {

    /**
     * 电梯上下行人流量统计 时间段分组
     *
     * @param reqVO
     * @return
     */
    PedestrianFlowStatisticsRespVO pedestrianFlowStatistics(PedestrianFlowStatisticsReqVO reqVO);

    /**
     * 电梯进出人流量统计 楼层分组
     *
     * @param reqVO
     * @return
     */
    PedestrianFlowStatisticsRespVO pedestrianFlowStatisticsGroupByFloor(PedestrianFlowStatisticsReqVO reqVO);

    /**
     * 获取小区电梯进出人流量统计 时间段分组
     *
     * @param reqVO
     * @return
     */
    PedestrianFlowStatisticsRespVO pedestrianFlowStatisticsByVillage(PedestrianFlowStatisticsByVillageReqVO reqVO);
}
