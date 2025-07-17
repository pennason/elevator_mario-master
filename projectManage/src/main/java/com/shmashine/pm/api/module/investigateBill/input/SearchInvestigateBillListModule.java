package com.shmashine.pm.api.module.investigateBill.input;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shmashine.pm.api.entity.base.PageListParams;

public class SearchInvestigateBillListModule extends PageListParams {

    private String vInvestigateBillId;

    private String vInvestigateTaskId;

    private Integer iWellHoleFourMeter;

    private Integer iMachRoom220;

    private Integer iBackupCable;

    private Integer iStraightDoorElevator;

    private String vRealInvestigator;

    private String vRealInvestigatorPhone;

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

    @JsonProperty("vInvestigateBillId")
    public String getvInvestigateBillId() {
        return vInvestigateBillId;
    }

    public void setvInvestigateBillId(String vInvestigateBillId) {
        this.vInvestigateBillId = vInvestigateBillId;
    }

    @JsonProperty("vInvestigateTaskId")
    public String getvInvestigateTaskId() {
        return vInvestigateTaskId;
    }

    public void setvInvestigateTaskId(String vInvestigateTaskId) {
        this.vInvestigateTaskId = vInvestigateTaskId;
    }

    @JsonProperty("vInvestigateTaskId")
    public Integer getiWellHoleFourMeter() {
        return iWellHoleFourMeter;
    }

    public void setiWellHoleFourMeter(Integer iWellHoleFourMeter) {
        this.iWellHoleFourMeter = iWellHoleFourMeter;
    }

    @JsonProperty("iMachRoom220")
    public Integer getiMachRoom220() {
        return iMachRoom220;
    }

    public void setiMachRoom220(Integer iMachRoom220) {
        this.iMachRoom220 = iMachRoom220;
    }

    @JsonProperty("iBackupCable")
    public Integer getiBackupCable() {
        return iBackupCable;
    }

    public void setiBackupCable(Integer iBackupCable) {
        this.iBackupCable = iBackupCable;
    }

    @JsonProperty("iStraightDoorElevator")
    public Integer getiStraightDoorElevator() {
        return iStraightDoorElevator;
    }

    public void setiStraightDoorElevator(Integer iStraightDoorElevator) {
        this.iStraightDoorElevator = iStraightDoorElevator;
    }

    @JsonProperty("vRealInvestigator")
    public String getvRealInvestigator() {
        return vRealInvestigator;
    }

    public void setvRealInvestigator(String vRealInvestigator) {
        this.vRealInvestigator = vRealInvestigator;
    }

    @JsonProperty("vRealInvestigatorPhone")
    public String getvRealInvestigatorPhone() {
        return vRealInvestigatorPhone;
    }

    public void setvRealInvestigatorPhone(String vRealInvestigatorPhone) {
        this.vRealInvestigatorPhone = vRealInvestigatorPhone;
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

    public ArrayList<String> getPermissionDeptIds() {
        return permissionDeptIds;
    }

    public void setPermissionDeptIds(ArrayList<String> permissionDeptIds) {
        this.permissionDeptIds = permissionDeptIds;
    }
}
