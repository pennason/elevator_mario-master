package com.shmashine.fault.camera.service;

import java.util.HashMap;

import com.shmashine.fault.camera.entity.TblCamera;

/**
 * TblCameraServiceI
 */
public interface TblCameraServiceI {

    String getRtmpUrlByElevatorCode(String elevatorCode);

    String getHlsUrlByElevatorCode(String elevatorCode);

    TblCamera getByElevatorCode(String elevatorCode);

    HashMap<String, String> getHKCameraInfoForTY(String elevatorCode);
}