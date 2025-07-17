package com.shmashine.pm.api.entity.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shmashine.pm.api.entity.TblPmImage;

public class TblInvestigateBillDto implements Serializable {

    private static final long serialVersionUID = 5836683700412909258L;

    private String vInvestigateBillId;

    private String vInvestigateTaskId;

    private Integer iWellHoleFourMeter;

    private Integer iMachRoom220;

    private Integer iBackupCable;

    private Integer iStraightDoorElevator;

    private String vRealInvestigator;

    private String vRealInvestigatorPhone;

    private String vElevatorBrandId;

    private String vAddress;

    private String vEquipmentCode;

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

    private Integer iMachRoom;

    private List<TblPmImage> greenCodeImages;

    private List<TblPmImage> floorImages;

    private List<TblPmImage> machRoomSignalImages;

    private List<TblPmImage> equipmentCodeImages;

    private List<TblPmImage> elevatorDoorBtnInsideImages;

    private String vElevatorId;

    private Integer iStatus;

    private String vStatusName;

    private String vDeviceType;

    private String vDevicePosition;

    private List<TblPmImage> images;

    private String vElevatorBrand;

    private String vInvestigateOnceBillId;

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

    @JsonProperty("iWellHoleFourMeter")
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

    @JsonProperty("greenCodeImages")
    public List<TblPmImage> getGreenCodeImages() {
        return greenCodeImages;
    }

    public void setGreenCodeImages(List<TblPmImage> greenCodeImages) {
        this.greenCodeImages = greenCodeImages;
    }

    @JsonProperty("floorImages")
    public List<TblPmImage> getFloorImages() {
        return floorImages;
    }

    public void setFloorImages(List<TblPmImage> floorImages) {
        this.floorImages = floorImages;
    }

    @JsonProperty("machRoomSignalImages")
    public List<TblPmImage> getMachRoomSignalImages() {
        return machRoomSignalImages;
    }

    public void setMachRoomSignalImages(List<TblPmImage> machRoomSignalImages) {
        this.machRoomSignalImages = machRoomSignalImages;
    }

    @JsonProperty("equipmentCodeImages")
    public List<TblPmImage> getEquipmentCodeImages() {
        return equipmentCodeImages;
    }

    public void setEquipmentCodeImages(List<TblPmImage> equipmentCodeImages) {
        this.equipmentCodeImages = equipmentCodeImages;
    }

    @JsonProperty("elevatorDoorBtnInsideImages")
    public List<TblPmImage> getElevatorDoorBtnInsideImages() {
        return elevatorDoorBtnInsideImages;
    }

    public void setElevatorDoorBtnInsideImages(List<TblPmImage> elevatorDoorBtnInsideImages) {
        this.elevatorDoorBtnInsideImages = elevatorDoorBtnInsideImages;
    }

    @JsonProperty("images")
    public List<TblPmImage> getImages() {
        return images;
    }

    public void setImages(List<TblPmImage> images) {
        this.images = images;
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

    @JsonProperty("vStatusName")
    public String getvStatusName() {
        return vStatusName;
    }

    public void setvStatusName(String vStatusName) {
        this.vStatusName = vStatusName;
    }

    @JsonProperty("vDeviceType")
    public String getvDeviceType() {
        return vDeviceType;
    }

    public void setvDeviceType(String vDeviceType) {
        this.vDeviceType = vDeviceType;
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

    @JsonProperty("vElevatorBrandId")
    public String getvElevatorBrandId() {
        return vElevatorBrandId;
    }

    public void setvElevatorBrandId(String vElevatorBrandId) {
        this.vElevatorBrandId = vElevatorBrandId;
    }

    @JsonProperty("vAddress")
    public String getvAddress() {
        return vAddress;
    }

    public void setvAddress(String vAddress) {
        this.vAddress = vAddress;
    }

    @JsonProperty("vEquipmentCode")
    public String getvEquipmentCode() {
        return vEquipmentCode;
    }

    public void setvEquipmentCode(String vEquipmentCode) {
        this.vEquipmentCode = vEquipmentCode;
    }
}
