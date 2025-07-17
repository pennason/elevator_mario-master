package com.shmashine.api.module.siteInvestigation;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shmashine.common.entity.base.PageListParams;

public class SearchSiteInvestigationModule extends PageListParams {
    /*
     *  项目id
     */
    private String vProjectId;
    /*
     *  电梯id
     */
    private String vElevatorId;

    /*
     *  现勘id
     */
    private String vSiteInvestigationId;

    /*
     *  状态
     */
    private Integer iStatus;

    /**
     * 权限列表
     */
    private ArrayList<String> permissionDeptIds;

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

    @JsonProperty("iStatus")
    public Integer getvStatus() {
        return iStatus;
    }

    public void setvStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    public ArrayList<String> getPermissionDeptIds() {
        return permissionDeptIds;
    }

    public void setPermissionDeptIds(ArrayList<String> permissionDeptIds) {
        this.permissionDeptIds = permissionDeptIds;
    }

    public String getvSiteInvestigationId() {
        return vSiteInvestigationId;
    }

    public void setvSiteInvestigationId(String vSiteInvestigationId) {
        this.vSiteInvestigationId = vSiteInvestigationId;
    }

    @Override
    public String toString() {
        return "SearchSiteInvestigationModule{" +
                "vProjectId='" + vProjectId + '\'' +
                ", vElevatorId='" + vElevatorId + '\'' +
                ", vSiteInvestigationId='" + vSiteInvestigationId + '\'' +
                ", iStatus='" + iStatus + '\'' +
                ", permissionDeptIds=" + permissionDeptIds +
                ", pageSize=" + pageSize +
                ", pageIndex=" + pageIndex +
                ", isAdminFlag=" + isAdminFlag +
                ", userId='" + userId + '\'' +
                '}';
    }
}
