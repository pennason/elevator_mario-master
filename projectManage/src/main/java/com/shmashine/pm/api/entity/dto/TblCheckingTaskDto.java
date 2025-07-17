package com.shmashine.pm.api.entity.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shmashine.common.annotation.FieldEncrypt;
import com.shmashine.common.annotation.SensitiveData;

/**
 * 验收单
 */
@SensitiveData
public class TblCheckingTaskDto {

    private String vCheckingTaskId;

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

    private String vRemark;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dCheckDate;

    private String vProjectName;

    private String vVillageName;

    private String iStatusName;

    @FieldEncrypt(key = "vName")
    private String vPrincipalName;

    @FieldEncrypt(key = "vMobile")
    private String vPrincipalMobile;

    private List<Map> testingBillInfo;

    @JsonProperty("vCheckingTaskId")
    public String getvCheckingTaskId() {
        return vCheckingTaskId;
    }

    public void setvCheckingTaskId(String vInstallingTaskId) {
        this.vCheckingTaskId = vCheckingTaskId;
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

    @JsonProperty("dCheckDate")
    public Date getdCheckDate() {
        return dCheckDate;
    }

    public void setdCheckDate(Date dCheckDate) {
        this.dCheckDate = dCheckDate;
    }

    @JsonProperty("vProjectName")
    public String getvProjectName() {
        return vProjectName;
    }

    public void setvProjectName(String vProjectName) {
        this.vProjectName = vProjectName;
    }

    @JsonProperty("vVillageName")
    public String getvVillageName() {
        return vVillageName;
    }

    public void setvVillageName(String vVillageName) {
        this.vVillageName = vVillageName;
    }

    @JsonProperty("iStatusName")
    public String getiStatusName() {
        return iStatusName;
    }

    public void setiStatusName(String iStatusName) {
        this.iStatusName = iStatusName;
    }

    @JsonProperty("vPrincipalName")
    public String getvPrincipalName() {
        return vPrincipalName;
    }

    public void setvPrincipalName(String vPrincipalName) {
        this.vPrincipalName = vPrincipalName;
    }

    @JsonProperty("vPrincipalMobile")
    public String getvPrincipalMobile() {
        return vPrincipalMobile;
    }

    public void setvPrincipalMobile(String vPrincipalMobile) {
        this.vPrincipalMobile = vPrincipalMobile;
    }

    @JsonProperty("testingBillInfo")
    public List<Map> getTestingBillInfo() {
        return testingBillInfo;
    }

    public void setTestingBillInfo(List<Map> testingBillInfo) {
        this.testingBillInfo = testingBillInfo;
    }

    @JsonProperty("vRemark")
    public String getvRemark() {
        return vRemark;
    }

    public void setvRemark(String vRemark) {
        this.vRemark = vRemark;
    }
}
