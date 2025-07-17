package com.shmashine.fault.elevator.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.entity.DeviceMaintenanceOrder;
import com.shmashine.fault.elevator.entity.TblElevator;

/**
 * 电梯表(TblElevator)表数据库访问层
 *
 * @author little.li
 * @since 2020-06-14 15:17:39
 */
@Mapper
public interface TblElevatorDao {


    /**
     * 根据电梯编号获取电梯
     *
     * @param elevatorCode 电梯编号
     */
    TblElevator getByElevatorCode(String elevatorCode);


    /**
     * 更新电梯服务模式
     *
     * @param elevatorCode 电梯编号
     * @param modeStatus   模式
     */
    void updateModeStatus(@Param("elevatorCode") String elevatorCode, @Param("modeStatus") Integer modeStatus);


    /**
     * 更新在线状态
     *
     * @param elevatorCode 电梯编号
     * @param onLine       在线状态
     */
    void updateOnlineStatus(@Param("elevatorCode") String elevatorCode, @Param("onLine") int onLine);

    List<TblElevator> list();

    void updateElevatorId(@Param("elevatorCode") String elevatorCode, @Param("nextId") String nextId);

    void updateFloorSettingStatus(Map<String, Object> map);


    /**
     * 根据电梯编号，修改统计数据
     *
     * @param messageJson  统计报文
     * @param elevatorCode 电梯编号
     */
    void updateStatisticalInformationByElevatorCode(@Param("messageJson") JSONObject messageJson,
                                                    @Param("elevatorCode") String elevatorCode);

    void updateFaultStatus(@Param("elevatorCode") String elevatorCode, @Param("status") int status);

    void updateStatisticalInformationByElevatorCode2(@Param("messageJson") JSONObject messageJson,
                                                     @Param("elevatorCode") String elevatorCode);

    void updateStatisticalInformationByElevatorCode3(@Param("messageJson") JSONObject messageJson,
                                                     @Param("elevatorCode") String elevatorCode);

    /**
     * 拿到该用户前一天发生故障的西子电梯
     *
     * @param userId 用户id
     */
    List<String> getFaultElevator(@Param("userId") String userId);

    /**
     * 查询电梯项目甲方
     *
     * @param vElevatorId 电梯id
     */
    String getClient(@Param("vElevatorId") String vElevatorId);

    /**
     * 获取设备运维单所需信息
     *
     * @param elevatorCode 电梯编号
     */
    DeviceMaintenanceOrder getProjectAndVillageByElevatorCode(String elevatorCode);

    /**
     * 更新电梯安装状态
     *
     * @param elevatorCode 电梯编号
     */
    void updateInstallStatus(@Param("elevatorCode") String elevatorCode, @Param("status") int status);

    /**
     * 更新电梯表设备配置状态
     *
     * @param elevatorCode     电梯编号
     * @param deviceConfStatus 设备配置状态0：未配置 1：已下发 2：已配置
     */
    void updateDeviceConfStatusByCode(@Param("elevatorCode") String elevatorCode,
                                      @Param("status") Integer deviceConfStatus);

}