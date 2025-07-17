package com.shmashine.fault.dal.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblDevice;

/**
 * 设备表
 *
 * @author jiangheng
 * @version V1.0.0 - 2023/12/5 15:00
 */
@Mapper
public interface TblDeviceDao {

    /**
     * 根据电梯id和设备类型获取设备
     *
     * @param elevatorId 电梯id
     * @param sensorType 设备类型
     * @return 设备
     */
    TblDevice getByElevatorIdAndSensorType(@Param("elevatorId") String elevatorId,
                                           @Param("sensorType") String sensorType);
}
