package com.shmashine.api.service.fault.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblFaultDefinition0902Dao;
import com.shmashine.api.dao.TblFaultDefinitionDao;
import com.shmashine.api.module.fault.input.FaultDefinitionModule;
import com.shmashine.api.service.elevator.BizElevatorService;
import com.shmashine.api.service.fault.BizFaultDefinition0902ServiceI;
import com.shmashine.api.service.fault.TblFaultDefinitionServiceI;
import com.shmashine.api.service.system.TblDeviceServiceI;
import com.shmashine.api.service.system.TblFaultShieldServiceI;
import com.shmashine.common.constants.SocketConstants;
import com.shmashine.common.entity.TblDevice;
import com.shmashine.common.entity.TblFaultDefinition;
import com.shmashine.common.entity.TblFaultDefinition0902;

@Service
public class TblFaultDefinitionServiceImpl implements TblFaultDefinitionServiceI {


    @Autowired
    private TblFaultDefinition0902Dao tblFaultDefinition0902Dao;

    @Autowired
    private TblFaultDefinitionDao tblFaultDefinitionDao;

    @Autowired
    private TblDeviceServiceI tblDeviceService;

    @Autowired
    private BizElevatorService elevatorService;

    @Autowired
    private BizFaultDefinition0902ServiceI bizFaultDefinition0902ServiceI;

    @Autowired
    private TblFaultShieldServiceI faultShieldService;


    @Override
    public List<TblFaultDefinition0902> getFaultDefinitionListByElevatorType(Integer elevatorType, Integer sensorType) {
        TblFaultDefinition0902 faultDefinition = new TblFaultDefinition0902();
        faultDefinition.setElevatorType(elevatorType);
        faultDefinition.setSensorType(sensorType);
        return tblFaultDefinition0902Dao.getByEntity(faultDefinition);
    }

    @Override
    public List<TblFaultDefinition> getAllFaultDefinition() {
        return tblFaultDefinitionDao.list();
    }

    @Override
    public List<Map<String, Object>> faultShieldInfo(String elevatorCode) {
        List<TblDevice> deviceList = tblDeviceService.deviceListByElevatorCode(elevatorCode);
        Integer elevatorType = elevatorService.getElevatorTypeByElevatorCode(elevatorCode);
        int eventType = 1;

        for (TblDevice device : deviceList) {
            // 如果是前装设备则获取对应的故障定义
            if (device.getVSensorType().equals(SocketConstants.SENSOR_TYPE_FRONT)) {
                eventType = Integer.parseInt(device.getVPlatformType());
            }
        }
        return faultShieldService.getShieldInfo(elevatorCode, elevatorType, eventType);
    }

    @Override
    public void updateFaultShield(String elevatorCode, List<Map<String, String>> faultShieldInfo) {
        faultShieldService.updateFaultShield(elevatorCode, faultShieldInfo);
    }

    @Override
    public void batchUpdateFaultShield(FaultDefinitionModule faultDefinitionModule) {
        faultShieldService.batchUpdateFaultShield(faultDefinitionModule);
    }


}