package com.shmashine.api.module.dept.input;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 编辑部门获取参数
 */
public class EditDeptLogModule {
    @NotNull(message = "请输入部门Id")
    /**部门Id*/
    private String vDeptId;
    @NotNull(message = "请输入系统标题")
    /**系统标题*/
    private String vSystemTitle;
    /**
     * 备注
     */
    private String vRemark;
    @NotNull(message = "请输入系统后台服务地址")
    /**系统后台服务*/
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
}
