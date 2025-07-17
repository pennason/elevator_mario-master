package com.shmashine.api.service.wuye;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.elevator.SearchElevatorModule;
import com.shmashine.api.module.fault.input.SearchFaultModule;

public interface MaintenanceService {

    /**
     * 获取维保逾期电梯
     *
     * @param searchElevatorModule
     * @return
     */
    HashMap<String, Object> getMaintenanceOverdueElevator(SearchElevatorModule searchElevatorModule);

    /**
     * 获取年检逾期电梯
     *
     * @param searchElevatorModule
     * @return
     */
    HashMap<String, Object> getYearCheckOverdueElevator(SearchElevatorModule searchElevatorModule);

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
     * 获取维保记录列表
     *
     * @param searchFaultModule
     * @return
     */
    PageListResultEntity searchMaintenanceList(SearchFaultModule searchFaultModule);
}
