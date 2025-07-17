package com.shmashine.commonbigscreen.entity;

import com.shmashine.common.entity.base.PageListParams;

/**
 * 电梯列表查询参数类
 *
 * @author LiuLiFu
 * @version V1.0.0 - 2020/6/1215:43
 */
public class SearchElevatorModule extends PageListParams {

    private String elevatorId;

    /**
     * 新增用户id
     */
    private String vUserId;

    private String vElevatorCode;

    /**
     * 电梯类型
     */
    private Integer iElevatorType;

    /**
     * 设备安装状态 0未安装 1安装
     */
    private Integer iInstallStatus;

    /**
     * 电梯在线状态，0 离线，1在线（有一个设备在线就认为电梯在线）
     */
    private Integer iOnLine;

    /**
     * 故障状态 0 未故障，1 故障
     */
    private Integer iFaultStatus;

    /**
     * 电梯服务模式 0 正常运行，1 检修模式，2 停止服务
     */
    private Integer iModeStatus;
    /**
     * 项目ID
     */
    private String vProjectId;
    /**
     * 小区ID
     */
    private String vVillageId;
    /**
     * 楼宇id
     */
    private String vBuildingId;

    private String startTime;

    private String endTime;

    /**
     * 省ID
     */
    private Integer iProvinceId;
    /**
     * 市ID
     */
    private Integer iCityId;
    /**
     * 区ID
     */
    private Integer iAreaId;

    /**
     * 设备版本号
     */
    private String vHwVersion;

    /**
     * 编号和地址联合
     */
    private String vCodeOrAddress;

    /**
     * 推送平台
     */
    private String vHttpPtCode;

    public String getvUserId() {
        return vUserId;
    }

    public void setvUserId(String vUserId) {
        this.vUserId = vUserId;
    }

    public String getvHwVersion() {
        return vHwVersion;
    }

    public void setvHwVersion(String vHwVersion) {
        this.vHwVersion = vHwVersion;
    }

    public Integer getiProvinceId() {
        return iProvinceId;
    }

    public void setiProvinceId(Integer iProvinceId) {
        this.iProvinceId = iProvinceId;
    }

    public Integer getiCityId() {
        return iCityId;
    }

    public void setiCityId(Integer iCityId) {
        this.iCityId = iCityId;
    }

    public Integer getiAreaId() {
        return iAreaId;
    }

    public void setiAreaId(Integer iAreaId) {
        this.iAreaId = iAreaId;
    }

    public String getvProjectId() {
        return vProjectId;
    }

    public void setvProjectId(String vProjectId) {
        this.vProjectId = vProjectId;
    }

    public String getvVillageId() {
        return vVillageId;
    }

    public void setvVillageId(String vVillageId) {
        this.vVillageId = vVillageId;
    }

    public Integer getiModeStatus() {
        return iModeStatus;
    }

    public void setiModeStatus(Integer iModeStatus) {
        this.iModeStatus = iModeStatus;
    }

    public Integer getiFaultStatus() {
        return iFaultStatus;
    }

    public void setiFaultStatus(Integer iFaultStatus) {
        this.iFaultStatus = iFaultStatus;
    }

    public Integer getiOnLine() {
        return iOnLine;
    }

    public void setiOnLine(Integer iOnLine) {
        this.iOnLine = iOnLine;
    }

    public String getvElevatorCode() {
        return vElevatorCode;
    }

    public void setvElevatorCode(String vElevatorCode) {
        this.vElevatorCode = vElevatorCode;
    }

    public Integer getiElevatorType() {
        return iElevatorType;
    }

    public void setiElevatorType(Integer iElevatorType) {
        this.iElevatorType = iElevatorType;
    }

    public Integer getiInstallStatus() {
        return iInstallStatus;
    }

    public void setiInstallStatus(Integer iInstallStatus) {
        this.iInstallStatus = iInstallStatus;
    }

    public String getvBuildingId() {
        return vBuildingId;
    }

    public void setvBuildingId(String vBuildingId) {
        this.vBuildingId = vBuildingId;
    }

    public String getvCodeOrAddress() {
        return vCodeOrAddress;
    }

    public void setvCodeOrAddress(String vCodeOrAddress) {
        this.vCodeOrAddress = vCodeOrAddress;
    }

    public String getvHttpPtCode() {
        return vHttpPtCode;
    }

    public void setvHttpPtCode(String vHttpPtCode) {
        this.vHttpPtCode = vHttpPtCode;
    }

    public String getElevatorId() {
        return elevatorId;
    }

    public void setElevatorId(String elevatorId) {
        this.elevatorId = elevatorId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
