package com.shmashine.api.module.fault.input;

import java.util.List;

import com.shmashine.common.entity.base.PageListParams;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @PackgeName: com.shmashine.api.module.fault.input
 * @ClassName: searchFaultModule
 * @Date: 2020/6/1717:54
 * @Author: LiuLiFu
 * @Description: 故障列表查询所需参数列表
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchFaultModule extends PageListParams {

    /**
     * 电梯编号
     */
    private String vElevatorCode;

    /**
     * 故障上报时间
     */
    private String dtReportTime;

    /**
     * 故障结束时间
     */
    private String dtEndTime;

    /**
     * 不文明行为标识（0:故障、1:不文明行为）
     */
    private Integer iUncivilizedBehaviorFlag;

    /**
     * 状态（0:实时、1:历史）
     */
    private Integer iStatus;

    /**
     * 故障类型
     */
    private String iFaultType;

    /**
     * 电梯安装状态
     */
    private Integer iInstallStatus;

    /**
     * 设备安装状态数组
     */
    private List<Integer> installStatusArray;

    /**
     * 电梯类型
     */
    private Integer iElevatorType;

    /**
     * 事件来源
     */
    private String vEventType;

    /**
     * 前装，后装
     */
    private Integer elevatorInstallType;

    private String vProjectId;

    private String villageId;

    /**
     * 维保状态，0：正常；1：超期
     */
    private Integer overdue;

    private Integer pageIndex;

    private Integer pageSize;

    private String eType;

    /**
     * 00：周 11：月 22：年
     */
    private String timeFlag;

    /**
     * 是否去重
     */
    private Boolean isDistinct;

    /**
     * 物业平台
     */
    private int wuyePlatform;

    /**
     * 是否急修 0 否 1 是
     */
    private int isRepair;

    private List<String> projectIds;

    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
    }

    public Integer getElevatorInstallType() {
        return elevatorInstallType;
    }

    public void setElevatorInstallType(Integer elevatorInstallType) {
        this.elevatorInstallType = elevatorInstallType;
    }

    public String getvEventType() {
        return vEventType;
    }

    public void setvEventType(String vEventType) {
        this.vEventType = vEventType;
    }

    public String getvElevatorCode() {
        return vElevatorCode;
    }

    public void setvElevatorCode(String vElevatorCode) {
        this.vElevatorCode = vElevatorCode;
    }

    public String getDtReportTime() {
        return dtReportTime;
    }

    public void setDtReportTime(String dtReportTime) {
        this.dtReportTime = dtReportTime;
    }

    public String getDtEndTime() {
        return dtEndTime;
    }

    public void setDtEndTime(String dtEndTime) {
        this.dtEndTime = dtEndTime;
    }

    public Integer getiUncivilizedBehaviorFlag() {
        return iUncivilizedBehaviorFlag;
    }

    public void setiUncivilizedBehaviorFlag(Integer iUncivilizedBehaviorFlag) {
        this.iUncivilizedBehaviorFlag = iUncivilizedBehaviorFlag;
    }

    public Integer getiStatus() {
        return iStatus;
    }

    public void setiStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    public String getiFaultType() {
        return iFaultType;
    }

    public void setiFaultType(String iFaultType) {
        this.iFaultType = iFaultType;
    }

    public Integer getiInstallStatus() {
        return iInstallStatus;
    }

    public void setiInstallStatus(Integer iInstallStatus) {
        this.iInstallStatus = iInstallStatus;
    }

    public Integer getiElevatorType() {
        return iElevatorType;
    }

    public void setiElevatorType(Integer iElevatorType) {
        this.iElevatorType = iElevatorType;
    }

    @Override
    public Integer getPageIndex() {
        return pageIndex;
    }

    @Override
    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    @Override
    public Integer getPageSize() {
        return pageSize;
    }

    @Override
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getvProjectId() {
        return vProjectId;
    }

    public void setvProjectId(String vProjectId) {
        this.vProjectId = vProjectId;
    }

    public Integer getOverdue() {
        return overdue;
    }

    public void setOverdue(Integer overdue) {
        this.overdue = overdue;
    }

    public String geteType() {
        return eType;
    }

    public void seteType(String eType) {
        this.eType = eType;
    }

    public String getTimeFlag() {
        return timeFlag;
    }

    public void setTimeFlag(String timeFlag) {
        this.timeFlag = timeFlag;
    }

    public Boolean getDistinct() {
        return isDistinct == null ? false : isDistinct;
    }

    public void setDistinct(Boolean distinct) {
        isDistinct = distinct;
    }

    public int getWuyePlatform() {
        return wuyePlatform;
    }

    public void setWuyePlatform(int wuyePlatform) {
        this.wuyePlatform = wuyePlatform;
    }

    public int getIsRepair() {
        return isRepair;
    }

    public void setIsRepair(int isRepair) {
        this.isRepair = isRepair;
    }

    @Override
    public String toString() {
        return "SearchFaultModule{" +
                "vElevatorCode='" + vElevatorCode + '\'' +
                ", dtReportTime='" + dtReportTime + '\'' +
                ", dtEndTime='" + dtEndTime + '\'' +
                ", iUncivilizedBehaviorFlag=" + iUncivilizedBehaviorFlag +
                ", iStatus=" + iStatus +
                ", iFaultType='" + iFaultType + '\'' +
                ", iInstallStatus=" + iInstallStatus +
                ", iElevatorType=" + iElevatorType +
                ", vEventType='" + vEventType + '\'' +
                ", elevatorInstallType=" + elevatorInstallType +
                ", vProjectId='" + vProjectId + '\'' +
                ", villageId='" + villageId + '\'' +
                ", overdue=" + overdue +
                ", pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                '}';
    }
    private  List<String> elevatorCode;
}
