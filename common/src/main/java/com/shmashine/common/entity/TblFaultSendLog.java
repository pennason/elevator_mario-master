package com.shmashine.common.entity;


import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * 故障推送第三方平台日志表(TblFaultSendLog)实体类
 *
 * @author makejava
 * @since 2020-09-25 17:21:11
 */
public class TblFaultSendLog implements Serializable {
    @Serial
    private static final long serialVersionUID = 910336493392080975L;
    private Integer id;
    /**
     * 故障ID
     */
    private String faultId;
    /**
     * 故障类型
     */
    private String faultType;
    /**
     * 电梯编号
     */
    private String elevatorCode;
    /**
     * 电梯注册码
     */
    private String registerNo;
    /**
     * 推送的平台
     */
    private String ptCodes;
    /**
     * 日志类型（0：应推送，1：已经推送）
     */
    private String logType;
    /**
     * 原始报文
     */
    private String faultMessage;
    /**
     * 推送报文
     */
    private String sendMessage;
    /**
     * 响应报文
     */
    private String responseMessage;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date createTime;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("faultId")
    public String getFaultId() {
        return faultId;
    }

    public void setFaultId(String faultId) {
        this.faultId = faultId;
    }

    @JsonProperty("faultType")
    public String getFaultType() {
        return faultType;
    }

    public void setFaultType(String faultType) {
        this.faultType = faultType;
    }

    @JsonProperty("elevatorCode")
    public String getElevatorCode() {
        return elevatorCode;
    }

    public void setElevatorCode(String elevatorCode) {
        this.elevatorCode = elevatorCode;
    }

    @JsonProperty("registerNo")
    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    @JsonProperty("ptCodes")
    public String getPtCodes() {
        return ptCodes;
    }

    public void setPtCodes(String ptCodes) {
        this.ptCodes = ptCodes;
    }

    @JsonProperty("logType")
    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    @JsonProperty("faultMessage")
    public String getFaultMessage() {
        return faultMessage;
    }

    public void setFaultMessage(String faultMessage) {
        this.faultMessage = faultMessage;
    }

    @JsonProperty("sendMessage")
    public String getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(String sendMessage) {
        this.sendMessage = sendMessage;
    }

    @JsonProperty("responseMessage")
    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @JsonProperty("createTime")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}