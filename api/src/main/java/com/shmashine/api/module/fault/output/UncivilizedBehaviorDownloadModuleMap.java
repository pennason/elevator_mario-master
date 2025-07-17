package com.shmashine.api.module.fault.output;

/**
 * 导出不文明行为列表
 *
 * @author Dean Winchester
 */
public class UncivilizedBehaviorDownloadModuleMap {
    //电梯编号
    private String elevatorCode;
    //级别
    private String levelName;

    //安装地址
    private String address;

    //上报时间
    private String reportTime;

    //故障类型
    private String faultName;

    //上报次数
    private String faultNum;

    //服务模式
    private String modeStatus;

    //不文明状态
    private String statusName;

    public String getElevatorCode() {
        return elevatorCode;
    }

    public void setElevatorCode(String elevatorCode) {
        this.elevatorCode = elevatorCode;
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
}
