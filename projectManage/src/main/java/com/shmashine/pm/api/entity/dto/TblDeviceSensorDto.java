package com.shmashine.pm.api.entity.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TblDeviceSensorDto {

    private String vDeviceSensorId;

    private String vDeviceId;

    private String vElevatorId;

    private String vElevatorCode;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtCreateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtModifyTime;

    private String vCreateUserId;

    private String vModifyUserId;

    private Boolean iDelFlag;

    private String vSensorConfigId;

    private Integer iSensorChose;

    private String eType;

    private String vSensorType;

    public String getvDeviceSensorId() {
        return vDeviceSensorId;
    }

    public void setvDeviceSensorId(String vDeviceSensorId) {
        this.vDeviceSensorId = vDeviceSensorId;
    }

    public String getvDeviceId() {
        return vDeviceId;
    }

    public void setvDeviceId(String vDeviceId) {
        this.vDeviceId = vDeviceId;
    }

    public String getvElevatorId() {
        return vElevatorId;
    }

    public void setvElevatorId(String vElevatorId) {
        this.vElevatorId = vElevatorId;
    }

    public String getvElevatorCode() {
        return vElevatorCode;
    }

    public void setvElevatorCode(String vElevatorCode) {
        this.vElevatorCode = vElevatorCode;
    }

    public Date getDtCreateTime() {
        return dtCreateTime;
    }

    public void setDtCreateTime(Date dtCreateTime) {
        this.dtCreateTime = dtCreateTime;
    }

    public Date getDtModifyTime() {
        return dtModifyTime;
    }

    public void setDtModifyTime(Date dtModifyTime) {
        this.dtModifyTime = dtModifyTime;
    }

    public String getvCreateUserId() {
        return vCreateUserId;
    }

    public void setvCreateUserId(String vCreateUserId) {
        this.vCreateUserId = vCreateUserId;
    }

    public String getvModifyUserId() {
        return vModifyUserId;
    }

    public void setvModifyUserId(String vModifyUserId) {
        this.vModifyUserId = vModifyUserId;
    }

    public Boolean getiDelFlag() {
        return iDelFlag;
    }

    public void setiDelFlag(Boolean iDelFlag) {
        this.iDelFlag = iDelFlag;
    }

    public String getvSensorConfigId() {
        return vSensorConfigId;
    }

    public void setvSensorConfigId(String vSensorConfigId) {
        this.vSensorConfigId = vSensorConfigId;
    }

    public Integer getiSensorChose() {
        return iSensorChose;
    }

    public void setiSensorChose(Integer iSensorChose) {
        this.iSensorChose = iSensorChose;
    }

    public String geteType() {
        return eType;
    }

    public void seteType(String eType) {
        this.eType = eType;
    }

    public String getvSensorType() {
        return vSensorType;
    }

    public void setvSensorType(String vSensorType) {
        this.vSensorType = vSensorType;
    }

    @Override
    public String toString() {
        return "TblDeviceSensorDto{" +
                "vDeviceSensorId='" + vDeviceSensorId + '\'' +
                ", vDeviceId='" + vDeviceId + '\'' +
                ", vElevatorId='" + vElevatorId + '\'' +
                ", vElevatorCode='" + vElevatorCode + '\'' +
                ", dtCreateTime=" + dtCreateTime +
                ", dtModifyTime=" + dtModifyTime +
                ", vCreateUserId='" + vCreateUserId + '\'' +
                ", vModifyUserId='" + vModifyUserId + '\'' +
                ", iDelFlag=" + iDelFlag +
                ", vSensorConfigId='" + vSensorConfigId + '\'' +
                ", iSensorChose=" + iSensorChose +
                ", eType='" + eType + '\'' +
                ", vSensorType='" + vSensorType + '\'' +
                '}';
    }
}
