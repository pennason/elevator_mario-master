package com.shmashine.api.controller.device.vo;

import java.io.Serializable;

/**
 * 设备传感器和故障状态返回
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/6/11 15:01
 * @Since: 1.0.0
 */
public class DeviceSensorAndStatusRespVO implements Serializable {

    /**
     * 传感器名
     */
    private String sensorName;

    /**
     * 传感器表对应id
     */
    private String sensorConfigId;

    /**
     * 是否有该传感器对应的传感器故障
     * 0：正常 1：故障
     */
    private Integer faultStatus = 0;

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

    public Integer getFaultStatus() {
        return faultStatus;
    }

    public void setFaultStatus(Integer faultStatus) {
        this.faultStatus = faultStatus;
    }
}


