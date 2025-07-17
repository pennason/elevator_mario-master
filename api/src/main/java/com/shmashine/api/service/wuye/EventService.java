package com.shmashine.api.service.wuye;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.fault.input.SearchFaultModule;
import com.shmashine.api.module.ruijin.FaultStatisticalQuantitySearchModule;

public interface EventService {
    /**
     * 获取仪电困人数
     *
     * @param searchFaultModule
     * @return
     */
    HashMap<String, Object> getPeopleTrappedCount(SearchFaultModule searchFaultModule);

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
     * 根据时间统计故障工单柱状图
     *
     * @param searchFaultModule
     * @return
     */
    HashMap<String, Object> getEventCountByTime(SearchFaultModule searchFaultModule);

    /**
     * 获取仪电反推故障工单
     *
     * @param searchFaultModule
     * @return
     */
    PageListResultEntity getEventByPage(SearchFaultModule searchFaultModule);

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
    List<Map> intelligentSupervision(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);
}
