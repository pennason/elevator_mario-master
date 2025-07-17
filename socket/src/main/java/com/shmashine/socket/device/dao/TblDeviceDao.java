package com.shmashine.socket.device.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.socket.device.entity.DeviceParamConfDO;
import com.shmashine.socket.device.entity.TblDevice;

/**
 * 设备表(TblDevice)表数据库访问层
 *
 * @author little.li
 * @since 2020-06-14 15:14:31
 */
@Mapper
public interface TblDeviceDao {


    /**
     * 根据所属电梯编号、设备类型 获取设备
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @return 设备数据
     */
    TblDevice queryByElevatorCodeAndSensorType(@Param("elevatorCode") String elevatorCode,
                                               @Param("sensorType") String sensorType);

    /**
     * 更新设备信息
     */
    void updateByElevatorCodeAndSensorType(TblDevice device);


    void updateDeviceId(@Param("elevatorCode") String elevatorCode, @Param("sensorType") String sensorType,
                        @Param("id") String id);

    List<TblDevice> listByElevatorCode(@Param("elevatorCode") String elevatorCode);

    void cancelDeviceTimeOutEvent(String id);

    /*获取故障中的离线告警记录*/
    List<String> getDeviceTimeOutEvent(@Param("elevatorCode") String elevatorCode,
                                       @Param("sensorType") String sensorType);

    /**
     * 获取传感器故障屏蔽列表
     */
    List<HashMap<String, String>> getSensorFaultShields();

    /**
     * 获取传感器关联故障屏蔽列表
     */
    List<HashMap<String, String>> getFaultShields();

    /**
     * 获取传感器故障屏蔽列表
     *
     * @param elevatorCode 电梯编号
     */
    HashMap<String, String> getSensorFaultShieldsByElevator(String elevatorCode);

    /**
     * 获取设备参数配置信息
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @return 设备参数配置信息
     */
    DeviceParamConfDO getDeviceParamConf(@Param("elevatorCode") String elevatorCode,
                                         @Param("sensorType") String sensorType);
}