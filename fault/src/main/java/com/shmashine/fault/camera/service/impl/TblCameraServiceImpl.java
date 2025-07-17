package com.shmashine.fault.camera.service.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.fault.camera.dao.TblCameraDao;
import com.shmashine.fault.camera.entity.TblCamera;
import com.shmashine.fault.camera.service.TblCameraServiceI;

/**
 * TblCameraServiceImpl
 */
@Service
public class TblCameraServiceImpl implements TblCameraServiceI {

    @Resource(type = TblCameraDao.class)
    private TblCameraDao tblCameraDao;

    @Override
    public String getRtmpUrlByElevatorCode(String elevatorCode) {
        return tblCameraDao.getRtmpUrlByElevatorCode(elevatorCode);
    }

    @Override
    public String getHlsUrlByElevatorCode(String elevatorCode) {
        return tblCameraDao.getHlsUrlByElevatorCode(elevatorCode);
    }

    @Override
    public TblCamera getByElevatorCode(String elevatorCode) {
        return tblCameraDao.getByElevatorCode(elevatorCode);
    }

    @Override
    public HashMap<String, String> getHKCameraInfoForTY(String elevatorCode) {
        return tblCameraDao.getHKCameraInfoForTY(elevatorCode);
    }


}