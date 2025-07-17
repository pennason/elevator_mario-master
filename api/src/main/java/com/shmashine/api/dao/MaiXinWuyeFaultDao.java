package com.shmashine.api.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shmashine.api.module.fault.input.FaultStatisticsModule;
import com.shmashine.api.module.fault.input.SearchFaultModule;

/**
 * 麦信本地物业
 */
public interface MaiXinWuyeFaultDao {

    /**
     * 查询电梯故障分页
     */
    List<Map> searchFaultList(SearchFaultModule searchFaultModule);

    /**
     * 获取平台故障统计
     *
     * @param searchFaultModule
     * @return
     */
    Integer getFaultCount(SearchFaultModule searchFaultModule);

    /**
     * 查询错误信息
     *
     * @param faultStatisticsModule
     * @return
     */
    List<HashMap<String, Object>> getFaultStatistics(FaultStatisticsModule faultStatisticsModule);

    /**
     * 查询错误信息 电梯分组
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
     * 查询错误信息,电梯分组
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
     * 查询错误信息 电梯分组
     *
     * @param faultStatisticsModule
     * @return
     */
    List<HashMap<String, Object>> getFaultStatisticsPersonTrappedGroupByElevator(FaultStatisticsModule faultStatisticsModule);

    /**
     * 维修趋势
     */
    List<HashMap<String, Object>> getRepairTrend(FaultStatisticsModule faultStatisticsModule);

    /**
     * 维保趋势
     */
    List<HashMap<String, Object>> getMaintenanceTrend(FaultStatisticsModule faultStatisticsModule);

    /**
     * 物联报警趋势
     */
    List<HashMap<String, Object>> getFaultTrend(FaultStatisticsModule faultStatisticsModule);
}
