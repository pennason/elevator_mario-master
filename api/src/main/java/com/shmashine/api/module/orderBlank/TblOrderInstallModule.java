package com.shmashine.api.module.orderBlank;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shmashine.common.entity.base.PageListParams;

public class TblOrderInstallModule extends PageListParams {

    private String vOrderInstallId;

    private String vOrderBlankId;

    private Integer iStatus;

    private String vInstaller;

    private String vProjectId;

    private String vElevatorId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date vInstallDate;

    /**
     * 权限列表
     */
    private ArrayList<String> permissionDeptIds;

    @JsonProperty("vOrderInstallId")
    public String getvOrderInstallId() {
        return vOrderInstallId;
    }

    public void setvOrderInstallId(String vOrderInstallId) {
        this.vOrderInstallId = vOrderInstallId;
    }

    @JsonProperty("vOrderBlankId")
    public String getvOrderBlankId() {
        return vOrderBlankId;
    }

    public void setvOrderBlankId(String vOrderBlankId) {
        this.vOrderBlankId = vOrderBlankId;
    }

    @JsonProperty("iStatus")
    public Integer getiStatus() {
        return iStatus;
    }

    public void setiStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    @JsonProperty("vInstaller")
    public String getvInstaller() {
        return vInstaller;
    }

    public void setvInstaller(String vInstaller) {
        this.vInstaller = vInstaller;
    }

    @JsonProperty("vInstallDate")
    public Date getvInstallDate() {
        return vInstallDate;
    }

    public void setvInstallDate(Date vInstallDate) {
        this.vInstallDate = vInstallDate;
    }

    @JsonProperty("vProjectId")
    public String getvProjectId() {
        return vProjectId;
    }

    public void setvProjectId(String vProjectId) {
        this.vProjectId = vProjectId;
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
