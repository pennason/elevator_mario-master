package com.shmashine.userclientapplets.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shmashine.userclientapplets.entity.Maintenance;

/**
 * 维保DAO
 *
 * @author jiangheng
 * @version V1.0.0 - 2022/2/15 11:21
 */
public interface MaintenanceDao extends BaseMapper<Maintenance> {

    /**
     * 获取今日需维保电梯
     */
    String queryMaintenanceByDay(@Param("userId") String userId, @Param("admin") boolean admin);

    /**
     * 获取本年度维保记录
     *
     * @param elevatorId 电梯id
     */
    List<Map> getOverdueOrders(String elevatorId);
}
