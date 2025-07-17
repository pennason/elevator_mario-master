package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import com.shmashine.api.module.fault.input.SearchFaultModule;

public interface MaiXinWuyeMaintenanceDao {

    /**
     * 获取维保统计
     *
     * @param searchFaultModule
     * @return
     */
    Integer getMaintenanceCount(SearchFaultModule searchFaultModule);

    /**
     * 超期维保
     *
     * @param searchFaultModule
     * @return
     */
    Integer getOverdueMaintenanceCount(SearchFaultModule searchFaultModule);

    /**
     * 获取本年度维保记录
     *
     * @param elevatorId
     * @return
     */
    List<Map> getOverdueOrders(String elevatorId);

    /**
     * 维保记录
     *
     * @param searchFaultModule
     * @return
     */
    List<Map> queryMaintenanceWorkOrdersList(SearchFaultModule searchFaultModule);
}
