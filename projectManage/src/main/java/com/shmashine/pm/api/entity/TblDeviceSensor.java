package com.shmashine.pm.api.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TblDeviceSensor implements Serializable {

    private static final long serialVersionUID = 1L;

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

    private String vSensorType;

    @JsonProperty("vDeviceSensorId")
    public String getvDeviceSensorId() {
        return vDeviceSensorId;
    }

    public void setvDeviceSensorId(String vDeviceSensorId) {
        this.vDeviceSensorId = vDeviceSensorId;
    }

    @JsonProperty("vDeviceId")
    public String getvDeviceId() {
        return vDeviceId;
    }

    public void setvDeviceId(String vDeviceId) {
        this.vDeviceId = vDeviceId;
    }

    @JsonProperty("vElevatorId")
    public String getvElevatorId() {
        return vElevatorId;
    }

    public void setvElevatorId(String vElevatorId) {
        this.vElevatorId = vElevatorId;
    }

    @JsonProperty("vElevatorCode")
    public String getvElevatorCode() {
        return vElevatorCode;
    }

    public void setvElevatorCode(String vElevatorCode) {
        this.vElevatorCode = vElevatorCode;
    }

    @JsonProperty("dtCreateTime")
    public Date getDtCreateTime() {
        return dtCreateTime;
    }

    public void setDtCreateTime(Date dtCreateTime) {
        this.dtCreateTime = dtCreateTime;
    }

    @JsonProperty("dtModifyTime")
    public Date getDtModifyTime() {
        return dtModifyTime;
    }

    public void setDtModifyTime(Date dtModifyTime) {
        this.dtModifyTime = dtModifyTime;
    }

    @JsonProperty("vCreateUserId")
    public String getvCreateUserId() {
        return vCreateUserId;
    }

    public void setvCreateUserId(String vCreateUserId) {
        this.vCreateUserId = vCreateUserId;
    }

    @JsonProperty("vModifyUserId")
    public String getvModifyUserId() {
        return vModifyUserId;
    }

    public void setvModifyUserId(String vModifyUserId) {
        this.vModifyUserId = vModifyUserId;
    }

    @JsonProperty("iDelFlag")
    public Boolean getiDelFlag() {
        return iDelFlag;
    }

    public void setiDelFlag(Boolean iDelFlag) {
        this.iDelFlag = iDelFlag;
    }

    @JsonProperty("vSensorConfigId")
    public String getvSensorConfigId() {
        return vSensorConfigId;
    }

    public void setvSensorConfigId(String vSensorConfigId) {
        this.vSensorConfigId = vSensorConfigId;
    }

    @JsonProperty("iSensorChose")
    public Integer getiSensorChose() {
        return iSensorChose;
    }

    public void setiSensorChose(Integer iSensorChose) {
        this.iSensorChose = iSensorChose;
    }

    public String getvSensorType() {
        return vSensorType;
    }

    public void setvSensorType(String vSensorType) {
        this.vSensorType = vSensorType;
    }

    @Override
    public String toString() {
        return "TblDeviceSensor{" +
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
                ", vSensorType='" + vSensorType + '\'' +
                ", iSensorChose=" + iSensorChose +
                '}';
    }
}