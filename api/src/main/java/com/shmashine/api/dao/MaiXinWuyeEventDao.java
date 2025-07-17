package com.shmashine.api.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.module.fault.input.SearchFaultModule;
import com.shmashine.api.module.ruijin.FaultStatisticalQuantitySearchModule;

public interface MaiXinWuyeEventDao {

    /**
     * 获取困人数
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
     * 仪电反推且状态为已确认和已完成的工单 不以小区group
     *
     * @param searchFaultModule
     * @return
     */
    HashMap<String, Object> getFaultOrderByConfirmOrCompletedTotal(SearchFaultModule searchFaultModule);

    /**
     * 反复阻挡门
     *
     * @param searchFaultModule
     * @return
     */
    HashMap<String, Object> getBlockDoorTotal(SearchFaultModule searchFaultModule);

    /**
     * 不文明行为
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
     * 仪电反推且状态为已确认和已完成的工单
     *
     * @param searchFaultModule
     * @return
     */
    List<HashMap<String, Object>> getFaultOrderByConfirmOrCompleted(SearchFaultModule searchFaultModule);

    /**
     * 反复阻挡门
     *
     * @param searchFaultModule
     * @return
     */
    List<HashMap<String, Object>> getBlockDoorByVillage(SearchFaultModule searchFaultModule);

    /**
     * 智能监管
     */
    List<Map> intelligentSupervision(@Param("module") FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 智能监管,城桥大屏项目
     */
    List<Map> intelligentSupervisionCQ(@Param("module") FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);
}
