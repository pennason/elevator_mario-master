package com.shmashine.api.module.village.input;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shmashine.common.entity.base.PageListParams;

/**
 * 小区列表所需参数
 */
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

    /** 项目id集合  在这之前项目是有权限的 */

    /**
     * 权限列表
     */
    private ArrayList<String> permissionDeptIds;

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
}
