package com.shmashine.fault.dal.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.shmashine.common.entity.DeviceMaintenanceOrder;

/**
 * 设备维保工单DAO
 *
 * @author jiangheng
 * @version V1.0.0 - 2023/6/29 13:53
 */
@Mapper
public interface DeviceMaintenanceOrderDAO {

    /**
     * 获取未完成状态的运维单
     *
     * @param elevatorCode 电梯编号
     * @return 维保单列表
     */
    List<DeviceMaintenanceOrder> queryByElevatorAndNotComplete(String elevatorCode);

    /**
     * 更新工单
     *
     * @param maintenanceOrder 工单
     */
    void updateOrderById(DeviceMaintenanceOrder maintenanceOrder);

    /**
     * 新增工单
     *
     * @param maintenanceOrder 工单
     */
    void insert(DeviceMaintenanceOrder maintenanceOrder);
}
