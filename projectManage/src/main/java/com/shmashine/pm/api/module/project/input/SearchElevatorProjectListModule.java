package com.shmashine.pm.api.module.project.input;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shmashine.pm.api.entity.base.PageListParams;

public class SearchElevatorProjectListModule extends PageListParams {

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
     * 项目类型
     */
    private Integer projectType;
    /**
     * 权限列表
     */
    private ArrayList<String> permissionDeptIds;
    /**
     * 备注
     */
    private String vRemarks;
    /**
     * @return
     */
    private Integer iStatus;

    private String vDeptName;

    @JsonProperty("vDeptId")
    public String getVDeptId() {
        return vDeptId;
    }

    public void setVDeptId(String vDeptId) {
        this.vDeptId = vDeptId;
    }


    @JsonProperty("vRemarks")
    public String getVRemarks() {
        return vRemarks;
    }

    public void setVRemarks(String vRemarks) {
        this.vRemarks = vRemarks;
    }

    public ArrayList<String> getPermissionDeptIds() {
        return permissionDeptIds;
    }

    public void setPermissionDeptIds(ArrayList<String> permissionDeptIds) {
        this.permissionDeptIds = permissionDeptIds;
    }

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

    @JsonProperty("vPartyA")
    public String getVPartyA() {
        return vPartyA;
    }

    public Integer getProjectType() {
        return projectType;
    }

    public void setProjectType(Integer projectType) {
        this.projectType = projectType;
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

    public Integer getiStatus() {
        return iStatus;
    }

    public void setiStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    @JsonProperty("vDeptName")
    public String getvDeptName() {
        return vDeptName;
    }

    public void setvDeptName(String vDeptName) {
        this.vDeptName = vDeptName;
    }
}
