package com.shmashine.api.service.bigScreen;

import java.util.List;
import java.util.Map;

import com.shmashine.api.module.ruijin.FaultStatisticalQuantitySearchModule;

public interface BigScreenService {

    /**
     * 获取电梯数量
     */
    Map countElevatorV1(String villageId, String userId, boolean isAdminFlag);

    /**
     * 电梯分类数量列表 通用
     */
    List<Map> elevatorClassificationQuantityListV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 不文明种类次数
     */
    List<Map> frequencyOfUncivilizedSpeciesV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 不文明行为前三电梯排行
     */
    List<Map> frequencyOfUncivilizedSpeciesTop3V1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 不文明行为走势图 通用
     */
    Map<String, Object> trendOfUncivilizedBehaviorV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 智能监管
     */
    List<Map> intelligentSupervisionV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 困人来源排行
     */
    List<Map> rankingOfPoorPeopleV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 统计故障次数信息
     */
    List<Map> statisticsOfFailureTimesV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 电梯故障排名
     */
    List<Map> elevatorFailureRankingV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 获取电梯基本信息
     */
    Map getElevatorInfoV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);
}
