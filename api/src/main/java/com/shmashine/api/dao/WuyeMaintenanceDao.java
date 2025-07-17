package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.shmashine.api.module.fault.input.SearchFaultModule;

@Mapper
public interface WuyeMaintenanceDao {

    /**
     * 获取维保统计
     *
     * @param searchFaultModule
     * @return
     */
    Integer getMaintenanceCount(SearchFaultModule searchFaultModule);

    /**
     * 获取本年度维保记录
     *
     * @param elevatorId
     * @return
     */
    List<Map> getOverdueOrders(String elevatorId);

    /**
     * 超期维保
     *
     * @param searchFaultModule
     * @return
     */
    Integer getOverdueMaintenanceCount(SearchFaultModule searchFaultModule);
}
