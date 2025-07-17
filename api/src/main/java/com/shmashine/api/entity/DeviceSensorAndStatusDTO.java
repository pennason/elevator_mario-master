package com.shmashine.api.entity;

import java.io.Serializable;

/**
 * 设备传感器和故障状态返回
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/6/11 15:01
 * @Since: 1.0.0
 */
public class DeviceSensorAndStatusDTO implements Serializable {

    /**
     * 电梯编号
     */
    private String elevatorCode;

    /**
     * 传感器名
     */
    private String sensorName;

    /**
     * 传感器表对应id
     */
    private String sensorConfigId;

    /**
     * 对应故障类型
     */
    private String faultType;

    /**
     * 对应传感器故障类型
     */
    private String sensorFaultType;

    /**
     * 对应设备类型
     */
    private String sensorType;

    /**
     * 是否有该传感器对应的传感器故障
     * 0：正常 1：故障
     */
    private Integer faultStatus = 0;

    public String getElevatorCode() {
        return elevatorCode;
    }

    public void setElevatorCode(String elevatorCode) {
        this.elevatorCode = elevatorCode;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getSensorConfigId() {
        return sensorConfigId;
    }

    public void setSensorConfigId(String sensorConfigId) {
        this.sensorConfigId = sensorConfigId;
    }

    public String getFaultType() {
        return faultType;
    }

    public void setFaultType(String faultType) {
        this.faultType = faultType;
    }

    public String getSensorFaultType() {
        return sensorFaultType;
    }

    public void setSensorFaultType(String sensorFaultType) {
        this.sensorFaultType = sensorFaultType;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public Integer getFaultStatus() {
        return faultStatus;
    }

    public void setFaultStatus(Integer faultStatus) {
        this.faultStatus = faultStatus;
    }
}


