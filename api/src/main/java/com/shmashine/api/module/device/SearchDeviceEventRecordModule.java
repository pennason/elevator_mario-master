package com.shmashine.api.module.device;

import com.shmashine.common.entity.base.PageListParams;

/**
 * @author little.li
 */
public class SearchDeviceEventRecordModule extends PageListParams {


    private String elevatorCode;

    private String vProjectId;

    private String villageId;

    public String getElevatorCode() {
        return elevatorCode;
    }

    public void setElevatorCode(String elevatorCode) {
        this.elevatorCode = elevatorCode;
    }

    public String getvProjectId() {
        return vProjectId;
    }

    public void setvProjectId(String vProjectId) {
        this.vProjectId = vProjectId;
    }

    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
    }
}
