package com.shmashine.api.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.api.module.fault.input.SearchFaultModule;
import com.shmashine.api.module.ruijin.FaultStatisticalQuantitySearchModule;

@Mapper
public interface WuyeEventDao {
    /**
     * 获取仪电困人数
     *
     * @param searchFaultModule
     * @return
     */
    HashMap<String, Object> getPeopleTrappedCount(SearchFaultModule searchFaultModule);

    /**
     * 获取仪电困人数加入小区
     *
     * @param searchFaultModule
     * @return
     */
    List<HashMap<String, Object>> getPeopleTrappedCountByVillage(SearchFaultModule searchFaultModule);

    /**
     * 获取仪电故障数
     *
     * @param searchFaultModule
     * @return
     */
    Integer getFaultCount(SearchFaultModule searchFaultModule);

    /**
     * 仪电反推且状态为已确认和已完成的工单
     *
     * @param searchFaultModule
     * @return
     */
    List<HashMap<String, Object>> getFaultOrderByConfirmOrCompleted(SearchFaultModule searchFaultModule);

    /**
     * 仪电反推且状态为已确认和已完成的工单 不以小区group
     *
     * @param searchFaultModule
     * @return
     */
    HashMap<String, Object> getFaultOrderByConfirmOrCompletedTotal(SearchFaultModule searchFaultModule);

    /**
     * 仪电反推且状态为误报或新建工单两种状态的工单
     *
     * @param searchFaultModule
     * @return
     */
    List<HashMap<String, Object>> getFaultOrderByFalsePositiveOrNew(SearchFaultModule searchFaultModule);

    /**
     * 根据时间统计仪电反推且状态为已确认和已完成的工单
     *
     * @param searchFaultModule
     * @return
     */
    List<HashMap<String, Object>> getEventCountByTime(SearchFaultModule searchFaultModule);

    /**
     * 救援进度表
     *
     * @param eventId
     * @return
     */
    List<HashMap<String, Object>> getEventRealTimeSchedule(String eventId);

    /**
     * 困人救援详情
     *
     * @param faultId
     * @return
     */
    HashMap<String, Object> getPeopleTrappedDetails(String faultId);

    /**
     * 智能监管
     */
    List<Map> intelligentSupervision(@Param("module") FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);
}
