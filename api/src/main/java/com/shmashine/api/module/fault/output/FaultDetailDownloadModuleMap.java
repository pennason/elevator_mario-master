package com.shmashine.api.module.fault.output;

/**
 * @PackgeName: com.shmashine.api.module.fault
 * @ClassName: FaultDetailDownloadModuleMap
 * @Date: 2020/7/3016:34
 * @Author: LiuLiFu
 * @Description: 打印实体
 */
public class FaultDetailDownloadModuleMap {

    private String faultId;
    private String elevatorCode;
    private String elevatorName;
    private String levelName;
    private String address;
    private String reportTime;
    private String faultType;
    private String faultName;
    private String faultNum;
    private String modeStatus;
    private String statusName;
    private String eType;

    public String getFaultId() {
        return faultId;
    }

    public void setFaultId(String faultId) {
        this.faultId = faultId;
    }

    public String getElevatorCode() {
        return elevatorCode;
    }

    public void setElevatorCode(String elevatorCode) {
        this.elevatorCode = elevatorCode;
    }

    public String getElevatorName() {
        return elevatorName;
    }

    public void setElevatorName(String elevatorName) {
        this.elevatorName = elevatorName;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getFaultType() {
        return faultType;
    }

    public void setFaultType(String faultType) {
        this.faultType = faultType;
    }

    public String getFaultName() {
        return faultName;
    }

    public void setFaultName(String faultName) {
        this.faultName = faultName;
    }

    public String getFaultNum() {
        return faultNum;
    }

    public void setFaultNum(String faultNum) {
        this.faultNum = faultNum;
    }

    public String getModeStatus() {
        return modeStatus;
    }

    public void setModeStatus(String modeStatus) {
        this.modeStatus = modeStatus;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getEType() {
        return eType;
    }

    public void setEType(String eType) {
        this.eType = eType;
    }
}
