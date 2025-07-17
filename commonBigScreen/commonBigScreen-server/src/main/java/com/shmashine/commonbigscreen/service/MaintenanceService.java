package com.shmashine.commonbigscreen.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shmashine.commonbigscreen.entity.Maintenance;
import com.shmashine.commonbigscreen.entity.SearchElevatorModule;
import com.shmashine.commonbigscreen.entity.SearchFaultModule;

/**
 * MaintenanceService
 *
 * @author jiangheng
 * @version 1.0 - 2022/3/7 11:31
 */
public interface MaintenanceService extends IService<Maintenance> {

    /**
     * 获取维保逾期电梯
     *
     * @param searchElevatorModule 查询参数
     */
    HashMap<String, Object> getMaintenanceOverdueElevator(SearchElevatorModule searchElevatorModule);

    /**
     * 获取年检逾期电梯
     *
     * @param searchElevatorModule 查询参数
     */
    HashMap<String, Object> getYearCheckOverdueElevator(SearchElevatorModule searchElevatorModule);

    /**
     * 获取维保统计
     *
     * @param searchFaultModule 查询参数
     */
    Integer getMaintenanceCount(SearchFaultModule searchFaultModule);

    /**
     * 获取本年度维保记录
     *
     * @param elevatorId 电梯id
     */
    List<Map> getOverdueOrders(String elevatorId);
}
