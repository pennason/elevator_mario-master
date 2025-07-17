package com.shmashine.pm.api.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TblInvestigateBill implements Serializable {

    private static final long serialVersionUID = 5692203060665952795L;

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

    private Integer iElevatorCount;

    // 是否有机房
    private Integer iMachRoom;

    // 电梯id
    private String vElevatorId;
    // 状态
    private Integer iStatus;
    // 项目
    private String vProjectId;
    // 小区
    private String vVillageId;
    // 设备安装位置
    private String vDevicePosition;
    /**
     * 电梯品牌
     */
    private String vElevatorBrand;
    /**
     * 快勘id
     */
    private String vInvestigateOnceBillId;

    /**
     * 是否控梯
     */
    private Integer iControlElevator;

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

    @JsonProperty("iElevatorCount")
    public Integer getiElevatorCount() {
        return iElevatorCount;
    }

    public void setiElevatorCount(Integer iElevatorCount) {
        this.iElevatorCount = iElevatorCount;
    }

    @JsonProperty("iMachRoom")
    public Integer getiMachRoom() {
        return iMachRoom;
    }

    public void setiMachRoom(Integer iMachRoom) {
        this.iMachRoom = iMachRoom;
    }

    @JsonProperty("vElevatorId")
    public String getvElevatorId() {
        return vElevatorId;
    }

    public void setvElevatorId(String vElevatorId) {
        this.vElevatorId = vElevatorId;
    }

    @JsonProperty("iStatus")
    public Integer getiStatus() {
        return iStatus;
    }

    public void setiStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    @JsonProperty("vProjectId")
    public String getvProjectId() {
        return vProjectId;
    }

    public void setvProjectId(String vProjectId) {
        this.vProjectId = vProjectId;
    }

    @JsonProperty("vVillageId")
    public String getvVillageId() {
        return vVillageId;
    }

    public void setvVillageId(String vVillageId) {
        this.vVillageId = vVillageId;
    }

    @JsonProperty("vDevicePosition")
    public String getvDevicePosition() {
        return vDevicePosition;
    }

    public void setvDevicePosition(String vDevicePosition) {
        this.vDevicePosition = vDevicePosition;
    }

    @JsonProperty("vElevatorBrand")
    public String getvElevatorBrand() {
        return vElevatorBrand;
    }

    public void setvElevatorBrand(String vElevatorBrand) {
        this.vElevatorBrand = vElevatorBrand;
    }

    @JsonProperty("vInvestigateOnceBillId")
    public String getvInvestigateOnceBillId() {
        return vInvestigateOnceBillId;
    }

    public void setvInvestigateOnceBillId(String vInvestigateOnceBillId) {
        this.vInvestigateOnceBillId = vInvestigateOnceBillId;
    }

    @JsonProperty("iControlElevator")
    public Integer getiControlElevator() {
        return iControlElevator;
    }

    public void setiControlElevator(Integer iControlElevator) {
        this.iControlElevator = iControlElevator;
    }
}
