package com.shmashine.api.module.fault.output;

import java.io.Serializable;

/**
 * 导出不文明行为列表
 *
 * @author depp
 */
public class EventElevatorDownloadModuleMap implements Serializable {
    private String elevatorCode;

    private String sensorTypeName;

    private String typeName;

    private String reason;

    private String happenTime;

    public String getElevatorCode() {
        return elevatorCode;
    }

    public void setElevatorCode(String elevatorCode) {
        this.elevatorCode = elevatorCode;
    }

    public String getSensorTypeName() {
        return sensorTypeName;
    }

    public void setSensorTypeName(String sensorTypeName) {
        this.sensorTypeName = sensorTypeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getHappenTime() {
        return happenTime;
    }

    public void setHappenTime(String happenTime) {
        this.happenTime = happenTime;
    }
}
