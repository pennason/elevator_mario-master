package com.shmashine.pm.api.module.investigateBill.input;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shmashine.pm.api.entity.base.PageListParams;

public class SearchTaskBillModule extends PageListParams {

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

    private String vVillageId;

    private String vProjectId;

    private String vElevatorId;

    private String iStatus;

    private int iDelFlag;

    private String vInvestigateOnceBillId;

    /**
     * 权限列表
     */
    private ArrayList<String> permissionDeptIds;

    public String getvInvestigateBillId() {
        return vInvestigateBillId;
    }

    public void setvInvestigateBillId(String vInvestigateBillId) {
        this.vInvestigateBillId = vInvestigateBillId;
    }

    public String getvInvestigateTaskId() {
        return vInvestigateTaskId;
    }

    public void setvInvestigateTaskId(String vInvestigateTaskId) {
        this.vInvestigateTaskId = vInvestigateTaskId;
    }

    public Integer getiWellHoleFourMeter() {
        return iWellHoleFourMeter;
    }

    public void setiWellHoleFourMeter(Integer iWellHoleFourMeter) {
        this.iWellHoleFourMeter = iWellHoleFourMeter;
    }

    public Integer getiMachRoom220() {
        return iMachRoom220;
    }

    public void setiMachRoom220(Integer iMachRoom220) {
        this.iMachRoom220 = iMachRoom220;
    }

    public Integer getiBackupCable() {
        return iBackupCable;
    }

    public void setiBackupCable(Integer iBackupCable) {
        this.iBackupCable = iBackupCable;
    }

    public Integer getiStraightDoorElevator() {
        return iStraightDoorElevator;
    }

    public void setiStraightDoorElevator(Integer iStraightDoorElevator) {
        this.iStraightDoorElevator = iStraightDoorElevator;
    }

    public String getvRealInvestigator() {
        return vRealInvestigator;
    }

    public void setvRealInvestigator(String vRealInvestigator) {
        this.vRealInvestigator = vRealInvestigator;
    }

    public String getvRealInvestigatorPhone() {
        return vRealInvestigatorPhone;
    }

    public void setvRealInvestigatorPhone(String vRealInvestigatorPhone) {
        this.vRealInvestigatorPhone = vRealInvestigatorPhone;
    }

    public Date getDtCreateTime() {
        return dtCreateTime;
    }

    public void setDtCreateTime(Date dtCreateTime) {
        this.dtCreateTime = dtCreateTime;
    }

    public Date getDtModifyTime() {
        return dtModifyTime;
    }

    public void setDtModifyTime(Date dtModifyTime) {
        this.dtModifyTime = dtModifyTime;
    }

    public String getvCreateUserId() {
        return vCreateUserId;
    }

    public void setvCreateUserId(String vCreateUserId) {
        this.vCreateUserId = vCreateUserId;
    }

    public String getvModifyUserId() {
        return vModifyUserId;
    }

    public void setvModifyUserId(String vModifyUserId) {
        this.vModifyUserId = vModifyUserId;
    }

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

    public String getvVillageId() {
        return vVillageId;
    }

    public void setvVillageId(String vVillageId) {
        this.vVillageId = vVillageId;
    }

    public String getvProjectId() {
        return vProjectId;
    }

    public void setvProjectId(String vProjectId) {
        this.vProjectId = vProjectId;
    }

    public String getvElevatorId() {
        return vElevatorId;
    }

    public void setvElevatorId(String vElevatorId) {
        this.vElevatorId = vElevatorId;
    }

    public String getiStatus() {
        return iStatus;
    }

    public void setiStatus(String iStatus) {
        this.iStatus = iStatus;
    }

    public String getvInvestigateOnceBillId() {
        return vInvestigateOnceBillId;
    }

    public void setvInvestigateOnceBillId(String vInvestigateOnceBillId) {
        this.vInvestigateOnceBillId = vInvestigateOnceBillId;
    }
}
