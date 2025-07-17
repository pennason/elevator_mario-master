package com.shmashine.api.module.ruijin;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shmashine.common.entity.base.PageListParams;

public class FaultStatisticalQuantitySearchModule extends PageListParams {
    /**
     * 注册编号
     */
    private String register_number;
    /** 00：周 11：月 22：年 */
    /**
     * 小区Id
     */
    private String villageId;
    /**
     * 00：周 11：月 22：年
     */
    private String timeFlag;
    /**
     * fault Type
     */
    private String faultType;

    /**
     * 项目id
     */
    private String vProjectId;

    /**
     * 电梯code
     */
    private String elevatorCode;

    /**
     * 当前状态1：渠道上报 2：新建工单 3：已接单、4：已签到 5：已完成 6：已确认 7： 误报 8：事故
     */
    private Integer currentStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * faultType list
     */
    private List<String> faultTypes;

    public String getRegister_number() {
        return register_number;
    }

    public void setRegister_number(String register_number) {
        this.register_number = register_number;
    }

    public String getFaultType() {
        return faultType;
    }

    public void setFaultType(String faultType) {
        this.faultType = faultType;
    }

    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
    }

    public String getTimeFlag() {
        return timeFlag;
    }

    public void setTimeFlag(String timeFlag) {
        this.timeFlag = timeFlag;
    }

    public String getvProjectId() {
        return vProjectId;
    }

    public void setvProjectId(String vProjectId) {
        this.vProjectId = vProjectId;
    }

    public String getElevatorCode() {
        return elevatorCode;
    }

    public void setElevatorCode(String elevatorCode) {
        this.elevatorCode = elevatorCode;
    }

    public Integer getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Integer currentStatus) {
        this.currentStatus = currentStatus;
    }

    public List<String> getFaultTypes() {
        return faultTypes;
    }

    public void setFaultTypes(List<String> faultTypes) {
        this.faultTypes = faultTypes;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
