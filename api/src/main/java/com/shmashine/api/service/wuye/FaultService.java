package com.shmashine.api.service.wuye;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shmashine.api.module.elevator.SearchElevatorModule;
import com.shmashine.api.module.fault.input.FaultStatisticsModule;
import com.shmashine.api.module.fault.input.SearchFaultModule;

public interface FaultService {

    /**
     * 获取故障统计
     *
     * @param searchFaultModule
     * @return
     */
    HashMap<String, Integer> getFaultCount(SearchFaultModule searchFaultModule);

    /**
     * 根据小区统计急修、隐患、电瓶车入梯
     *
     * @param searchFaultModule
     * @return
     */
    List<HashMap<String, Object>> getFaultCountByVillage(SearchFaultModule searchFaultModule);

    /**
     * 根据时间获取故障柱状图
     *
     * @param searchFaultModule
     * @return
     */
    HashMap<String, Object> getFaultChartByTime(SearchFaultModule searchFaultModule);

    /**
     * 获取当日困人电梯
     *
     * @param searchElevatorModule
     * @return
     */
    HashMap<String, Object> getTodayPeopleTrappedElevator(SearchElevatorModule searchElevatorModule);

    /**
     * 获取当日故障电梯
     *
     * @param searchElevatorModule
     * @return
     */
    HashMap<String, Object> getTodayFaultElevator(SearchElevatorModule searchElevatorModule);

    /**
     * 获取故障中故障
     *
     * @param elevatorCode
     * @return
     */
    HashMap<String, Object> getThisMomentFault(String elevatorCode);

    /**
     * 事件回溯
     *
     * @param searchFaultModule
     * @return
     */
    HashMap<String, Integer> getHistoryRecordCount(SearchFaultModule searchFaultModule);

    /**
     * 获取故障数量
     *
     * @param searchFaultModule
     * @return
     */
    Integer queryFaultNumber(SearchFaultModule searchFaultModule);


    /**
     * 获取困人数
     *
     * @param searchFaultModule
     * @return
     */
    Integer queryTrappedPeopleNumber(SearchFaultModule searchFaultModule);

    /**
     * 救援时长统计麦信平台故障时长
     *
     * @param searchFaultModule
     * @return
     */
    Integer getTrappedPeopleTimeForMX(SearchFaultModule searchFaultModule);

    /**
     * 获取故障取证文件
     *
     * @param faultId
     * @return
     */
    List<HashMap<String, Object>> getFaultFileById(String faultId);

    /**
     * 获取小区数据率
     *
     * @param searchFaultModule
     * @return
     */
    HashMap<String, HashMap<String, Object>> getVillageCountRate(SearchFaultModule searchFaultModule);

    /**
     * 故障统计
     *
     * @param faultStatisticsModule
     * @return
     */
    Map<String, List> getFaultStatistics(FaultStatisticsModule faultStatisticsModule);

    /**
     * 故障统计 电梯分组
     *
     * @param faultStatisticsModule
     * @return
     */
    Map<String, List> getFaultStatisticsGroupByElevator(FaultStatisticsModule faultStatisticsModule);

    /**
     * 不文明统计
     *
     * @param faultStatisticsModule
     * @return
     */
    Map<String, List> getUncivilizedStatistics(FaultStatisticsModule faultStatisticsModule);

    /**
     * 不文明统计,电梯分组
     *
     * @param faultStatisticsModule
     * @return
     */
    Map<String, List> getUncivilizedStatisticsGroupByElevator(FaultStatisticsModule faultStatisticsModule);

    /**
     * 急修工单趋势
     */
    Map<String, List> getRepairTrend(FaultStatisticsModule faultStatisticsModule);

    /**
     * 维保工单趋势
     *
     * @param faultStatisticsModule
     * @return
     */
    Map<String, List> getMaintenanceTrend(FaultStatisticsModule faultStatisticsModule);

    /**
     * 不文明趋势
     *
     * @param faultStatisticsModule
     * @return
     */
    Map<String, List> getUncivilizedTrend(FaultStatisticsModule faultStatisticsModule);
}
