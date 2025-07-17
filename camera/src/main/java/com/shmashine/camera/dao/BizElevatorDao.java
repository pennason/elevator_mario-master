package com.shmashine.camera.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.camera.model.elevator.ElevatorDetailModule;
import com.shmashine.camera.model.elevator.TblElevator;

/**
 * BizElevatorDao
 *
 * @author Dean Winchester
 */
@Mapper
public interface BizElevatorDao {
    /**
     * 通过注册编号查询详情
     *
     * @param equipmentCode
     * @return
     */
    ElevatorDetailModule getElevatorByEquipmentCode(String equipmentCode);

    TblElevator getElevatorByCode(String elevatorCode);

    TblElevator getElevatorBindingByCodeAndCameraId(@Param("elevatorCode") String elevatorCode, @Param("cameraId") String cameraId);

    //获取待确认故障类型
    String getFaultTempTypeByFaultId(String faultId);

    //获取故障类型
    String getFaultTypeByFaultId(String faultId);
}
