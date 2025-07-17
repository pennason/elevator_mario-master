package com.shmashine.common.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 维保内容定义表(TblMaintenanceDefinition)实体类
 *
 * @author little.li
 * @since 2020-07-13 15:14:50
 */
public class TblMaintenanceDefinition implements Serializable {
    @Serial
    private static final long serialVersionUID = -14617878250423721L;
    /**
     * 维保项定义id
     */
    private String vMaintenanceDefinitionId;
    /**
     * 维保项目（名称）
     */
    private String vMaintenanceName;
    /**
     * 维保基本要求
     */
    private String vMaintenanceClaim;
    /**
     * 维保类型
     */
    private Integer iMaintenanceType;
    /**
     * 维保周期
     */
    private Integer iMaintenanceTime;
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
    private Integer vCreateUserId;
    /**
     * 修改记录用户
     */
    private Integer vModifyUserId;
    /**
     * 删除标识 0-未删除，1-已删除
     */
    private Integer iDelFlag;

    @JsonProperty("vMaintenanceDefinitionId")
    public String getVMaintenanceDefinitionId() {
        return vMaintenanceDefinitionId;
    }

    public void setVMaintenanceDefinitionId(String vMaintenanceDefinitionId) {
        this.vMaintenanceDefinitionId = vMaintenanceDefinitionId;
    }

    @JsonProperty("vMaintenanceName")
    public String getVMaintenanceName() {
        return vMaintenanceName;
    }

    public void setVMaintenanceName(String vMaintenanceName) {
        this.vMaintenanceName = vMaintenanceName;
    }

    @JsonProperty("vMaintenanceClaim")
    public String getVMaintenanceClaim() {
        return vMaintenanceClaim;
    }

    public void setVMaintenanceClaim(String vMaintenanceClaim) {
        this.vMaintenanceClaim = vMaintenanceClaim;
    }

    @JsonProperty("iMaintenanceType")
    public Integer getIMaintenanceType() {
        return iMaintenanceType;
    }

    public void setIMaintenanceType(Integer iMaintenanceType) {
        this.iMaintenanceType = iMaintenanceType;
    }

    @JsonProperty("iMaintenanceTime")
    public Integer getIMaintenanceTime() {
        return iMaintenanceTime;
    }

    public void setIMaintenanceTime(Integer iMaintenanceTime) {
        this.iMaintenanceTime = iMaintenanceTime;
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
    public Integer getVCreateUserId() {
        return vCreateUserId;
    }

    public void setVCreateUserId(Integer vCreateUserId) {
        this.vCreateUserId = vCreateUserId;
    }

    @JsonProperty("vModifyUserId")
    public Integer getVModifyUserId() {
        return vModifyUserId;
    }

    public void setVModifyUserId(Integer vModifyUserId) {
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