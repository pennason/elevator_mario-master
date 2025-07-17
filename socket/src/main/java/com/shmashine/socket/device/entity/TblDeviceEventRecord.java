package com.shmashine.socket.device.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 设备上下线事件记录表(TblDeviceEventRecord)实体类
 *
 * @author little.li
 * @since 2020-06-15 11:01:55
 */
public class TblDeviceEventRecord implements Serializable {


    private static final long serialVersionUID = -27842568843858608L;

    /**
     * 设备事件记录ID
     */
    private String vDeviceEventRecordId;
    /**
     * 所属电梯编号
     */
    private String vElevatorCode;
    /**
     * 设备类型
     */
    private String vSensorType;
    /**
     * 事件类型 1 上线，2 离线
     */
    private Integer iType;
    /**
     * 设备上报的重连原因
     */
    private String vReason;
    /**
     * 事件发生时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtHappenTime;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtCreateTime;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtModifyTime;
    /**
     * 创建记录用户
     */
    private String vCreateUserId;
    /**
     * 修改记录用户
     */
    private String vModifyUserId;
    /**
     * 删除标识 0-未删除，1-已删除
     */
    private Integer iDelFlag;

    @JsonProperty("vDeviceEventRecordId")
    public String getVDeviceEventRecordId() {
        return vDeviceEventRecordId;
    }

    public void setVDeviceEventRecordId(String vDeviceEventRecordId) {
        this.vDeviceEventRecordId = vDeviceEventRecordId;
    }

    @JsonProperty("vElevatorCode")
    public String getVElevatorCode() {
        return vElevatorCode;
    }

    public void setVElevatorCode(String vElevatorCode) {
        this.vElevatorCode = vElevatorCode;
    }

    @JsonProperty("vSensorType")
    public String getVSensorType() {
        return vSensorType;
    }

    public void setVSensorType(String vSensorType) {
        this.vSensorType = vSensorType;
    }

    @JsonProperty("iType")
    public Integer getIType() {
        return iType;
    }

    public void setIType(Integer iType) {
        this.iType = iType;
    }

    @JsonProperty("vReason")
    public String getVReason() {
        return vReason;
    }

    public void setVReason(String vReason) {
        this.vReason = vReason;
    }

    @JsonProperty("dtHappenTime")
    public Date getDtHappenTime() {
        return dtHappenTime;
    }

    public void setDtHappenTime(Date dtHappenTime) {
        this.dtHappenTime = dtHappenTime;
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
    public String getVCreateUserId() {
        return vCreateUserId;
    }

    public void setVCreateUserId(String vCreateUserId) {
        this.vCreateUserId = vCreateUserId;
    }

    @JsonProperty("vModifyUserId")
    public String getVModifyUserId() {
        return vModifyUserId;
    }

    public void setVModifyUserId(String vModifyUserId) {
        this.vModifyUserId = vModifyUserId;
    }

    @JsonProperty("iDelFlag")
    public Integer getIDelFlag() {
        return iDelFlag;
    }

    public void setIDelFlag(Integer iDelFlag) {
        this.iDelFlag = iDelFlag;
    }

}