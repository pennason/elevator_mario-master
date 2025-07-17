package com.shmashine.socket.elevator.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.socket.elevator.entity.TblElevator;

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
    void updateModeStatus(@Param("elevatorCode") String elevatorCode, @Param("modeStatus") String modeStatus);


    /**
     * 更新在线状态
     *
     * @param elevatorCode 电梯编号
     * @param onLine       在线状态
     */
    void updateOnlineStatus(@Param("elevatorCode") String elevatorCode, @Param("onLine") int onLine);

    List<TblElevator> list();

    void updateElevatorId(@Param("elevatorCode") String elevatorCode, @Param("nextId") String nextId);

    void updateFloorSettingStatus(@Param("elevatorCode") String elevatorCode,
                                  @Param("settingFloorStatus") String settingFloorStatus);

    void updateFaultStatus(@Param("elevatorCode") String elevatorCode, @Param("status") int status);

    /**
     * 获取所有电梯设备状态
     */
    List<HashMap<String, Object>> getAllNettyDeviceStatus();

    /**
     * 更新设备在线状态
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @param status       在线状态
     */
    void changeDeviceStatus(@Param("elevatorCode") String elevatorCode,
                            @Param("sensorType") String sensorType, @Param("status") int status);

    /**
     * 更新电梯在线状态
     *
     * @param elevatorCode 电梯编号
     * @param status       在线状态
     */
    void changeElevatorStatus(@Param("elevatorCode") String elevatorCode, @Param("status") int status);

    /**
     * 获取电梯是否开启检测人数
     *
     * @param elevatorCode 电梯编号
     */
    Boolean getDetectedPeopleNumsIsOpen(String elevatorCode);
}