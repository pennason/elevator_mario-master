package com.shmashine.pm.api.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TblInstallingTask implements Serializable {

    private static final long serialVersionUID = 128266188797731823L;

    private String vInstallingTaskId;

    private String vProjectId;

    private String vVillageId;

    private String vPrincipalId;

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

    private Integer iElevatorCount;

    private String vAddress;

    private Integer iNextStep;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dInstallingDate;

    @JsonProperty("vInstallingTaskId")
    public String getvInstallingTaskId() {
        return vInstallingTaskId;
    }

    public void setvInstallingTaskId(String vInstallingTaskId) {
        this.vInstallingTaskId = vInstallingTaskId;
    }

    @JsonProperty("vProjectId")
    public String getvProjectId() {
        return vProjectId;
    }

    public void setvProjectId(String vProjectId) {
        this.vProjectId = vProjectId;
    }

    @JsonProperty("vVillageId")
    public String getvVillageId() {
        return vVillageId;
    }

    public void setvVillageId(String vVillageId) {
        this.vVillageId = vVillageId;
    }

    @JsonProperty("vPrincipalId")
    public String getvPrincipalId() {
        return vPrincipalId;
    }

    public void setvPrincipalId(String vPrincipalId) {
        this.vPrincipalId = vPrincipalId;
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
    public String getvCreateUserId() {
        return vCreateUserId;
    }

    public void setvCreateUserId(String vCreateUserId) {
        this.vCreateUserId = vCreateUserId;
    }

    @JsonProperty("vModifyUserId")
    public String getvModifyUserId() {
        return vModifyUserId;
    }

    public void setvModifyUserId(String vModifyUserId) {
        this.vModifyUserId = vModifyUserId;
    }

    @JsonProperty("iDelFlag")
    public Integer getiDelFlag() {
        return iDelFlag;
    }

    public void setiDelFlag(Integer iDelFlag) {
        this.iDelFlag = iDelFlag;
    }

    @JsonProperty("iStatus")
    public Integer getiStatus() {
        return iStatus;
    }

    public void setiStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    @JsonProperty("iElevatorCount")
    public Integer getiElevatorCount() {
        return iElevatorCount;
    }

    public void setiElevatorCount(Integer iElevatorCount) {
        this.iElevatorCount = iElevatorCount;
    }

    @JsonProperty("vAddress")
    public String getvAddress() {
        return vAddress;
    }

    public void setvAddress(String vAddress) {
        this.vAddress = vAddress;
    }

    @JsonProperty("dInstallingDate")
    public Date getdInstallingDate() {
        return dInstallingDate;
    }

    public void setdInstallingDate(Date dInstallingDate) {
        this.dInstallingDate = dInstallingDate;
    }

    @JsonProperty("iNextStep")
    public Integer getiNextStep() {
        return iNextStep;
    }

    public void setiNextStep(Integer iNextStep) {
        this.iNextStep = iNextStep;
    }
}
