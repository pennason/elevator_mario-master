package com.shmashine.pm.api.module.testingTask.input;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shmashine.pm.api.entity.base.PageListParams;

public class SearchTestingTaskListModule extends PageListParams {

    private String vTestingTaskId;

    private String vProjectId;

    private String vVillageId;

    private String vAddress;

    private String vPrincipalId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dInstallingDate;

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

    /**
     * 权限列表
     */
    private ArrayList<String> permissionDeptIds;

    @JsonProperty("vTestingTaskId")
    public String getvTestingTaskId() {
        return vTestingTaskId;
    }

    public void setvTestingTaskId(String vTestingTaskId) {
        this.vTestingTaskId = vTestingTaskId;
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

    @JsonProperty("dInstallingDate")
    public Date getdInstallingDate() {
        return dInstallingDate;
    }

    public void setdInstallingDate(Date dInstallingDate) {
        this.dInstallingDate = dInstallingDate;
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

    public ArrayList<String> getPermissionDeptIds() {
        return permissionDeptIds;
    }

    public void setPermissionDeptIds(ArrayList<String> permissionDeptIds) {
        this.permissionDeptIds = permissionDeptIds;
    }
}
