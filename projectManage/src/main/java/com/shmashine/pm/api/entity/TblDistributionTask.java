package com.shmashine.pm.api.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TblDistributionTask implements Serializable {

    private static final long serialVersionUID = 1659436269319508774L;

    private String vDistributionTaskId;

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

    private String vTemplateFileUrl;

    private Integer iElevatorCount;

    private String vAddress;

    private Integer iNextStep;

    private String vInitElevatorCode;

    private String vInitVerifyCode;

    private Integer iShortcut;

    private Integer iCubeCount;

    private String vDistributerName;

    @JsonProperty("vDistributionTaskId")
    public String getvDistributionTaskId() {
        return vDistributionTaskId;
    }

    public void setvDistributionTaskId(String vDistributionTaskId) {
        this.vDistributionTaskId = vDistributionTaskId;
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

    @JsonProperty("vTemplateFileUrl")
    public String getvTemplateFilelUrl() {
        return vTemplateFileUrl;
    }

    public void setvTemplateFileUrl(String vTemplateFileUrl) {
        this.vTemplateFileUrl = vTemplateFileUrl;
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

    @JsonProperty("iNextStep")
    public Integer getiNextStep() {
        return iNextStep;
    }

    public void setiNextStep(Integer iNextStep) {
        this.iNextStep = iNextStep;
    }

    @JsonProperty("vInitElevatorCode")
    public String getvInitElevatorCode() {
        return vInitElevatorCode;
    }

    public void setvInitElevatorCode(String vInitElevatorCode) {
        this.vInitElevatorCode = vInitElevatorCode;
    }

    @JsonProperty("vInitVerifyCode")
    public String getvInitVerifyCode() {
        return vInitVerifyCode;
    }

    public void setvInitVerifyCode(String vInitVerifyCode) {
        this.vInitVerifyCode = vInitVerifyCode;
    }

    @JsonProperty("iShortcut")
    public Integer getiShortcut() {
        return iShortcut;
    }

    public void setiShortcut(Integer iShortCut) {
        this.iShortcut = iShortCut;
    }

    @JsonProperty("iCubeCount")
    public Integer getiCubeCount() {
        if (iCubeCount == null)
            return 1;
        return iCubeCount;
    }

    public void setiCubeCount(Integer iCubeCount) {
        this.iCubeCount = iCubeCount;
    }

    @JsonProperty("vDistributerName")
    public String getvDistributerName() {
        return vDistributerName;
    }

    public void setvDistributerName(String vDistributerName) {
        this.vDistributerName = vDistributerName;
    }

    @Override
    public String toString() {
        return "TblDistributionTask{" +
                "vDistributionTaskId='" + vDistributionTaskId + '\'' +
                ", vProjectId='" + vProjectId + '\'' +
                ", vVillageId='" + vVillageId + '\'' +
                ", vPrincipalId='" + vPrincipalId + '\'' +
                ", dtCreateTime=" + dtCreateTime +
                ", dtModifyTime=" + dtModifyTime +
                ", vCreateUserId='" + vCreateUserId + '\'' +
                ", vModifyUserId='" + vModifyUserId + '\'' +
                ", iDelFlag=" + iDelFlag +
                ", iStatus=" + iStatus +
                ", vTemplateFileUrl='" + vTemplateFileUrl + '\'' +
                ", iElevatorCount=" + iElevatorCount +
                ", vAddress='" + vAddress + '\'' +
                '}';
    }
}
