package com.shmashine.api.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.shmashine.api.module.elevator.SearchElevatorModule;
import com.shmashine.api.module.fault.input.FaultStatisticsModule;
import com.shmashine.api.module.fault.input.SearchFaultModule;

@Mapper
public interface WuyeFaultDao {
    /**
     * 获取平台故障统计
     *
     * @param searchFaultModule
     * @return
     */
    Integer getFaultCount(SearchFaultModule searchFaultModule);

    /**
     * 根据小区统计电瓶车入梯
     *
     * @param searchFaultModule
     * @return
     */
    List<HashMap<String, Object>> getElectroMobileFaultCount(SearchFaultModule searchFaultModule);

    /**
     * 根据部门获取所有小区信息——根据电梯数排序
     *
     * @param deptIds
     * @return
     */
    List<HashMap<String, Object>> getVillageInfoByDeptIdsAndElevatorCount(List<String> deptIds);

    /**
     * 根据部门获取所有小区信息——根据当日困人数排序
     *
     * @param deptIds
     * @return
     */
    List<HashMap<String, Object>> getVillageInfoByDeptIdsAndPeopleTrappedCount(List<String> deptIds);

    /**
     * 根据时间获取故障柱状图
     *
     * @param searchFaultModule
     * @return
     */
    List<HashMap<String, Object>> getFaultChartByTime(SearchFaultModule searchFaultModule);

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
     * 获取困人总条数
     *
     * @param searchFaultModule
     * @return
     */
    Integer getTrappedPeopleTotal(SearchFaultModule searchFaultModule);

    /**
     * 救援时长统计麦信平台故障时长
     *
     * @param searchFaultModule
     * @return
     */
    Integer getTrappedPeopleTimeForMX(SearchFaultModule searchFaultModule);

    /**
     * 获取取证文件
     *
     * @param faultId
     * @return
     */
    List<HashMap<String, Object>> getFaultFileById(String faultId);

    /**
     * 查询错误信息
     *
     * @param faultStatisticsModule
     * @return
     */
    List<HashMap<String, Object>> getFaultStatistics(FaultStatisticsModule faultStatisticsModule);


    /**
     * 查询错误信息, 电梯分组
     *
     * @param faultStatisticsModule
     * @return
     */
    List<HashMap<String, Object>> getFaultStatisticsGroupByElevator(FaultStatisticsModule faultStatisticsModule);

    /**
     * 查询错误信息
     *
     * @param faultStatisticsModule
     * @return
     */
    List<HashMap<String, Object>> getFaultStatisticsRepairs(FaultStatisticsModule faultStatisticsModule);

    /**
     * 查询错误信息, 电梯分组
     *
     * @param faultStatisticsModule
     * @return
     */
    List<HashMap<String, Object>> getFaultStatisticsRepairsGroupByElevator(FaultStatisticsModule faultStatisticsModule);

    /**
     * 查询错误信息
     *
     * @param faultStatisticsModule
     * @return
     */
    List<HashMap<String, Object>> getFaultStatisticsPersonTrapped(FaultStatisticsModule faultStatisticsModule);

    /**
     * 查询错误信息, 电梯分组
     *
     * @param faultStatisticsModule
     * @return
     */
    List<HashMap<String, Object>> getFaultStatisticsPersonTrappedGroupByElevator(FaultStatisticsModule faultStatisticsModule);

    /**
     * 不文明统计
     *
     * @param faultStatisticsModule
     * @return
     */
    List<HashMap<String, Object>> getUncivilizedStatistics(FaultStatisticsModule faultStatisticsModule);

    /**
     * 不文明统计,电梯分组
     *
     * @param faultStatisticsModule
     * @return
     */
    List<HashMap<String, Object>> getUncivilizedStatisticsGroupByElevator(FaultStatisticsModule faultStatisticsModule);

    /**
     * 不文明统计
     *
     * @param faultStatisticsModule
     * @return
     */
    List<HashMap<String, Object>> getUncivilizedStatisticsElectricBike(FaultStatisticsModule faultStatisticsModule);

    /**
     * 不文明统计,电梯分组
     *
     * @param faultStatisticsModule
     * @return
     */
    List<HashMap<String, Object>> getUncivilizedStatisticsElectricBikeGroupByElevator(FaultStatisticsModule faultStatisticsModule);

    /**
     * 不文明统计
     *
     * @param faultStatisticsModule
     * @return
     */
    List<HashMap<String, Object>> getUncivilizedStatisticsBlockDoor(FaultStatisticsModule faultStatisticsModule);

    /**
     * 不文明统计,电梯分组
     *
     * @param faultStatisticsModule
     * @return
     */
    List<HashMap<String, Object>> getUncivilizedStatisticsBlockDoorGroupByElevator(FaultStatisticsModule faultStatisticsModule);

    /**
     * 维修趋势
     */
    List<HashMap<String, Object>> getRepairTrend(FaultStatisticsModule faultStatisticsModule);

    /**
     * 维保趋势
     */
    List<HashMap<String, Object>> getMaintenanceTrend(FaultStatisticsModule faultStatisticsModule);

    /**
     * 不文明趋势
     */
    List<HashMap<String, Object>> getUncivilizedTrend(FaultStatisticsModule faultStatisticsModule);

    List<String> getElevator(SearchFaultModule searchFaultModule);
}
