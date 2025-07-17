package com.shmashine.fault.camera.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.fault.camera.entity.TblCamera;

/**
 * TblCameraDao
 */
@Mapper
public interface TblCameraDao {


    String getRtmpUrlByElevatorCode(@Param("elevatorCode") String elevatorCode);

    String getHlsUrlByElevatorCode(@Param("elevatorCode") String elevatorCode);

    TblCamera getByElevatorCode(@Param("elevatorCode") String elevatorCode);

    HashMap<String, String> getHKCameraInfoForTY(@Param("elevatorCode") String elevatorCode);
}