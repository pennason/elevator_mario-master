package com.shmashine.api.module.orderBlank;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shmashine.common.entity.base.PageListParams;

public class TblOrderBlankModule extends PageListParams {

    private String vOrderBlankId;

    private String vProjectId;

    private String vAddress;

    private String vBuildingName;

    private String vCubeNo;

    private String vElevatorCode;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtPlanInstallDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtAssignDate;

    private String vAssigner;

    private Integer iStatus;

    private String vElevatorId;

    /**
     * 权限列表
     */
    private ArrayList<String> permissionDeptIds;

    @JsonProperty("vOrderBlankId")
    public String getvOrderBlankId() {
        return vOrderBlankId;
    }

    public void setvOrderBlankId(String vOrderBlankId) {
        this.vOrderBlankId = vOrderBlankId;
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

    @JsonProperty("vBuildingName")
    public String getvBuildingName() {
        return vBuildingName;
    }

    public void setvBuildingName(String vBuildingName) {
        this.vBuildingName = vBuildingName;
    }

    @JsonProperty("vCubeNo")
    public String getvCubeNo() {
        return vCubeNo;
    }

    public void setvCubeNo(String vCubeNo) {
        this.vCubeNo = vCubeNo;
    }

    @JsonProperty("vElevatorCode")
    public String getvElevatorCode() {
        return vElevatorCode;
    }

    public void setvElevatorCode(String vElevatorCode) {
        this.vElevatorCode = vElevatorCode;
    }

    @JsonProperty("dtPlanInstallDate")
    public Date getDtPlanInstallDate() {
        return dtPlanInstallDate;
    }

    public void setDtPlanInstallDate(Date dtPlanInstallDate) {
        this.dtPlanInstallDate = dtPlanInstallDate;
    }

    @JsonProperty("dtAssignDate")
    public Date getDtAssignDate() {
        return dtAssignDate;
    }

    public void setDtAssignDate(Date dtAssignDate) {
        this.dtAssignDate = dtAssignDate;
    }

    @JsonProperty("vAssigner")
    public String getvAssigner() {
        return vAssigner;
    }

    public void setvAssigner(String vAssigner) {
        this.vAssigner = vAssigner;
    }

    @JsonProperty("iStatus")
    public Integer getiStatus() {
        return iStatus;
    }

    public void setiStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    @JsonProperty("vElevatorId")
    public String getvElevatorId() {
        return vElevatorId;
    }

    public void setvElevatorId(String vElevatorId) {
        this.vElevatorId = vElevatorId;
    }

    public ArrayList<String> getPermissionDeptIds() {
        return permissionDeptIds;
    }

    public void setPermissionDeptIds(ArrayList<String> permissionDeptIds) {
        this.permissionDeptIds = permissionDeptIds;
    }
}
