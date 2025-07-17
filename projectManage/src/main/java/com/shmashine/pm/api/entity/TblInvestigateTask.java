package com.shmashine.pm.api.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shmashine.pm.api.enums.TblInvestigateTaskStatusEnum;

/**
 * 现勘任务
 */
public class TblInvestigateTask implements Serializable {

    private static final long serialVersionUID = -5402254036897436471L;

    private String vInvestigateTaskId;

    private String vProjectId;

    private String vVillageId;

    private String vAddress;

    private String vPrincipalId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dInvestigateDate;

    private Integer iElevatorCount;

    private Integer iStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtCreateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtModifyTime;

    private String vCreateUserId;

    private String vModifyUserId;

    private int iDelFlag;

    private String vTemplateFileUrl;

    private Integer iNextStep;

    @JsonProperty("vInvestigateTaskId")
    public String getvInvestigateTaskId() {
        return vInvestigateTaskId;
    }

    public void setvInvestigateTaskId(String vInvestigateTaskId) {
        this.vInvestigateTaskId = vInvestigateTaskId;
    }

    @JsonProperty("vProjectId")
    public String getvProjectId() {
        return vProjectId;
    }

    public void setvProjectId(String vProjectId) {
        this.vProjectId = vProjectId;
    }

    @JsonProperty("vAddress")
    public String getvAddress() {
        return vAddress;
    }

    public void setvAddress(String vAddress) {
        this.vAddress = vAddress;
    }

    @JsonProperty("vPrincipalId")
    public String getvPrincipalId() {
        return vPrincipalId;
    }

    public void setvPrincipalId(String vPrincipalId) {
        this.vPrincipalId = vPrincipalId;
    }

    @JsonProperty("dInvestigateDate")
    public Date getdInvestigateDate() {
        return dInvestigateDate;
    }

    public void setdInvestigateDate(Date dInvestigateDate) {
        this.dInvestigateDate = dInvestigateDate;
    }

    @JsonProperty("iElevatorCount")
    public Integer getiElevatorCount() {
        return iElevatorCount;
    }

    public void setiElevatorCount(Integer iElevatorCount) {
        this.iElevatorCount = iElevatorCount;
    }

    @JsonProperty("iStatus")
    public Integer getiStatus() {
        return iStatus;
    }

    public void setiStatus(Integer iStatus) {
        this.iStatus = iStatus;
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
    public int getiDelFlag() {
        return iDelFlag;
    }

    public void setiDelFlag(int iDelFlag) {
        this.iDelFlag = iDelFlag;
    }

    @JsonProperty("vVillageId")
    public String getvVillageId() {
        return vVillageId;
    }

    public void setvVillageId(String vVillageId) {
        this.vVillageId = vVillageId;
    }

    @JsonProperty("vTemplateFileUrl")
    public String getvTemplateFileUrl() {
        return vTemplateFileUrl;
    }

    public void setvTemplateFileUrl(String vTemplateFileUrl) {
        this.vTemplateFileUrl = vTemplateFileUrl;
    }

    @JsonProperty("iNextStep")
    public Integer getiNextStep() {
        return iNextStep;
    }

    public void setiNextStep(Integer iNextStep) {
        this.iNextStep = iNextStep;
    }

    /**
     * 判断当前任务状态
     */
    public Boolean isCompleted() {
        return this.getiStatus() == TblInvestigateTaskStatusEnum.Completed.getValue();
    }


    @Override
    public String toString() {
        return "TblInvestigateTask{" +
                "vInvestigateTaskId='" + vInvestigateTaskId + '\'' +
                ", vProjectId='" + vProjectId + '\'' +
                ", vVillageId='" + vVillageId + '\'' +
                ", vAddress='" + vAddress + '\'' +
                ", vPrincipalId='" + vPrincipalId + '\'' +
                ", dInvestigateDate=" + dInvestigateDate +
                ", iElevatorCount=" + iElevatorCount +
                ", iStatus=" + iStatus +
                ", dtCreateTime=" + dtCreateTime +
                ", dtModifyTime=" + dtModifyTime +
                ", vCreateUserId='" + vCreateUserId + '\'' +
                ", vModifyUserId='" + vModifyUserId + '\'' +
                ", iDelFlag=" + iDelFlag +
                ", vTemplateFileUrl='" + vTemplateFileUrl + '\'' +
                ", iNextStep=" + iNextStep +
                '}';
    }
}
