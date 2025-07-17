package com.shmashine.api.service.wuye;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shmashine.api.module.fault.input.SearchFaultModule;
import com.shmashine.api.module.ruijin.FaultStatisticalQuantitySearchModule;

public interface MaiXinEventService {

    /**
     * 本地
     *
     * @param searchFaultModule
     * @return
     */
    List<HashMap<String, Object>> getFaultOrderByConfirmOrCompleted(SearchFaultModule searchFaultModule);

    /**
     * 本地困人
     *
     * @param searchFaultModule
     * @return
     */
    List<HashMap<String, Object>> getPeopleTrappedCountByVillage(SearchFaultModule searchFaultModule);

    /**
     * 本地反复阻挡门
     *
     * @param searchFaultModule
     * @return
     */
    List<HashMap<String, Object>> getBlockDoorByVillage(SearchFaultModule searchFaultModule);

    /**
     * 获取仪电困人数
     *
     * @param searchFaultModule
     * @return
     */
    HashMap<String, Object> getPeopleTrappedCount(SearchFaultModule searchFaultModule);

    /**
     * 仪电反推且状态为已确认和已完成的工单 不以小区group
     *
     * @param searchFaultModule
     * @return
     */
    HashMap<String, Object> getFaultOrderByConfirmOrCompletedTotal(SearchFaultModule searchFaultModule);

    /**
     * 仪电反推且状态为已确认和已完成的工单 不以小区group
     *
     * @param searchFaultModule
     * @return
     */
    HashMap<String, Object> getBlockDoorTotal(SearchFaultModule searchFaultModule);

    /**
     * 获取不文明统计
     *
     * @param searchFaultModule
     * @return
     */
    HashMap<String, Object> getUncivilizedBehaviorTotal(SearchFaultModule searchFaultModule);

    /**
     * 电动车入梯
     *
     * @param searchFaultModule
     * @return
     */
    HashMap<String, Object> getElectronBikeTotal(SearchFaultModule searchFaultModule);

    /**
     * 智能监管
     */
    List<Map> intelligentSupervision(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 智能监管, 城桥
     */
    List<Map> intelligentSupervisionCQ(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);
}
