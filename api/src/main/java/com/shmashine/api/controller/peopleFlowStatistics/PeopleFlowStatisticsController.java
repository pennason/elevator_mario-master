package com.shmashine.api.controller.peopleFlowStatistics;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.controller.peopleFlowStatistics.vo.PedestrianFlowStatisticsByVillageReqVO;
import com.shmashine.api.controller.peopleFlowStatistics.vo.PedestrianFlowStatisticsReqVO;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.service.peopleFlowStatistics.PeopleFlowStatisticsService;

/**
 * @author  jiangheng
 * @version 2024/1/16 11:18
 * @description: 人流量统计
 */
@RestController
@RequestMapping("/peopleFlowStatisticsController")
public class PeopleFlowStatisticsController {

    private final PeopleFlowStatisticsService peopleFlowStatisticsService;

    @Autowired
    public PeopleFlowStatisticsController(PeopleFlowStatisticsService peopleFlowStatisticsService) {
        this.peopleFlowStatisticsService = peopleFlowStatisticsService;
    }

    /**
     * 电梯上下行人流量统计 时间段分组
     *
     * @return
     */
    @PostMapping("pedestrianFlowStatistics")
    public ResponseResult pedestrianFlowStatistics(@RequestBody @Valid PedestrianFlowStatisticsReqVO reqVO) {
        return ResponseResult.successObj(peopleFlowStatisticsService.pedestrianFlowStatistics(reqVO));
    }

    /**
     * 获取小区电梯上下行人流量统计 时间段分组
     *
     * @return
     */
    @PostMapping("pedestrianFlowStatisticsByVillage")
    public ResponseResult pedestrianFlowStatisticsByVillage(@RequestBody @Valid PedestrianFlowStatisticsByVillageReqVO reqVO) {
        return ResponseResult.successObj(peopleFlowStatisticsService.pedestrianFlowStatisticsByVillage(reqVO));
    }

    /**
     * 电梯进出人流量统计 楼层分组
     *
     * @return
     */
    @PostMapping("pedestrianFlowStatisticsGroupByFloor")
    public ResponseResult pedestrianFlowStatisticsGroupByFloor(@RequestBody @Valid PedestrianFlowStatisticsReqVO reqVO) {
        return ResponseResult.successObj(peopleFlowStatisticsService.pedestrianFlowStatisticsGroupByFloor(reqVO));
    }

}
