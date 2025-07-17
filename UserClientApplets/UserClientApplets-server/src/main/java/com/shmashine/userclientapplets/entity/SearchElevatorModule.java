package com.shmashine.userclientapplets.entity;


import com.shmashine.common.entity.base.PageListParams;

/**
 * 电梯查询参数
 *
 * @author jiangheng
 * @version V1.0.0 - 2022/2/9 10:58
 */
public class SearchElevatorModule extends PageListParams {

    /**
     * 电梯id
     */
    private String elevatorId;

    /**
     * 电梯编号
     */
    private String vElevatorCode;

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
     * 电梯地址
     */
    private String vAddress;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 故障状态 0 未故障，1 故障
     */
    private Integer iFaultStatus;

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

    public Integer getiFaultStatus() {
        return iFaultStatus;
    }

    public void setiFaultStatus(Integer iFaultStatus) {
        this.iFaultStatus = iFaultStatus;
    }

    public String getvAddress() {
        return vAddress;
    }

    public void setvAddress(String vAddress) {
        this.vAddress = vAddress;
    }
}
