package com.shmashine.api.module.elevator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.shmashine.common.entity.base.PageListParams;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 电梯列表查询参数类
 *
 * @PackgeName: com.shmashine.api.module.elevator
 * @ClassName: SearchElevatorModule
 * @Date: 2020/6/1215:43
 * @Author: LiuLiFu
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SearchElevatorModule extends PageListParams {


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
     * 设备安装状态数组
     */
    private List<Integer> installStatusArray;

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
     * 多个小区ID
     */
    private Collection<String> villageIds;
    /**
     * 楼宇id
     */
    private String vBuildingId;


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
     * 设备软件版本号
     */
    private String masterVersion;

    /**
     * 编号和地址联合
     */
    private String vCodeOrAddress;

    /**
     * 推送平台
     */
    private String vHttpPtCode;

    private String eType;

    private String vDevicePosition;

    private String vMasterVersion;


    /**
     * 群租识别标记 1：开启，0关闭
     */
    private Integer groupLeasingStatus;

    /**
     * 群租识别结果 0：未确认 1：取证中 2：已确认
     */
    private Integer groupLeasingResult;

    /**
     * 权限列表
     */
    private ArrayList<String> permissionDeptIds;

    /**
     * 项目IDS
     */
    private List<String> projectIds;


    public ArrayList<String> getPermissionDeptIds() {
        return permissionDeptIds;
    }

    public void setPermissionDeptIds(ArrayList<String> permissionDeptIds) {
        this.permissionDeptIds = permissionDeptIds;
    }

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

    public Collection<String> getVillageIds() {
        return villageIds;
    }

    public void setVillageIds(Collection<String> villageIds) {
        this.villageIds = villageIds;
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

    public String geteType() {
        return eType;
    }

    public void seteType(String eType) {
        this.eType = eType;
    }

    public String getvDevicePosition() {
        return vDevicePosition;
    }

    public void setvDevicePosition(String vDevicePosition) {
        this.vDevicePosition = vDevicePosition;
    }

    public String getvMasterVersion() {
        return vMasterVersion;
    }

    public void setvMasterVersion(String vMasterVersion) {
        this.vMasterVersion = vMasterVersion;
    }

    public Integer getGroupLeasingStatus() {
        return groupLeasingStatus;
    }

    public void setGroupLeasingStatus(Integer groupLeasingStatus) {
        this.groupLeasingStatus = groupLeasingStatus;
    }

    public Integer getGroupLeasingResult() {
        return groupLeasingResult;
    }

    public void setGroupLeasingResult(Integer groupLeasingResult) {
        this.groupLeasingResult = groupLeasingResult;
    }

    public String getMasterVersion() {
        return masterVersion;
    }

    public void setMasterVersion(String masterVersion) {
        this.masterVersion = masterVersion;
    }
}
