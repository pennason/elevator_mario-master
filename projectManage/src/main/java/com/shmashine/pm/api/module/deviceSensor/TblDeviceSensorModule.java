package com.shmashine.pm.api.module.deviceSensor;

import java.util.List;

import com.shmashine.pm.api.entity.dto.TblDeviceSensorDto;

public class TblDeviceSensorModule {

    private List<TblDeviceSensorDto> list;

    private List<String> elevatorCodes;

    public List<TblDeviceSensorDto> getList() {
        return list;
    }

    public void setList(List<TblDeviceSensorDto> list) {
        this.list = list;
    }

    public List<String> getElevatorCodes() {
        return elevatorCodes;
    }

    public void setElevatorCodes(List<String> elevatorCodes) {
        this.elevatorCodes = elevatorCodes;
    }
}
