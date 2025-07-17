package com.shmashine.userclientapplets.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shmashine.userclientapplets.entity.Maintenance;
import com.shmashine.userclientapplets.entity.SearchFaultModule;

/**
 * 维保服务
 *
 * @author jiangheng
 * @version V1.0.0 -2022/2/15 11:19
 */
public interface MaintenanceService extends IService<Maintenance> {

    /**
     * 获取维保详情
     *
     * @param workOrderNumber 工单号
     * @return 工单详情
     */
    Maintenance getMaintenanceById(String workOrderNumber);

    /**
     * 获取维保记录列表
     *
     * @param searchFaultModule 查询条件
     * @return 维保记录列表
     */
    HashMap queryMaintenanceList(SearchFaultModule searchFaultModule);

    /**
     * 获取今日需维保电梯列表
     *
     * @return 维保电梯列表
     */
    String queryMaintenanceByDay(String userId, boolean admin);

    /**
     * 获取本年度维保记录
     *
     * @param elevatorId 电梯id
     * @return 年度维保记录
     */
    List<Map> getOverdueOrders(String elevatorId);

    /**
     * 获取年检信息
     *
     * @param searchFaultModule 查询条件
     * @return 年检信息
     */
    HashMap queryAnnualInspectionList(SearchFaultModule searchFaultModule);
}
