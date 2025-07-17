package com.shmashine.pm.api.entity.dto;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shmashine.pm.api.entity.TblPmImage;

public class TblInstallingBillDto {

    private String vInstallingBillId;

    private String vProjectId;

    private String vVillageId;

    private String vInstallingTaskId;

    private Integer iSingleBox;

    private Integer iDoubleBox;

    private String vConnectMode;

    private Integer iCamera;

    private Integer iFloorSensor;

    private Integer iCameraWatermark;

    private Integer iBodySensor;

    private Integer iCarroofDoorSensor;

    private Integer iCarroofLockSensor;

    private Integer iMachRoomServiceSensor;

    private Integer iMachRoomOverhaulSensor;

    private Integer iMachRoomSwitchSensor;

    private Integer iMachRoomCallSensor;

    private Integer iMachRoomCircuitSensor;

    private Integer iMachRoomHallCircuitSensor;

    private Integer iMachRoomSwitchCircuitSensor;

    private Integer iMachRoomElectricitySensor;

    private Integer iCable;

    private Integer iPowerSupply;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtCreateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtModifyTime;

    private String vCreateUserId;

    private String vModifyUserId;

    private int iDelFlag;

    private String vVerifyCode;

    private String vElevatorCode;

    private Integer iDeviceSelfcheck;

    private Integer iStatus;

    private String vRemark;

    private Integer i4gRouter;

    private Integer iRouter;

    private String vDeviceType;

    private Integer iTempSensor;

    private Integer iMagnetSensor;

    private Integer iPlcMode;

    private Integer iNetworkMode;

    private Integer iCollateFloorSensor;

    private Integer i4gModule;

    private Integer iPatchBoard;

    private Integer iCircuitBeauty;

    private Integer iCarroofEndpoint;

    private List<TblPmImage> carInsideImages;

    private List<TblPmImage> machRoomBoxImages;

    private List<TblPmImage> carRoofBoxImages;

    @JsonProperty("vInstallingBillId")
    public String getvInstallingBillId() {
        return vInstallingBillId;
    }

    public void setvInstallingBillId(String vInstallingBillId) {
        this.vInstallingBillId = vInstallingBillId;
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

    @JsonProperty("vInstallingTaskId")
    public String getvInstallingTaskId() {
        return vInstallingTaskId;
    }

    public void setvInstallingTaskId(String vInstallingTaskId) {
        this.vInstallingTaskId = vInstallingTaskId;
    }

    @JsonProperty("iSingleBox")
    public Integer getiSingleBox() {
        return iSingleBox;
    }

    public void setiSingleBox(Integer iSingleBox) {
        this.iSingleBox = iSingleBox;
    }

    @JsonProperty("iDoubleBox")
    public Integer getiDoubleBox() {
        return iDoubleBox;
    }

    public void setiDoubleBox(Integer iDoubleBox) {
        this.iDoubleBox = iDoubleBox;
    }

    @JsonProperty("vConnectMode")
    public String getvConnectMode() {
        return vConnectMode;
    }

    public void setvConnectMode(String vConnectMode) {
        this.vConnectMode = vConnectMode;
    }

    @JsonProperty("iCamera")
    public Integer getiCamera() {
        return iCamera;
    }

    public void setiCamera(Integer iCamera) {
        this.iCamera = iCamera;
    }

    @JsonProperty("iFloorSensor")
    public Integer getiFloorSensor() {
        return iFloorSensor;
    }

    public void setiFloorSensor(Integer iFloorSensor) {
        this.iFloorSensor = iFloorSensor;
    }

    @JsonProperty("iCameraWatermark")
    public Integer getiCameraWatermark() {
        return iCameraWatermark;
    }

    public void setiCameraWatermark(Integer iCameraWatermark) {
        this.iCameraWatermark = iCameraWatermark;
    }

    @JsonProperty("iBodySensor")
    public Integer getiBodySensor() {
        return iBodySensor;
    }

    public void setiBodySensor(Integer iBodySensor) {
        this.iBodySensor = iBodySensor;
    }

    @JsonProperty("iCarroofDoorSensor")
    public Integer getiCarroofDoorSensor() {
        return iCarroofDoorSensor;
    }

    public void setiCarroofDoorSensor(Integer iCarroofDoorSensor) {
        this.iCarroofDoorSensor = iCarroofDoorSensor;
    }

    @JsonProperty("iCarroofLockSensor")
    public Integer getiCarroofLockSensor() {
        return iCarroofLockSensor;
    }

    public void setiCarroofLockSensor(Integer iCarroofLockSensor) {
        this.iCarroofLockSensor = iCarroofLockSensor;
    }

    @JsonProperty("iMachRoomServiceSensor")
    public Integer getiMachRoomServiceSensor() {
        return iMachRoomServiceSensor;
    }

    public void setiMachRoomServiceSensor(Integer iMachRoomServiceSensor) {
        this.iMachRoomServiceSensor = iMachRoomServiceSensor;
    }

    @JsonProperty("iMachRoomOverhaulSensor")
    public Integer getiMachRoomOverhaulSensor() {
        return iMachRoomOverhaulSensor;
    }

    public void setiMachRoomOverhaulSensor(Integer iMachRoomOverhaulSensor) {
        this.iMachRoomOverhaulSensor = iMachRoomOverhaulSensor;
    }

    @JsonProperty("iMachRoomSwitchSensor")
    public Integer getiMachRoomSwitchSensor() {
        return iMachRoomSwitchSensor;
    }

    public void setiMachRoomSwitchSensor(Integer iMachRoomSwitchSensor) {
        this.iMachRoomSwitchSensor = iMachRoomSwitchSensor;
    }

    @JsonProperty("iMachRoomCallSensor")
    public Integer getiMachRoomCallSensor() {
        return iMachRoomCallSensor;
    }

    public void setiMachRoomCallSensor(Integer iMachRoomCallSensor) {
        this.iMachRoomCallSensor = iMachRoomCallSensor;
    }

    @JsonProperty("iMachRoomCircuitSensor")
    public Integer getiMachRoomCircuitSensor() {
        return iMachRoomCircuitSensor;
    }

    public void setiMachRoomCircuitSensor(Integer iMachRoomCircuitSensor) {
        this.iMachRoomCircuitSensor = iMachRoomCircuitSensor;
    }

    @JsonProperty("iMachRoomHallCircuitSensor")
    public Integer getiMachRoomHallCircuitSensor() {
        return iMachRoomHallCircuitSensor;
    }

    public void setiMachRoomHallCircuitSensor(Integer iMachRoomHallCircuitSensor) {
        this.iMachRoomHallCircuitSensor = iMachRoomHallCircuitSensor;
    }

    @JsonProperty("iMachRoomSwitchCircuitSensor")
    public Integer getiMachRoomSwitchCircuitSensor() {
        return iMachRoomSwitchCircuitSensor;
    }

    public void setiMachRoomSwitchCircuitSensor(Integer iMachRoomSwitchCircuitSensor) {
        this.iMachRoomSwitchCircuitSensor = iMachRoomSwitchCircuitSensor;
    }

    @JsonProperty("iMachRoomElectricitySensor")
    public Integer getiMachRoomElectricitySensor() {
        return iMachRoomElectricitySensor;
    }

    public void setiMachRoomElectricitySensor(Integer iMachRoomElectricitySensor) {
        this.iMachRoomElectricitySensor = iMachRoomElectricitySensor;
    }

    @JsonProperty("iCable")
    public Integer getiCable() {
        return iCable;
    }

    public void setiCable(Integer iCable) {
        this.iCable = iCable;
    }

    @JsonProperty("iPowerSupply")
    public Integer getiPowerSupply() {
        return iPowerSupply;
    }

    public void setiPowerSupply(Integer iPowerSupply) {
        this.iPowerSupply = iPowerSupply;
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

    @JsonProperty("vVerifyCode")
    public String getvVerifyCode() {
        return vVerifyCode;
    }

    public void setvVerifyCode(String vVerifyCode) {
        this.vVerifyCode = vVerifyCode;
    }

    @JsonProperty("vElevatorCode")
    public String getvElevatorCode() {
        return vElevatorCode;
    }

    public void setvElevatorCode(String vElevatorCode) {
        this.vElevatorCode = vElevatorCode;
    }

    @JsonProperty("iDeviceSelfcheck")
    public Integer getiDeviceSelfcheck() {
        return iDeviceSelfcheck;
    }

    public void setiDeviceSelfcheck(Integer iDeviceSelfcheck) {
        this.iDeviceSelfcheck = iDeviceSelfcheck;
    }

    @JsonProperty("iStatus")
    public Integer getiStatus() {
        return iStatus;
    }

    public void setiStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    @JsonProperty("vRemark")
    public String getvRemark() {
        return vRemark;
    }

    public void setvRemark(String vRemark) {
        this.vRemark = vRemark;
    }

    @JsonProperty("i4gRouter")
    public Integer getI4gRouter() {
        return i4gRouter;
    }

    public void setI4gRouter(Integer i4gRouter) {
        this.i4gRouter = i4gRouter;
    }

    @JsonProperty("iRouter")
    public Integer getiRouter() {
        return iRouter;
    }

    public void setiRouter(Integer iRouter) {
        this.iRouter = iRouter;
    }

    @JsonProperty("vDeviceType")
    public String getvDeviceType() {
        return vDeviceType;
    }

    public void setvDeviceType(String vDeviceType) {
        this.vDeviceType = vDeviceType;
    }

    @JsonProperty("carInsideImages")
    public List<TblPmImage> getCarInsideImages() {
        return carInsideImages;
    }

    public void setCarInsideImages(List<TblPmImage> carInsideImages) {
        this.carInsideImages = carInsideImages;
    }

    @JsonProperty("machRoomBoxImages")
    public List<TblPmImage> getMachRoomBoxImages() {
        return machRoomBoxImages;
    }

    public void setMachRoomBoxImages(List<TblPmImage> machRoomBoxImages) {
        this.machRoomBoxImages = machRoomBoxImages;
    }

    @JsonProperty("carRoofBoxImages")
    public List<TblPmImage> getCarRoofBoxImages() {
        return carRoofBoxImages;
    }

    public void setCarRoofBoxImages(List<TblPmImage> carRoofBoxImages) {
        this.carRoofBoxImages = carRoofBoxImages;
    }

    @JsonProperty("iTempSensor")
    public Integer getiTempSensor() {
        return iTempSensor;
    }

    public void setiTempSensor(Integer iTempSensor) {
        this.iTempSensor = iTempSensor;
    }

    @JsonProperty("iMagnetSensor")
    public Integer getiMagnetSensor() {
        return iMagnetSensor;
    }

    public void setiMagnetSensor(Integer iMagnetSensor) {
        this.iMagnetSensor = iMagnetSensor;
    }

    @JsonProperty("iPlcMode")
    public Integer getiPlcMode() {
        return iPlcMode;
    }

    public void setiPlcMode(Integer iPlcMode) {
        this.iPlcMode = iPlcMode;
    }

    @JsonProperty("iNetworkMode")
    public Integer getiNetworkMode() {
        return iNetworkMode;
    }

    public void setiNetworkMode(Integer iNetworkMode) {
        this.iNetworkMode = iNetworkMode;
    }

    @JsonProperty("iCollateFloorSensor")
    public Integer getiCollateFloorSensor() {
        return iCollateFloorSensor;
    }

    public void setiCollateFloorSensor(Integer iCollateFloorSensor) {
        this.iCollateFloorSensor = iCollateFloorSensor;
    }

    @JsonProperty("i4gModule")
    public Integer getI4gModule() {
        return i4gModule;
    }

    public void setI4gModule(Integer i4gModule) {
        this.i4gModule = i4gModule;
    }

    @JsonProperty("iPatchBoard")
    public Integer getiPatchBoard() {
        return iPatchBoard;
    }

    public void setiPatchBoard(Integer iPatchBoard) {
        this.iPatchBoard = iPatchBoard;
    }

    @JsonProperty("iCircuitBeauty")
    public Integer getiCircuitBeauty() {
        return iCircuitBeauty;
    }

    public void setiCircuitBeauty(Integer iCircuitBeauty) {
        this.iCircuitBeauty = iCircuitBeauty;
    }

    @JsonProperty("iCarroofEndpoint")
    public Integer getiCarroofEndpoint() {
        return iCarroofEndpoint;
    }

    public void setiCarroofEndpoint(Integer iCarroofEndpoint) {
        this.iCarroofEndpoint = iCarroofEndpoint;
    }
}
