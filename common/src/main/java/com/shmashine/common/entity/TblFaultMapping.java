package com.shmashine.common.entity;


import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * 平台故障映射表(TblFaultMapping)实体类
 *
 * @author makejava
 * @since 2020-09-25 11:43:54
 */
public class TblFaultMapping implements Serializable {
    @Serial
    private static final long serialVersionUID = 675434597518338375L;
    /**
     * 故障定义唯一ID
     */
    private Integer id;
    /**
     * 故障来源平台ID
     */
    private String platformCode;
    /**
     * 故障来源平台名称
     */
    private String platformName;
    /**
     * 故障主类型
     */
    private String faultType;
    /**
     * 故障名称
     */
    private String faultName;
    /**
     * 麦信故障类型
     */
    private String mxFaultType;
    /**
     * 麦信故障名称
     */
    private String mxFaultName;
    /**
     * 电梯类型 1 直梯; 2 扶梯
     */
    private Integer elevatorType;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date createTime;
    /**
     * 删除标识 0-未删除，1-已删除
     */
    private Object delFlag;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("platformCode")
    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    @JsonProperty("platformName")
    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    @JsonProperty("faultType")
    public String getFaultType() {
        return faultType;
    }

    public void setFaultType(String faultType) {
        this.faultType = faultType;
    }

    @JsonProperty("faultName")
    public String getFaultName() {
        return faultName;
    }

    public void setFaultName(String faultName) {
        this.faultName = faultName;
    }

    @JsonProperty("mxFaultType")
    public String getMxFaultType() {
        return mxFaultType;
    }

    public void setMxFaultType(String mxFaultType) {
        this.mxFaultType = mxFaultType;
    }

    @JsonProperty("mxFaultName")
    public String getMxFaultName() {
        return mxFaultName;
    }

    public void setMxFaultName(String mxFaultName) {
        this.mxFaultName = mxFaultName;
    }

    @JsonProperty("elevatorType")
    public Integer getElevatorType() {
        return elevatorType;
    }

    public void setElevatorType(Integer elevatorType) {
        this.elevatorType = elevatorType;
    }

    @JsonProperty("createTime")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JsonProperty("delFlag")
    public Object getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Object delFlag) {
        this.delFlag = delFlag;
    }

}