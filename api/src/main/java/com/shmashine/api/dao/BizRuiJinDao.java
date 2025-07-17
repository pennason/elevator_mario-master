package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.module.elevator.SearchElevatorModule;
import com.shmashine.api.module.fault.input.SearchFaultModule;
import com.shmashine.api.module.fault.output.QueryFaultExportModule;
import com.shmashine.api.module.fault.output.QueryMaintenanceExportModule;
import com.shmashine.api.module.ruijin.EventDownloadModuleMap;
import com.shmashine.api.module.ruijin.FaultStatisticalQuantitySearchModule;

/**
 * 瑞金医院
 */
public interface BizRuiJinDao {
    /**
     * 瑞金大屏电梯数量接口 公用图表统计
     */
    List<Map> countElevatorClassificationInfo(@Param("villageId") String villageId, @Param("vProjectId") String vProjectId, @Param("userId") String userId, @Param("isAdminFlag") boolean isAdminFlag);

    /**
     * 大屏电梯数量接口 公用图表统计 通用
     */
    List<Map> countElevatorClassificationInfoV1(@Param("faultStatisticalQuantitySearchModule") FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 故障统计 困人统计 不文明行为统计
     */
    List<Map> faultStatisticalQuantity(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 故障统计 困人统计 不文明行为统计 骋隆
     */
    List<Map> faultStatisticalQuantityForCl(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 困人来源排行统计
     */
    List<Map> statisticsOnTheSourceOfPoverty(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);


    /**
     * 困人来源排行统计 骋隆
     */
    List<Map> statisticsOnTheSourceOfPovertyForCl(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 故障工单数
     */
    Integer searchWrokOrderCount(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 故障占比
     */
    List<Map> statisticsFaultPoverty(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 不文明行为统计（每种不文明行为次数占比）
     */
    List<Map> uncivilizedBehavior(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 不文明行为统计（每种不文明行为次数占比）通用
     */
    List<Map> uncivilizedBehaviorV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 不文明行为统计（每种不文明行为次数占比）骋隆
     */
    List<Map> uncivilizedBehaviorForCl(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 每种不文明行为前三电梯占比
     */
    List<Map> uncivilizedBehaviorByTopThree(@Param("faultStatisticalQuantitySearchModule") FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule, @Param("faultType") String faultType);

    /**
     * 每种不文明行为前三电梯占比
     */
    List<Map> uncivilizedBehaviorByTopThreeV1(@Param("faultStatisticalQuantitySearchModule") FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule, @Param("faultType") String faultType);


    /**
     * 每种不文明行为前三电梯占比 运行总数
     */
    List<Map> uncivilizedBehaviorByTopThreeV1RunCount(@Param("faultStatisticalQuantitySearchModule") FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);
    /** 不文明行为统计走势图 */
    // 调用原来的位置controller

    /**
     * 智能监管
     */
    List<Map> intelligentSupervision(@Param("module") FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 智能监管 通用
     */
    List<Map> intelligentSupervisionV1(@Param("module") FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 智能监管 骋隆
     */
    List<Map> intelligentSupervisionForCl(@Param("module") FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 基本电梯信息查询 大屏
     */
    Map getElevatorInfo(@Param("register_number") String register_number);

    /**
     * 电梯历史信息 困人记录 故障记录 不文明行为
     */
    List<Map> getElevatorHlsInfoSleepy(@Param("register_number") String register_number);

    /**
     * 电梯历史信息 困人记录 故障记录 不文明行为
     */
    List<Map> getElevatorHlsInfoFault(@Param("register_number") String register_number);

    /**
     * 电梯历史信息 困人记录 故障记录 不文明行为
     */
    List<Map> getElevatorHlsInfoUncivilizedBehavior(@Param("register_number") String register_number);

    /**
     * 电梯历史信息 困人记录 故障记录 不文明行为 骋隆
     */
    List<Map> getElevatorHlsInfoUncivilizedBehaviorForCl(@Param("register_number") String register_number);

    /**
     * 根据工单查看工单详情信息
     */
    List<Map> getEnventDetailById(@Param("event_id") String event_id);

    /**
     * 维保工单次数统计
     */
    List<Map> numberOfMaintenanceWorkOrders(@Param("faultStatisticalQuantitySearchModule") FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

//    /**
//     * 正常维保记录数
//     */
//    int numberOfNormalMaintenanceWorkOrders(@Param("faultStatisticalQuantitySearchModule") FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 超期维保记录数
     */
    int numberOfOverdueMaintenanceWorkOrders(@Param("faultStatisticalQuantitySearchModule") FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 超期维保记录数
     */
    List<Map> queryMaintenanceWorkOrders(@Param("faultStatisticalQuantitySearchModule") FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    List<Map> queryMaintenanceWorkOrdersList(SearchFaultModule searchFaultModule);

    /**
     * 困人救援统计
     */
    List<Map> statisticsOfTrappedPeopleRescue(@Param("faultStatisticalQuantitySearchModule") FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 困人救援统计平均时长
     */
    Integer getTrappedPeopleTime(@Param("faultStatisticalQuantitySearchModule") FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 困人工单历史
     */
    List<Map> theHistoryOfSingleLabor(@Param("faultStatisticalQuantitySearchModule") FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 困人工单历史
     */
    List<Map> searchUncivilizedBehavior();

    /**
     * 本年度不文明行为走势
     */
    List<Map> trendOfUncivilizedBehaviorInThisYear(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 本年度不文明行为走势 通用
     */
    List<Map> trendOfUncivilizedBehaviorInThisYearV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 本年度不文明行为走势 骋隆
     */
    List<Map> trendOfUncivilizedBehaviorInThisYearForCl(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 前一月不文明行为走势
     */
    List<Map> trendOfUncivilizedBehaviorInThePreviousMonth(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 前一月不文明行为走势 通用
     */
    List<Map> trendOfUncivilizedBehaviorInThePreviousMonthV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 前一月不文明行为走势 骋隆
     */
    List<Map> trendOfUncivilizedBehaviorInThePreviousMonthForCl(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 前一周不文明行为走势
     */
    List<Map> trendOfUncivilizedBehaviorInThePreviousWeek(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 前一周不文明行为走势 通用
     */
    List<Map> trendOfUncivilizedBehaviorInThePreviousWeekV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 前一周不文明行为走势 骋隆
     */
    List<Map> trendOfUncivilizedBehaviorInThePreviousWeekForCl(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 电梯故障工单走势图 年度
     */
    List<Map> failureWorkOrderTrendChart(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 电梯故障工单走势图 月度
     */
    List<Map> monthlyTrendChartOfFaultWorkOrder(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 电梯故障工单走势图 周
     */
    List<Map> elevatorFaultWorkOrderTrendChartWeek(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 历史维保数据走势 年
     */
    List<Map> trendYearOfHistoricalMaintenanceData(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 历史维保数据走势 月
     */
    List<Map> historicalMaintenanceDataTrendMonth(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 历史维保数据走势 周
     */
    List<Map> trendWeekOfHistoricalMaintenanceData(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /***
     * 获取电梯每个状态的数量
     * @param userId
     * @param isAdminFlag
     * @return
     */
    List<Map> countElevator(@Param("villageId") String villageId, @Param("userId") String userId, @Param("isAdminFlag") boolean isAdminFlag);

    /***
     * 获取电梯每个状态的数量
     * @param userId
     * @param isAdminFlag
     * @return
     */
    List<Map> countElevatorV1(@Param("villageId") String villageId, @Param("userId") String userId, @Param("isAdminFlag") boolean isAdminFlag);


    /**
     * 查询电梯列表
     */
    List<Map<String, Object>> searchElevatorList(@Param("SearchElevatorModule") SearchElevatorModule SearchElevatorModule);

    /**
     * 查询电梯故障分页
     */
    List<Map> searchFaultList(SearchFaultModule searchFaultModule);


    /**
     * 获取维保记录列表
     */
    List<Map> searchMaintenanceList(SearchFaultModule searchFaultModule);

    /**
     * 最新的上报困人(仪电推送故障， 困人中的记录)
     */
    Map getPersonShuttingLately(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 查询电梯故障分页
     */
    List<EventDownloadModuleMap> searchEventsForDownload(SearchFaultModule searchFaultModule);

    /**
     * 获取故障统计
     *
     * @param faultStatisticalQuantitySearchModule
     * @return
     */
    int searchFaultCount(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 获取困人故障统计
     *
     * @param faultStatisticalQuantitySearchModule
     * @return
     */
    int searchtrappedPeopleCount(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 获取最近一个月反复阻挡门统计
     *
     * @param faultStatisticalQuantitySearchModule
     * @return
     */
    int queryStopCount(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 最近一月运行频次统计
     *
     * @param faultStatisticalQuantitySearchModule
     * @return
     */
    Integer getRunCount(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    //获取本年度维保工单
    List<Map> getOverdueOrders(@Param("faultStatisticalQuantitySearchModule") FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 获取当日困人电梯
     *
     * @param faultStatisticalQuantitySearchModule
     * @return
     */
    List<String> getTodayTrappedPeopleEleName(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /*电梯故障占比-儿童医院项目只统计平台故障*/
    List<Map> statisticsFaultPovertyByMX(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /*电梯故障工单走势图——儿童医院项目只统计平台数据*/
    List<Map> failureWorkOrderTrendChartByMX(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 儿童医院项目——故障频次统计麦信平台故障
     *
     * @param faultStatisticalQuantitySearchModule
     * @return
     */
    int searchFaultCountForMX(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 儿童医院项目——困人频次统计麦信平台故障
     *
     * @param faultStatisticalQuantitySearchModule
     * @return
     */
    int searchtrappedPeopleCountForMX(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 儿童医院项目——救援时长统计麦信平台故障时长
     *
     * @param faultStatisticalQuantitySearchModule
     * @return
     */
    Integer getTrappedPeopleTimeForMX(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 历史困人救援统计——儿童医院项目，统计麦信平台故障
     *
     * @param faultStatisticalQuantitySearchModule
     * @return
     */
    List<Map> statisticsOfTrappedPeopleRescueForMX(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 儿童医院项目——历史困人工单统计平台故障
     *
     * @param faultStatisticalQuantitySearchModule
     * @return
     */
    List<Map> theHistoryOfSingleLaborForMX(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 电梯基础数据
     *
     * @param searchFaultModule
     * @return
     */
    List<Map> getRunCountInfo1(SearchFaultModule searchFaultModule);

    /**
     * 基础运行数据统计
     *
     * @param searchFaultModule
     * @return
     */
    List<Map> getRunCountInfo2(SearchFaultModule searchFaultModule);

    /**
     * 困人故障统计维保次数统计
     *
     * @param searchFaultModule
     * @return
     */
    List<Map> getRunCountInfo3(SearchFaultModule searchFaultModule);

    /**
     * 困人故障统计维保次数统计,麦信维保平台
     *
     * @param searchFaultModule
     * @return
     */
    List<Map> getRunCountInfo3Mx(SearchFaultModule searchFaultModule);

    /**
     * 维保次数统计
     *
     * @param searchFaultModule
     * @return
     */
    List<Map> getRunCountInfo4(SearchFaultModule searchFaultModule);

    /**
     * 关门受阻挡次数统计
     *
     * @param searchFaultModule
     * @return
     */
    List<Map> getRunCountInfo5(SearchFaultModule searchFaultModule);

    /**
     * 电动车乘梯次数统计
     *
     * @param searchFaultModule
     * @return
     */
    List<Map> getRunCountInfo6(SearchFaultModule searchFaultModule);

    /**
     * 瑞金大屏首页状态
     *
     * @param searchFaultModule
     * @return
     */
    List<Map> getElevatorsStatus(SearchFaultModule searchFaultModule);

    List<Map> queryBuilding(SearchFaultModule searchFaultModule);

    /**
     * 获取年检信息
     *
     * @param searchFaultModule
     * @return
     */
    List<Map> queryAnnualInspectionList(SearchFaultModule searchFaultModule);

    /**
     * 统计故障次数信息 骋隆 麦信数据源
     *
     * @param faultStatisticalQuantitySearchModule
     */
    List<Map> statisticsOfFailureTimesForClWithMXData(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 获取电梯基本信息 麦信数据源
     *
     * @param register_number
     * @return
     */
    Map getElevatorInfoWithMXData(String register_number);

    /**
     * 获取困人记录 麦信数据源
     *
     * @param register_number
     * @return
     */
    List<Map> getElevatorHlsInfoTrappedWithMXData(String register_number);

    /**
     * 获取前三故障记录 麦信数据源
     *
     * @param register_number
     * @return
     */
    List<Map> getElevatorHlsInfoFaultWithMXData(String register_number);

    /**
     * 获取故障列表(麦信推送故障)
     *
     * @param searchFaultModule
     * @return
     */
    List<Map> searchFaultListWithMXData(SearchFaultModule searchFaultModule);

    /**
     * 维修下载
     *
     * @param searchFaultModule
     * @return
     */
    List<QueryFaultExportModule> searchFaultListDownload(SearchFaultModule searchFaultModule);

    /**
     * 维保下载
     *
     * @param searchFaultModule
     * @return
     */
    List<QueryMaintenanceExportModule> queryMaintenanceWorkOrdersListDownload(SearchFaultModule searchFaultModule);

    /**
     * 获取年检记录
     *
     * @param searchFaultModule
     * @return
     */
    List<Map> queryRuiJinAnnualCheckList(SearchFaultModule searchFaultModule);
}
