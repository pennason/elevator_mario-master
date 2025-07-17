package com.shmashine.api.module.elevator;


import lombok.Data;

@Data
public class ElevatorBatchAddModule {

    /**
     * 所属项目
     */
    private String vProjectId;

    /**
     * 时间
     */
    private String dtInstallTime;

    /**
     * 电梯编码
     */
    private String vElevatorCode;

    /**
     * 设备标识：1：前装设备；null：后装设备
     */
    private String deviceMark;

    public String getvProjectId() {
        return vProjectId;
    }

    public void setvProjectId(String vProjectId) {
        this.vProjectId = vProjectId;
    }

    public String getDtInstallTime() {
        return dtInstallTime;
    }

    public void setDtInstallTime(String dtInstallTime) {
        this.dtInstallTime = dtInstallTime;
    }

    public String getvElevatorCode() {
        return vElevatorCode;
    }

    public void setvElevatorCode(String vElevatorCode) {
        this.vElevatorCode = vElevatorCode;
    }

    public String getDeviceMark() {
        return deviceMark;
    }

    public void setDeviceMark(String deviceMark) {
        this.deviceMark = deviceMark;
    }
}