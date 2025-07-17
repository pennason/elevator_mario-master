package com.shmashine.api.module.building;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shmashine.common.entity.base.PageListParams;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 搜索楼栋信息
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SearchBuildingModule extends PageListParams {
    /**
     * 小区id
     */
    private String villageId;
    /**
     * 楼宇id
     */
    private String vBuildingId;
    /**
     * 楼宇名称
     */
    private String vBuildingName;
    /**
     * 小区ids
     */
    private ArrayList<String> villageIds;
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

    @JsonProperty("villageId")
    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String vVillageId) {
        this.villageId = vVillageId;
    }

    @JsonProperty("vBuildingId")
    public String getVBuildingId() {
        return vBuildingId;
    }

    public void setVBuildingId(String vBuildingId) {
        this.vBuildingId = vBuildingId;
    }

    @JsonProperty("vBuildingName")
    public String getVBuildingName() {
        return vBuildingName;
    }

    public void setVBuildingName(String vBuildingName) {
        this.vBuildingName = vBuildingName;
    }

    @JsonProperty("villageIds")
    public ArrayList getVillageIds() {
        return villageIds;
    }

    public void setVillageIds(ArrayList villageIds) {
        this.villageIds = villageIds;
    }
}
