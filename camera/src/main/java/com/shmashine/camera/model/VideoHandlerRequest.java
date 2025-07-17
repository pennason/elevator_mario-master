package com.shmashine.camera.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 录制视频请求封装
 *
 * @author Dean Winchester
 */
public class VideoHandlerRequest {
    private String elevatorCode;
    private String stype;
    private String faultId;
    /**
     * 不文明行为标识，0：故障，1：不文明行为
     */
    private Integer uncivilizedBehaviorFlag;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date endTime;

    public Integer getUncivilizedBehaviorFlag() {
        return uncivilizedBehaviorFlag;
    }

    public void setUncivilizedBehaviorFlag(Integer uncivilizedBehaviorFlag) {
        this.uncivilizedBehaviorFlag = uncivilizedBehaviorFlag;
    }

    public String getElevatorCode() {
        return elevatorCode;
    }

    public void setElevatorCode(String elevatorCode) {
        this.elevatorCode = elevatorCode;
    }

    public String getStype() {
        return stype;
    }

    public void setStype(String stype) {
        this.stype = stype;
    }

    public String getFaultId() {
        return faultId;
    }

    public void setFaultId(String faultId) {
        this.faultId = faultId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
