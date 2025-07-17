package com.shmashine.pm.api.module.village.input;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shmashine.pm.api.entity.base.PageListParams;

public class SearchVillaListModule extends PageListParams {

    /**
     * 小区id
     */
    private String vVillageId;
    /**
     * 小区名称
     */
    private String vVillageName;
    /**
     * 小区详细地址
     */
    private String vAddress;
    /**
     * 小区所属项目id
     */
    private String vProjectId;
    /**
     * 备注
     */
    private String vRemarks;
    /**
     * 删除标识 0-未删除，1-已删除
     */
    private Integer iDelFlag;

    private String vContacts;

    private String vContactsPhone;

    private String vInvestigator;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dInvestigateDate;

    private String vInstaller;

    private Date dInstallDate;

    private Integer iElevatorCount;

    private String iStatus;

    /** 项目id集合  在这之前项目是有权限的 */

    /**
     * 权限列表
     */
    private ArrayList<String> permissionDeptIds;

    /**
     * 群租识别标记 1：开启，0关闭
     */
    private Integer groupLeasingStatus;

    /**
     * 群租识别结果 0：未确认 1：取证中 2：已确认
     */
    private Integer groupLeasingResult;

    public ArrayList<String> getPermissionDeptIds() {
        return permissionDeptIds;
    }

    public void setPermissionDeptIds(ArrayList<String> permissionDeptIds) {
        this.permissionDeptIds = permissionDeptIds;
    }

    @JsonProperty("vVillageId")
    public String getVVillageId() {
        return vVillageId;
    }

    public void setVVillageId(String vVillageId) {
        this.vVillageId = vVillageId;
    }

    @JsonProperty("vVillageName")
    public String getVVillageName() {
        return vVillageName;
    }

    public void setVVillageName(String vVillageName) {
        this.vVillageName = vVillageName;
    }

    @JsonProperty("vAddress")
    public String getVAddress() {
        return vAddress;
    }

    public void setVAddress(String vAddress) {
        this.vAddress = vAddress;
    }

    @JsonProperty("vProjectId")
    public String getVProjectId() {
        return vProjectId;
    }

    public void setVProjectId(String vProjectId) {
        this.vProjectId = vProjectId;
    }

    @JsonProperty("vRemarks")
    public String getVRemarks() {
        return vRemarks;
    }

    public void setVRemarks(String vRemarks) {
        this.vRemarks = vRemarks;
    }

    @JsonProperty("iDelFlag")
    public Integer getIDelFlag() {
        return iDelFlag;
    }

    public void setIDelFlag(Integer iDelFlag) {
        this.iDelFlag = iDelFlag;
    }

    @JsonProperty("vContacts")
    public String getvContacts() {
        return vContacts;
    }

    public void setvContacts(String vContacts) {
        this.vContacts = vContacts;
    }

    @JsonProperty("vContactsPhone")
    public String getvContactsPhone() {
        return vContactsPhone;
    }

    public void setvContactsPhone(String vContactsPhone) {
        this.vContactsPhone = vContactsPhone;
    }

    @JsonProperty("vInvestigator")
    public String getvInvestigator() {
        return vInvestigator;
    }

    public void setvInvestigator(String vInvestigator) {
        this.vInvestigator = vInvestigator;
    }

    @JsonProperty("dInvestigateDate")
    public Date getdInvestigateDate() {
        return dInvestigateDate;
    }

    public void setdInvestigateDate(Date dInvestigateDate) {
        this.dInvestigateDate = dInvestigateDate;
    }

    @JsonProperty("vInstaller")
    public String getvInstaller() {
        return vInstaller;
    }

    public void setvInstaller(String vInstaller) {
        this.vInstaller = vInstaller;
    }

    @JsonProperty("dInstallDate")
    public Date getdInstallDate() {
        return dInstallDate;
    }

    public void setdInstallDate(Date dInstallDate) {
        this.dInstallDate = dInstallDate;
    }

    @JsonProperty("iElevatorCount")
    public Integer getiElevatorCount() {
        return iElevatorCount;
    }

    public void setiElevatorCount(Integer iElevatorCount) {
        this.iElevatorCount = iElevatorCount;
    }

    @JsonProperty("iStatus")
    public String getiStatus() {
        return iStatus;
    }

    public void setiStatus(String iStatus) {
        this.iStatus = iStatus;
    }

    public Integer getGroupLeasingStatus() {
        return groupLeasingStatus;
    }

    public void setGroupLeasingStatus(Integer groupLeasingStatus) {
        this.groupLeasingStatus = groupLeasingStatus;
    }

    public Integer getGroupLeasingResult() {
        return groupLeasingResult;
    }

    public void setGroupLeasingResult(Integer groupLeasingResult) {
        this.groupLeasingResult = groupLeasingResult;
    }
}
