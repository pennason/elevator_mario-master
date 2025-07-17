package com.shmashine.common.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * (TblMonitorDefinition)实体类
 *
 * @author little.li
 * @since 2020-07-02 16:10:22
 */

public class TblMonitorDefinition implements Serializable {
    @Serial
    private static final long serialVersionUID = 423233668482523013L;
    private String vMonitorDefinitionId;
    /**
     * 监控状态类型
     */
    private Integer iMonitorType;
    /**
     * 监控状态名称
     */
    private String vMonitorName;
    /**
     * 监控字段标识
     */
    private String vMonitorField;
    /**
     * 电梯类型 1 直梯; 2 扶梯
     */
    private Integer iElevatorType;
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

    @JsonProperty("vMonitorDefinitionId")
    public String getVMonitorDefinitionId() {
        return vMonitorDefinitionId;
    }

    public void setVMonitorDefinitionId(String vMonitorDefinitionId) {
        this.vMonitorDefinitionId = vMonitorDefinitionId;
    }

    @JsonProperty("iMonitorType")
    public Integer getIMonitorType() {
        return iMonitorType;
    }

    public void setIMonitorType(Integer iMonitorType) {
        this.iMonitorType = iMonitorType;
    }

    @JsonProperty("vMonitorName")
    public String getVMonitorName() {
        return vMonitorName;
    }

    public void setVMonitorName(String vMonitorName) {
        this.vMonitorName = vMonitorName;
    }

    @JsonProperty("vMonitorField")
    public String getVMonitorField() {
        return vMonitorField;
    }

    public void setVMonitorField(String vMonitorField) {
        this.vMonitorField = vMonitorField;
    }

    @JsonProperty("iElevatorType")
    public Integer getIElevatorType() {
        return iElevatorType;
    }

    public void setIElevatorType(Integer iElevatorType) {
        this.iElevatorType = iElevatorType;
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