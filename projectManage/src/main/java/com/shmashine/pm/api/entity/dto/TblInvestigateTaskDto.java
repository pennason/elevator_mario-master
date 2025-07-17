package com.shmashine.pm.api.entity.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TblInvestigateTaskDto {

    private Map villageInfo;

    private List<Map> investigateBillInfo;

    @JsonProperty("villageInfo")
    public Map getVillageInfo() {
        return villageInfo;
    }

    public void setVillageInfo(Map villageInfo) {
        this.villageInfo = villageInfo;
    }

    @JsonProperty("investigateBillInfo")
    public List<Map> getInvestigateBillInfo() {
        return investigateBillInfo;
    }

    public void setInvestigateBillInfo(List<Map> investigateBillInfo) {
        this.investigateBillInfo = investigateBillInfo;
    }
}
