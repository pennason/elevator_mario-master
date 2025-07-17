package com.shmashine.userclientapplets.entity;

import com.shmashine.common.entity.base.PageListParams;

/**
 * 故障查询实体类
 */
public class SearchFaultModule extends PageListParams {

    /**
     * 电梯id
     */
    private String elevatorId;

    /**
     * 电梯编号
     */
    private String vElevatorCode;

    /**
     * 状态（0:故障中、1:已恢复）
     */
    private Integer iStatus;

    /**
     * 电梯类型
     */
    private Integer iElevatorType;

    /**
     * 项目id
     */
    private String vProjectId;

    /**
     * 小区id
     */
    private String villageId;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 故障上报时间
     */
    private String dtReportTime;

    /**
     * 故障结束时间
     */
    private String dtEndTime;


    /**
     * 故障状态 0 未故障，1 故障
     */
    private Integer iFaultStatus;

    /**
     * 故障类型
     */
    private String iFaultType;

    /**
     * 不文明行为标识 0:故障 1:不文明行为
     */
    private Integer iUncivilizedBehaviorFlag;

    /**
     * 故障来源
     */
    private Integer vEventType;

    /**
     * 维保状态，0：正常；1：超期
     */
    private Integer overdue;

    public String getElevatorId() {
        return elevatorId;
    }

    public void setElevatorId(String elevatorId) {
        this.elevatorId = elevatorId;
    }

    public String getvElevatorCode() {
        return vElevatorCode;
    }

    public void setvElevatorCode(String vElevatorCode) {
        this.vElevatorCode = vElevatorCode;
    }

    public Integer getiStatus() {
        return iStatus;
    }

    public void setiStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    public Integer getiElevatorType() {
        return iElevatorType;
    }

    public void setiElevatorType(Integer iElevatorType) {
        this.iElevatorType = iElevatorType;
    }

    public String getvProjectId() {
        return vProjectId;
    }

    public void setvProjectId(String vProjectId) {
        this.vProjectId = vProjectId;
    }

    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
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

    public Integer getiFaultStatus() {
        return iFaultStatus;
    }

    public void setiFaultStatus(Integer iFaultStatus) {
        this.iFaultStatus = iFaultStatus;
    }

    public String getiFaultType() {
        return iFaultType;
    }

    public void setiFaultType(String iFaultType) {
        this.iFaultType = iFaultType;
    }

    public Integer getiUncivilizedBehaviorFlag() {
        return iUncivilizedBehaviorFlag;
    }

    public void setiUncivilizedBehaviorFlag(Integer iUncivilizedBehaviorFlag) {
        this.iUncivilizedBehaviorFlag = iUncivilizedBehaviorFlag;
    }

    public Integer getvEventType() {
        return vEventType;
    }

    public void setvEventType(Integer vEventType) {
        this.vEventType = vEventType;
    }

    public Integer getOverdue() {
        return overdue;
    }

    public void setOverdue(Integer overdue) {
        this.overdue = overdue;
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
}
