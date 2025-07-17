package com.shmashine.pm.api.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TblProject implements Serializable {

    private static final long serialVersionUID = 1954448502944942726L;

    /**
     * 项目id
     */
    private String vProjectId;
    /**
     * 项目名称
     */
    private String vProjectName;
    /**
     * 部门ID
     */
    private String vDeptId;
    /**
     * 部门ID
     */
    private String vDeptIdName;
    /**
     * 项目类型-对照systeminfo表10035字典值
     */
    private Integer projectType;
    /**
     * 备注
     */
    private String vRemarks;
    /**
     * 甲方
     */
    private String vPartyA;
    /**
     * 甲方联系人
     */
    private String vPartyContactsName;
    /**
     * 甲方联系电话
     */
    private String vPartyContactsTel;
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

    private Integer iStatus;

    /**
     * 计划开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dStartTime;

    /**
     * 计划结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dEndTime;

    private Integer iVillageCount;

    @JsonProperty("vDeptIdName")
    public String getVDeptIdName() {
        return vDeptIdName;
    }

    public void setVDeptIdName(String vDeptIdName) {
        this.vDeptIdName = vDeptIdName;
    }

    @JsonProperty("vProjectId")
    public String getVProjectId() {
        return vProjectId;
    }

    public void setVProjectId(String vProjectId) {
        this.vProjectId = vProjectId;
    }

    @JsonProperty("vProjectName")
    public String getVProjectName() {
        return vProjectName;
    }

    public void setVProjectName(String vProjectName) {
        this.vProjectName = vProjectName;
    }

    @JsonProperty("vDeptId")
    public String getVDeptId() {
        return vDeptId;
    }

    public void setVDeptId(String vDeptId) {
        this.vDeptId = vDeptId;
    }

    public Integer getProjectType() {
        return projectType;
    }

    public void setProjectType(Integer projectType) {
        this.projectType = projectType;
    }

    @JsonProperty("vRemarks")
    public String getVRemarks() {
        return vRemarks;
    }

    public void setVRemarks(String vRemarks) {
        this.vRemarks = vRemarks;
    }

    @JsonProperty("vPartyA")
    public String getVPartyA() {
        return vPartyA;
    }

    public void setVPartyA(String vPartyA) {
        this.vPartyA = vPartyA;
    }

    @JsonProperty("vPartyContactsName")
    public String getVPartyContactsName() {
        return vPartyContactsName;
    }

    public void setVPartyContactsName(String vPartyContactsName) {
        this.vPartyContactsName = vPartyContactsName;
    }

    @JsonProperty("vPartyContactsTel")
    public String getVPartyContactsTel() {
        return vPartyContactsTel;
    }

    public void setVPartyContactsTel(String vPartyContactsTel) {
        this.vPartyContactsTel = vPartyContactsTel;
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

    @JsonProperty("iStatus")
    public Integer getiStatus() {
        return iStatus;
    }

    public void setiStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    @JsonProperty("dStartTime")
    public Date getdStartTime() {
        return dStartTime;
    }

    public void setdStartTime(Date dStartTime) {
        this.dStartTime = dStartTime;
    }

    @JsonProperty("dEndTime")
    public Date getdEndTime() {
        return dEndTime;
    }

    public void setdEndTime(Date dEndTime) {
        this.dEndTime = dEndTime;
    }

    public Integer getiVillageCount() {
        return iVillageCount;
    }

    public void setiVillageCount(Integer iVillageCount) {
        this.iVillageCount = iVillageCount;
    }
}
