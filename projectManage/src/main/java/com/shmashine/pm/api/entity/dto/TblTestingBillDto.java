package com.shmashine.pm.api.entity.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TblTestingBillDto {

    private String vTestingBillId;

    private String vTestingTaskId;

    private String vProjectId;

    private String vVillageId;
    /**
     * 调测设备自检
     */
    private Integer iDeviceSelfcheck;
    /**
     * 设备是否故障
     */
    private Integer iDeviceFault;
    /**
     * 楼层校验是否正确
     */
    private Integer iElevatorFloorCollate;
    /**
     * 电梯故障输出是否正确
     */
    private Integer iElevatorFaultCollate;
    /**
     * 电梯故障屏蔽设置是否正确
     */
    private Integer iElevatorFaultCover;
    /**
     * 状态
     */
    private Integer iStatus;

    private Integer iNeedExpert;

    private String vElevatorCode;

    private String vRemark;

    private String vProjectName;

    private String vVillageName;

    private String vElevatorAddress;

    private String vElevatorId;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtCreateTime;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtModifyTime;
    /**
     * 创建记录用户
     */
    private String vCreateUserId;
    /**
     * 修改记录用户
     */
    private String vModifyUserId;
    /**
     * 删除标识 0-未删除，1-已删除
     */
    private Integer iDelFlag;

    private String vDeviceType;

    // 摄像头调测到位确认
    private Integer iCameraCheck;
    // 电梯运行确认
    private Integer iElevatorRunCheck;
    // 助动车检测
    private Integer iElectronBikeCheck;
    // 传感器检测
    private Integer iSensorCheck;

    @JsonProperty("vTestingBillId")
    public String getvTestingBillId() {
        return vTestingBillId;
    }

    public void setvTestingBillId(String vTestingBillId) {
        this.vTestingBillId = vTestingBillId;
    }

    @JsonProperty("vTestingTaskId")
    public String getvTestingTaskId() {
        return vTestingTaskId;
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

    public void setvTestingTaskId(String vTestingTaskId) {
        this.vTestingTaskId = vTestingTaskId;
    }

    @JsonProperty("iDeviceSelfcheck")
    public Integer getiDeviceSelfcheck() {
        return iDeviceSelfcheck;
    }

    public void setiDeviceSelfcheck(Integer iDeviceSelfcheck) {
        this.iDeviceSelfcheck = iDeviceSelfcheck;
    }

    @JsonProperty("iDeviceFault")
    public Integer getiDeviceFault() {
        return iDeviceFault;
    }

    public void setiDeviceFault(Integer iDeviceFault) {
        this.iDeviceFault = iDeviceFault;
    }

    @JsonProperty("iElevatorFloorCollate")
    public Integer getiElevatorFloorCollate() {
        return iElevatorFloorCollate;
    }

    public void setiElevatorFloorCollate(Integer iElevatorFloorCollate) {
        this.iElevatorFloorCollate = iElevatorFloorCollate;
    }

    @JsonProperty("iElevatorFaultCollate")
    public Integer getiElevatorFaultCollate() {
        return iElevatorFaultCollate;
    }

    public void setiElevatorFaultCollate(Integer iElevatorFaultCollate) {
        this.iElevatorFaultCollate = iElevatorFaultCollate;
    }

    @JsonProperty("iElevatorFaultCover")
    public Integer getiElevatorFaultCover() {
        return iElevatorFaultCover;
    }

    public void setiElevatorFaultCover(Integer iElevatorFaultCover) {
        this.iElevatorFaultCover = iElevatorFaultCover;
    }

    @JsonProperty("vRemark")
    public String getvRemark() {
        return vRemark;
    }

    public void setvRemark(String vRemark) {
        this.vRemark = vRemark;
    }

    @JsonProperty("iStatus")
    public Integer getiStatus() {
        return iStatus;
    }

    public void setiStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    @JsonProperty("vElevatorCode")
    public String getvElevatorCode() {
        return vElevatorCode;
    }

    public void setvElevatorCode(String vElevatorCode) {
        this.vElevatorCode = vElevatorCode;
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
    public Integer getiDelFlag() {
        return iDelFlag;
    }

    public void setiDelFlag(Integer iDelFlag) {
        this.iDelFlag = iDelFlag;
    }

    @JsonProperty("vProjectName")
    public String getvProjectName() {
        return vProjectName;
    }

    public void setvProjectName(String vProjectName) {
        this.vProjectName = vProjectName;
    }

    @JsonProperty("vVillageName")
    public String getvVillageName() {
        return vVillageName;
    }

    public void setvVillageName(String vVillageName) {
        this.vVillageName = vVillageName;
    }

    @JsonProperty("vElevatorAddress")
    public String getvElevatorAddress() {
        return vElevatorAddress;
    }

    public void setvElevatorAddress(String vElevatorAddress) {
        this.vElevatorAddress = vElevatorAddress;
    }

    @JsonProperty("iNeedExpert")
    public Integer getiNeedExpert() {
        return iNeedExpert;
    }

    public void setiNeedExpert(Integer iNeedExpert) {
        this.iNeedExpert = iNeedExpert;
    }

    @JsonProperty("vElevatorId")
    public String getvElevatorId() {
        return vElevatorId;
    }

    public void setvElevatorId(String vElevatorId) {
        this.vElevatorId = vElevatorId;
    }

    @JsonProperty("vDeviceType")
    public String getvDeviceType() {
        return vDeviceType;
    }

    public void setvDeviceType(String vDeviceType) {
        this.vDeviceType = vDeviceType;
    }

    @JsonProperty("iCameraCheck")
    public Integer getiCameraCheck() {
        return iCameraCheck;
    }

    public void setiCameraCheck(Integer iCameraCheck) {
        this.iCameraCheck = iCameraCheck;
    }

    @JsonProperty("iElevatorRunCheck")
    public Integer getiElevatorRunCheck() {
        return iElevatorRunCheck;
    }

    public void setiElevatorRunCheck(Integer iElevatorRunCheck) {
        this.iElevatorRunCheck = iElevatorRunCheck;
    }

    @JsonProperty("iElectronBikeCheck")
    public Integer getiElectronBikeCheck() {
        return iElectronBikeCheck;
    }

    public void setiElectronBikeCheck(Integer iElectronBikeCheck) {
        this.iElectronBikeCheck = iElectronBikeCheck;
    }

    @JsonProperty("iSensorCheck")
    public Integer getiSensorCheck() {
        return iSensorCheck;
    }

    public void setiSensorCheck(Integer iSensorCheck) {
        this.iSensorCheck = iSensorCheck;
    }
}
