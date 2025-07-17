package com.shmashine.api.service.wuye;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.fault.input.FaultStatisticsModule;
import com.shmashine.api.module.fault.input.SearchFaultModule;

public interface MaiXinFaultService {

    /**
     * 获取故障列表
     *
     * @param searchFaultModule
     * @return
     */
    PageListResultEntity searchFaultsListWithPage(SearchFaultModule searchFaultModule);

    /**
     * 根据小区统计急修、隐患、电瓶车入梯
     *
     * @param searchFaultModule
     * @return
     */
    List<HashMap<String, Object>> getFaultCountByVillage(SearchFaultModule searchFaultModule);

    /**
     * 获取小区数据率
     *
     * @param searchFaultModule
     * @return
     */
    HashMap<String, HashMap<String, Object>> getVillageCountRate(SearchFaultModule searchFaultModule);

    /**
     * 获取小区数据率,城桥大屏
     *
     * @param searchFaultModule
     * @return
     */
    HashMap<String, HashMap<String, Object>> getVillageCountRateCQ(SearchFaultModule searchFaultModule);

    /**
     * 故障统计
     *
     * @param faultStatisticsModule
     * @return
     */
    Map<String, List> getFaultStatistics(FaultStatisticsModule faultStatisticsModule);


    /**
     * 故障统计, 电梯分组
     *
     * @param faultStatisticsModule
     * @return
     */
    Map<String, List> getFaultDataStatisticsGroupByElevator(FaultStatisticsModule faultStatisticsModule);
    Map<String, List> getFaultDataStatisticsGroupByType(FaultStatisticsModule faultStatisticsModule);
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
     * 物联网报警趋势
     *
     * @param faultStatisticsModule
     * @return
     */
    Map<String, List> getFaultTrend(FaultStatisticsModule faultStatisticsModule);
}
