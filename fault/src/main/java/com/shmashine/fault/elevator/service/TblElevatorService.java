package com.shmashine.fault.elevator.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.fault.elevator.entity.TblElevator;
import com.shmashine.fault.user.entity.TblSysUser;

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
    String getRightFloor(String elevatorCode, String deviceFloor);


    /**
     * 更新电梯服务模式
     *
     * @param elevatorCode 电梯编号
     * @param modeStatus   模式
     */
    void updateModeStatus(String elevatorCode, Integer modeStatus);

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

    void updateFloorSettingStatus(Map<String, Object> map);

    /**
     * 根据电梯编号，修改统计数据
     *
     * @param messageJson  统计报文
     * @param elevatorCode 电梯编号
     */
    void updateStatisticalInformationByElevatorCode(JSONObject messageJson, String elevatorCode);

    void updateFaultStatus(String elevatorCode, int i);

    void updateStatisticalInformationByElevatorCode2(JSONObject messageJson, String elevatorCode);

    void updateStatisticalInformationByElevatorCode3(JSONObject messageJson, String elevatorCode);

    /**
     * 拿到对应用户发生故障的电梯
     *
     * @param user 用户信息
     */
    List<String> getFaultElevator(TblSysUser user);

    /**
     * 查询项目甲方name
     *
     * @param vElevatorId 电梯id
     */
    String getClient(String vElevatorId);

    void updateInstallStatus(String elevatorCode, int i);

    /**
     * 更新电梯表设备配置状态
     *
     * @param elevatorCode     电梯编号
     * @param deviceConfStatus 设备配置状态0：未配置 1：已下发 2：已配置
     */
    void updateDeviceConfStatusByCode(String elevatorCode, Integer deviceConfStatus);
}