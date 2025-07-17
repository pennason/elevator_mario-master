package com.shmashine.socket.elevator.service;

import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.socket.elevator.entity.TblElevator;

/**
 * 电梯表(TblElevator)表服务接口
 *
 * @author little.li
 * @since 2020-06-14 15:17:40
 */
public interface TblElevatorService {


    /**
     * 获取正确的楼层信息
     *
     * @param elevatorCode 电梯编号
     * @param deviceFloor  设备楼层
     */
    String getRightFloor(String elevatorCode, String deviceFloor, JSONObject messageJson);


    /**
     * 更新电梯服务模式
     *
     * @param elevatorCode 电梯编号
     * @param modeStatus   服务模式
     */
    void updateModeStatus(String elevatorCode, String modeStatus);

    /**
     * 更新在线离线状态
     *
     * @param elevatorCode 电梯编号
     * @param i            在线状态
     */
    void updateOnlineStatus(String elevatorCode, int i);

    List<TblElevator> list();

    void updateElevatorId(String elevatorCode, long nextId);

    TblElevator getByElevatorCode(String elevatorCode);

    void updateFloorSettingStatus(String elevatorCode, String settingFloorStatus);

    void updateFaultStatus(String elevatorCode, int i);

    //获取所有电梯及设备状态
    List<HashMap<String, Object>> getAllNettyDeviceStatus();

    /**
     * 更新设备状态
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @param status       设备状态
     */
    void changeDeviceStatus(String elevatorCode, String sensorType, int status);

    /**
     * 更新电梯状态
     *
     * @param elevatorCode 电梯编号
     * @param status       电梯状态
     */
    void changeElevatorStatus(String elevatorCode, int status);

    /**
     * 获取电梯是否开启检测人数
     *
     * @param elevatorCode 电梯编号
     */
    boolean getDetectedPeopleNumsIsOpen(String elevatorCode);
}