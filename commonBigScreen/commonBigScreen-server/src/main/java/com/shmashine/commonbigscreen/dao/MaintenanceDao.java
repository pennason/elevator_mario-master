package com.shmashine.commonbigscreen.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shmashine.commonbigscreen.entity.Maintenance;
import com.shmashine.commonbigscreen.entity.SearchFaultModule;

/**
 * MaintenanceDao
 *
 * @author jiangheng
 * @version V1.0 - 2022/3/14 13:53
 */
public interface MaintenanceDao extends BaseMapper<Maintenance> {

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
