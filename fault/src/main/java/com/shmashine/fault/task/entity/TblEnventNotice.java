package com.shmashine.fault.task.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 模式切换记录
 *
 * @author makejava
 * @since 2020-07-09 18:42:59
 */
public class TblEnventNotice implements Serializable {
    private static final long serialVersionUID = -1L;
    /**
     * ID
     */
    private String vEnventNotifyId;
    /**
     * 电梯ID
     */
    private String vElevatorId;
    /**
     * 电梯编号
     */
    private String vElevatorCode;
    /**
     * 电梯注册码
     */
    private String vRegisterNo;
    /**
     * 通知类型：1.提醒事件、2.故障频发、3.关注电梯
     */
    private String vNoticeType;
    /**
     * 通知的信息内容
     */
    private String vMessage;
    /**
     * 是否删除，0 未删除，1 已删除
     */
    private Integer iIsDelete;
    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtStartTime;
    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtEndTime;

    @JsonProperty("vEnventNotifyId")
    public String getVEnventNotifyId() {
        return vEnventNotifyId;
    }

    public void setVEnventNotifyId(String vEnventNotifyId) {
        this.vEnventNotifyId = vEnventNotifyId;
    }

    @JsonProperty("vElevatorId")
    public String getVElevatorId() {
        return vElevatorId;
    }

    public void setVElevatorId(String vElevatorId) {
        this.vElevatorId = vElevatorId;
    }

    @JsonProperty("vElevatorCode")
    public String getVElevatorCode() {
        return vElevatorCode;
    }

    public void setVElevatorCode(String vElevatorCode) {
        this.vElevatorCode = vElevatorCode;
    }

    @JsonProperty("vRegisterNo")
    public String getVRegisterNo() {
        return vRegisterNo;
    }

    public void setVRegisterNo(String vRegisterNo) {
        this.vRegisterNo = vRegisterNo;
    }

    @JsonProperty("vNoticeType")
    public String getVNoticeType() {
        return vNoticeType;
    }

    public void setVNoticeType(String vNoticeType) {
        this.vNoticeType = vNoticeType;
    }

    @JsonProperty("vMessage")
    public String getVMessage() {
        return vMessage;
    }

    public void setVMessage(String vMessage) {
        this.vMessage = vMessage;
    }

    @JsonProperty("iIsDelete")
    public Integer getIIsDelete() {
        return iIsDelete;
    }

    public void setIIsDelete(Integer iIsDelete) {
        this.iIsDelete = iIsDelete;
    }

    @JsonProperty("dtStartTime")
    public Date getDtStartTime() {
        return dtStartTime;
    }

    public void setDtStartTime(Date dtStartTime) {
        this.dtStartTime = dtStartTime;
    }

    @JsonProperty("dtEndTime")
    public Date getDtEndTime() {
        return dtEndTime;
    }

    public void setDtEndTime(Date dtEndTime) {
        this.dtEndTime = dtEndTime;
    }

}