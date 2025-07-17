package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.module.ruijin.FaultStatisticalQuantitySearchModule;

public interface BizBigScreenDao {

    /**
     * 大屏电梯数量接口 公用图表统计 通用
     */
    List<Map> countElevatorClassificationInfoV1(@Param("faultStatisticalQuantitySearchModule") FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 不文明行为统计（每种不文明行为次数占比）通用
     */
    List<Map> uncivilizedBehaviorV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);


    /**
     * 每种不文明行为前三电梯占比
     */
    List<Map> uncivilizedBehaviorByTopThreeV1(@Param("faultStatisticalQuantitySearchModule") FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule, @Param("faultType") String faultType);

    /**
     * 每种不文明行为前三电梯占比 运行总数
     */
    List<Map> uncivilizedBehaviorByTopThreeV1RunCount(@Param("faultStatisticalQuantitySearchModule") FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 智能监管
     */
    List<Map> intelligentSupervisionV1(@Param("module") FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);


    /**
     * 本年度不文明行为走势 通用
     */
    List<Map> trendOfUncivilizedBehaviorInThisYearV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 前一月不文明行为走势 通用
     */
    List<Map> trendOfUncivilizedBehaviorInThePreviousMonthV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 前一周不文明行为走势 通用
     */
    List<Map> trendOfUncivilizedBehaviorInThePreviousWeekV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);


    /***
     * 获取电梯每个状态的数量
     * @param userId
     * @param isAdminFlag
     * @return
     */
    List<Map> countElevatorV1(@Param("villageId") String villageId, @Param("userId") String userId, @Param("isAdminFlag") boolean isAdminFlag);


    /**
     * 困人工单历史
     */
    List<Map> searchUncivilizedBehavior();

    /**
     * 困人来源排行统计
     */
    List<Map> statisticsOnTheSourceOfPovertyV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 故障统计 困人统计 不文明行为统计
     */
    List<Map> faultStatisticalQuantityV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 故障占比
     */
    List<Map> statisticsFaultPovertyV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 电梯信息
     */
    Map getElevatorInfoV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 统计故障次数信息——儿童医院项目统计平台故障
     */
    List<Map> faultStatisticalQuantityByMX(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);
}
