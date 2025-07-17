package com.shmashine.fault.dal.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.fault.dal.dataobject.DeviceParamConfDO;

/**
 * 设备参数配置
 *
 * @author jiangheng
 * @version V1.0.0 - 2023/12/5 11:17
 */
@Mapper
public interface DeviceConfDao {

    /**
     * 根据电梯code和设备类型获取配置
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     */
    DeviceParamConfDO getConfByCodeAndType(@Param("elevatorCode") String elevatorCode,
                                           @Param("sensorType") String sensorType);

    /**
     * 更新设备配置状态
     *
     * @param deviceParamConf 设备参数配置
     */
    int updateDeviceConfStatus(DeviceParamConfDO deviceParamConf);

    int updateDeviceConf(DeviceParamConfDO deviceParamConf);

    /**
     * 根据电梯编号和设备类型获取配置状态
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @return 设备配置状态 0：未配置 1：已下发 2：已配置
     */
    Integer getConfStatusByCodeAndSensorType(@Param("elevatorCode") String elevatorCode,
                                             @Param("sensorType") String sensorType);
}
