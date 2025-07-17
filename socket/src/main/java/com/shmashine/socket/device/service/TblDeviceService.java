package com.shmashine.socket.device.service;

import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.socket.device.entity.DeviceParamConfDO;
import com.shmashine.socket.device.entity.TblDevice;

/**
 * 设备表(TblDevice)表服务接口
 *
 * @author little.li
 * @since 2020-06-14 15:14:32
 */
public interface TblDeviceService {


    /**
     * 根据所属电梯编号、设备类型 检查设备是否存在
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     */
    boolean checkDevice(String elevatorCode, String sensorType);

    /**
     * 更新设备信息
     *
     * @param messageJson  消息
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     */
    void updateDevice(JSONObject messageJson, String elevatorCode, String sensorType);

    /**
     * 更新设备在线状态
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @param serverIp     服务器ip
     * @param nowTime      格式化时间
     */
    void updateOnLineStatus(String elevatorCode, String sensorType, String serverIp, String nowTime);


    /**
     * 更新设备离线状态
     *
     * @param elevatorCode 所属电梯编号
     * @param sensorType   设备类型
     */
    void updateOffLineStatus(String elevatorCode, String sensorType, String nowTime);


    void updateDeviceId(String elevatorCode, String sensorType, String id);

    void insertDeviceEventRecord(String elevatorCode, String sensorType, int type, String reason);

    List<TblDevice> getDeviceListByElevatorCode(String elevatorCode);

    /*恢复离线一小时警告*/
    void cancelDeviceTimeOutEvent(String elevatorCode, String sensorType);

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
     * 获取设备信息
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @return 设备信息
     */
    TblDevice getDevice(String elevatorCode, String sensorType);

    /**
     * 获取设备参数配置信息
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @return 设备参数配置信息
     */
    DeviceParamConfDO getDeviceParamConf(String elevatorCode, String sensorType);
}