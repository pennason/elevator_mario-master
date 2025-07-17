package com.shmashine.pm.api.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TblSysDeptLogo implements Serializable {

    private static final long serialVersionUID = -5689164932453022171L;

    private String vLogoId;
    /**
     * 部门Id
     */
    private String vDeptId;
    /**
     * 系统标题
     */
    private String vSystemTitle;
    /**
     * 备注
     */
    private String vRemark;
    /**
     * 系统后台服务
     */
    private String vServerUrl;
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
     * url
     */
    private String vLogFileUrl;

    @JsonProperty("vLogoId")
    public String getVLogoId() {
        return vLogoId;
    }

    public void setVLogoId(String vLogoId) {
        this.vLogoId = vLogoId;
    }

    @JsonProperty("vDeptId")
    public String getVDeptId() {
        return vDeptId;
    }

    public void setVDeptId(String vDeptId) {
        this.vDeptId = vDeptId;
    }

    @JsonProperty("vSystemTitle")
    public String getVSystemTitle() {
        return vSystemTitle;
    }

    public void setVSystemTitle(String vSystemTitle) {
        this.vSystemTitle = vSystemTitle;
    }

    @JsonProperty("vRemark")
    public String getVRemark() {
        return vRemark;
    }

    public void setVRemark(String vRemark) {
        this.vRemark = vRemark;
    }

    @JsonProperty("vServerUrl")
    public String getVServerUrl() {
        return vServerUrl;
    }

    public void setVServerUrl(String vServerUrl) {
        this.vServerUrl = vServerUrl;
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

    @JsonProperty("vLogFileUrl")
    public String getVLogFileUrl() {
        return vLogFileUrl;
    }

    public void setVLogFileUrl(String vLogFileUrl) {
        this.vLogFileUrl = vLogFileUrl;
    }
}
