package com.shmashine.api.module.fault.input;

import com.shmashine.common.entity.base.PageListParams;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 临时故障 列表查询所需参数列表
 *
 * @author chenx
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SearchFaultTempModule extends PageListParams {

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
     * 确认标识 0-未确认，1-已确认
     */
    private Integer iConfirmStatus;

    /**
     * 故障类型
     */
    private String iFaultType;

    /**
     * 1:成功，0：未识别/失败
     */
    private Integer recognitionResult;

    /**
     * 电梯类型
     */
    private Integer iElevatorType;

    private String eType;

    /**
     * 项目
     */
    private String vProjectId;

    /**
     * 小区
     */
    private String villageId;

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

    public Integer getiConfirmStatus() {
        return iConfirmStatus;
    }

    public void setiConfirmStatus(Integer iConfirmStatus) {
        this.iConfirmStatus = iConfirmStatus;
    }

    public String getiFaultType() {
        return iFaultType;
    }

    public void setiFaultType(String iFaultType) {
        this.iFaultType = iFaultType;
    }

    public Integer getiElevatorType() {
        return iElevatorType;
    }

    public void setiElevatorType(Integer iElevatorType) {
        this.iElevatorType = iElevatorType;
    }

    public Integer getRecognitionResult() {
        return recognitionResult;
    }

    public void setRecognitionResult(Integer recognitionResult) {
        this.recognitionResult = recognitionResult;
    }

    public String geteType() {
        return eType;
    }

    public void seteType(String eType) {
        this.eType = eType;
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
}
