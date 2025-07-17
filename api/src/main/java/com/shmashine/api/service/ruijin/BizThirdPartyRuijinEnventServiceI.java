package com.shmashine.api.service.ruijin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.elevator.SearchElevatorModule;
import com.shmashine.api.module.fault.input.SearchFaultModule;
import com.shmashine.api.module.ruijin.EventDownloadModuleMap;
import com.shmashine.api.module.ruijin.FaultStatisticalQuantitySearchModule;

public interface BizThirdPartyRuijinEnventServiceI {

    /**
     * 获取维保记录下载列表
     */
    List<Map> downLoadSearchMaintenanceList(SearchFaultModule searchFaultModule);

    /**
     * 获取电梯数量
     */
    Map countElevator(String villageId, String userId, boolean isAdminFlag);

    /**
     * 获取电梯数量
     */
    Map countElevatorV1(String villageId, String userId, boolean isAdminFlag);

    /**
     * 删除瑞金医院数据
     */
    void deleteThirdPartyRuijinEnventData(String eventNumber);

    /**
     * 电梯分类占比
     */
    List<Map> elevatorClassification(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 电梯分类数量列表
     */
    List<Map> elevatorClassificationQuantityList(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 电梯分类数量列表 通用
     */
    List<Map> elevatorClassificationQuantityListV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 统计故障次数信息
     */
    List<Map> statisticsOfFailureTimes(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 统计故障次数信息 骋隆
     */
    List<Map> statisticsOfFailureTimesForCl(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 困人来源排行
     */
    List<Map> rankingOfPoorPeople(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 困人来源排行 骋隆
     */
    List<Map> rankingOfPoorPeopleForCl(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 电梯故障占比
     */
    List<Map> proportionOfElevatorFailures(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 电梯故障排名
     */
    List<Map> elevatorFailureRanking(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 不文明种类次数
     */
    List<Map> frequencyOfUncivilizedSpecies(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 不文明种类次数
     */
    List<Map> frequencyOfUncivilizedSpeciesV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 不文明种类次数 骋隆
     */
    List<Map> frequencyOfUncivilizedSpeciesForCl(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 不文明行为前三电梯排行
     */
    List<Map> frequencyOfUncivilizedSpeciesTop3(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 不文明行为前三电梯排行
     */
    List<Map> frequencyOfUncivilizedSpeciesTop3V1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 不文明行为走势图
     */
    Map<String, Object> trendOfUncivilizedBehavior(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 不文明行为走势图 通用
     */
    Map<String, Object> trendOfUncivilizedBehaviorV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 不文明行为走势图 骋隆
     */
    Map<String, Object> trendOfUncivilizedBehaviorForCl(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 智能监管
     */
    List<Map> intelligentSupervision(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 智能监管 骋隆
     */
    List<Map> intelligentSupervisionForCl(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 智能监管
     */
    List<Map> intelligentSupervisionV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 获取电梯基本信息
     */
    Map getElevatorInfo(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    LinkedHashMap getElevatorHlsInfo(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    LinkedHashMap getElevatorHlsInfoForCl(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    //    List<Map> searchSleepyRealStatus(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);
    List<Map> getEnventDetailById(String eventId);

    /**
     * 维保工单次数统计
     */
    List<Map> numberOfMaintenanceWorkOrders(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 统计工单状态占比
     */
    List<Map> statisticsWorkOrderStatusProportion(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 困人救援统计
     */
    List<Map> statisticsOfTrappedPeopleRescue(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 困人工单历史
     */
    List<Map> theHistoryOfSingleLabor(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 电梯故障工单走势图
     */
    Map failureWorkOrderTrendChart(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 历史维保数据走势
     */
    Map trendYearOfHistoricalMaintenance(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 获取电梯列表不分页
     */
    List<Map<String, Object>> searchElevatorListNoPage(SearchElevatorModule searchElevatorModule);

    /**
     * 获取故障列表
     *
     * @param searchFaultModule
     * @return
     */
    PageListResultEntity searchFaultsListWithPage(SearchFaultModule searchFaultModule);


    /**
     * 获取维保记录列表
     *
     * @param searchFaultModule
     * @return
     */
    PageListResultEntity searchMaintenanceList(SearchFaultModule searchFaultModule);

    /**
     * 历史维保数据走势
     */
    Map getPersonShuttingLately(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 获取故障列表
     *
     * @param searchFaultModule
     * @return
     */
    List<EventDownloadModuleMap> searchEventForDownload(SearchFaultModule searchFaultModule);

    /**
     * 获取当日困人电梯
     *
     * @param faultStatisticalQuantitySearchModule
     * @return
     */
    List<String> getTodayTrappedPeopleEleName(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 瑞金大屏——获取电梯运行数据
     *
     * @param searchFaultModule
     * @return
     */
    PageListResultEntity getRunCountInfo(SearchFaultModule searchFaultModule);

    /**
     * 运行统计数据导出
     *
     * @param searchFaultModule
     * @param response
     */
    String exportRunCountInfo(SearchFaultModule searchFaultModule, HttpServletResponse response);

    /**
     * 瑞金大屏首页状态
     *
     * @param searchFaultModule
     * @return
     */
    List<Map> getElevatorsStatus(SearchFaultModule searchFaultModule);

    /**
     * 获取楼宇
     *
     * @param searchFaultModule
     * @return
     */
    List<Map> queryBuilding(SearchFaultModule searchFaultModule);

    /**
     * 获取年检信息
     *
     * @param searchFaultModule
     * @return
     */
    PageListResultEntity queryAnnualInspectionList(SearchFaultModule searchFaultModule);

    /**
     * 统计故障次数信息 骋隆 麦信数据源
     *
     * @param faultStatisticalQuantitySearchModule
     * @return
     */
    List<Map> statisticsOfFailureTimesForClWithMXData(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 电梯故障占比 麦信数据源
     *
     * @param faultStatisticalQuantitySearchModule
     * @return
     */
    List<Map> proportionOfElevatorFailuresWithMXData(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 电梯故障排名 麦信数据源
     *
     * @param faultStatisticalQuantitySearchModule
     * @return
     */
    List<Map> elevatorFailureRankingWithMXData(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 取电梯基本信息 麦信数据源
     *
     * @param faultStatisticalQuantitySearchModule
     * @return
     */
    Map getElevatorInfoWithMXData(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 电梯 困人记录 故障记录 不文明行为 麦信数据源
     *
     * @param faultStatisticalQuantitySearchModule
     * @return
     */
    Map getElevatorHlsInfoWithMXData(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 获取故障列表(麦信推送故障)
     *
     * @param searchFaultModule
     * @return
     */
    PageListResultEntity searchFaultsListWithMXDataByPage(SearchFaultModule searchFaultModule);

    /**
     * 维修工单下载啊
     *
     * @param searchFaultModule
     * @param response
     */
    void searchFaultListDownload(SearchFaultModule searchFaultModule, HttpServletResponse response);

    /**
     * 维保记录下载
     *
     * @param searchFaultModule
     * @param response
     */
    void searchMaintenanceListDownload(SearchFaultModule searchFaultModule, HttpServletResponse response);

    /**
     * 获取电梯年检记录
     *
     * @param searchFaultModule
     * @return
     */
    PageListResultEntity queryRuiJinAnnualCheckList(SearchFaultModule searchFaultModule);
}