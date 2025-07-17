package com.shmashine.common.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * 故障记录临时表(TblFaultTemp)实体类
 *
 * @author makejava
 * @since 2020-06-29 18:40:09
 */
public class TblFaultTemp implements Serializable {
    @Serial
    private static final long serialVersionUID = -60776169350000806L;
    /**
     * 临时故障id
     */
    private String vFaultId;
    /**
     * 电梯编号
     */
    private String vElevatorId;
    /**
     * 电梯编号
     */
    private String vElevatorCode;
    /**
     * 电梯安装地址
     */
    private String vAddress;
    /**
     * 故障上报时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtReportTime;
    /**
     * 故障结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtEndTime;
    /**
     * 故障类型
     */
    private Integer iFaultType;
    /**
     * 故障名称
     */
    private String vFaultName;
    /**
     * 故障级别（严重Lv1、重要Lv2、中等Lv3、普通Lv4）
     */
    private Integer iLevel;
    /**
     * 故障级别名称
     */
    private String iLevelName;
    /**
     * 故障报文
     */
    private String iFaultMessage;
    /**
     * 故障次数
     */
    private Integer iFaultNum;
    /**
     * 不文明行为标识，0：故障，1：不文明行为
     */
    private Integer iUncivilizedBehaviorFlag;
    /**
     * 服务模式(0:正常运行, 1:停止服务，2:检修模式)
     */
    private Integer iModeStatus;
    /**
     * 状态（0:故障中、1:已恢复）
     */
    private Integer iStatus;
    /**
     * 确认标识 0-未确认，1-已确认
     */
    private Integer iConfirmStatus;
    /**
     * 手动恢复故障 -2 无任何操作，-1 手动恢复故障，需通知设备，0 失败，1成功，2故障不存在（后面三个都是通知终端后返回结果 ）
     */
    private Integer iManualClear;
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
     * 确认人
     */
    private String vModifyUserId;
    /**
     * 删除标识 0-未删除，1-已删除
     */
    private Integer iDelFlag;

    /**
     * 图像识别结果(1:成功，0：未识别)
     */
    private Integer recognitionResult;

    @JsonProperty("vFaultId")
    public String getVFaultId() {
        return vFaultId;
    }

    public void setVFaultId(String vFaultId) {
        this.vFaultId = vFaultId;
    }

    public String getvElevatorId() {
        return vElevatorId;
    }

    public void setvElevatorId(String vElevatorId) {
        this.vElevatorId = vElevatorId;
    }

    public String getVElevatorCode() {
        return vElevatorCode;
    }

    public void setVElevatorCode(String vElevatorCode) {
        this.vElevatorCode = vElevatorCode;
    }

    @JsonProperty("vAddress")
    public String getVAddress() {
        return vAddress;
    }

    public void setVAddress(String vAddress) {
        this.vAddress = vAddress;
    }

    @JsonProperty("dtReportTime")
    public Date getDtReportTime() {
        return dtReportTime;
    }

    public void setDtReportTime(Date dtReportTime) {
        this.dtReportTime = dtReportTime;
    }

    @JsonProperty("dtEndTime")
    public Date getDtEndTime() {
        return dtEndTime;
    }

    public void setDtEndTime(Date dtEndTime) {
        this.dtEndTime = dtEndTime;
    }

    @JsonProperty("iFaultType")
    public Integer getIFaultType() {
        return iFaultType;
    }

    public void setIFaultType(Integer iFaultType) {
        this.iFaultType = iFaultType;
    }

    @JsonProperty("vFaultName")
    public String getVFaultName() {
        return vFaultName;
    }

    public void setVFaultName(String vFaultName) {
        this.vFaultName = vFaultName;
    }

    @JsonProperty("iLevel")
    public Integer getILevel() {
        return iLevel;
    }

    public void setILevel(Integer iLevel) {
        this.iLevel = iLevel;
    }

    @JsonProperty("iLevelName")
    public String getILevelName() {
        return iLevelName;
    }

    public void setILevelName(String iLevelName) {
        this.iLevelName = iLevelName;
    }

    @JsonProperty("iFaultMessage")
    public String getIFaultMessage() {
        return iFaultMessage;
    }

    public void setIFaultMessage(String iFaultMessage) {
        this.iFaultMessage = iFaultMessage;
    }

    @JsonProperty("iFaultNum")
    public Integer getIFaultNum() {
        return iFaultNum;
    }

    public void setIFaultNum(Integer iFaultNum) {
        this.iFaultNum = iFaultNum;
    }

    @JsonProperty("iUncivilizedBehaviorFlag")
    public Integer getIUncivilizedBehaviorFlag() {
        return iUncivilizedBehaviorFlag;
    }

    public void setIUncivilizedBehaviorFlag(Integer iUncivilizedBehaviorFlag) {
        this.iUncivilizedBehaviorFlag = iUncivilizedBehaviorFlag;
    }

    @JsonProperty("iModeStatus")
    public Integer getIModeStatus() {
        return iModeStatus;
    }

    public void setIModeStatus(Integer iModeStatus) {
        this.iModeStatus = iModeStatus;
    }

    @JsonProperty("iStatus")
    public Integer getIStatus() {
        return iStatus;
    }

    public void setIStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    @JsonProperty("iConfirmStatus")
    public Integer getIConfirmStatus() {
        return iConfirmStatus;
    }

    public void setIConfirmStatus(Integer iConfirmStatus) {
        this.iConfirmStatus = iConfirmStatus;
    }

    @JsonProperty("iManualClear")
    public Integer getIManualClear() {
        return iManualClear;
    }

    public void setIManualClear(Integer iManualClear) {
        this.iManualClear = iManualClear;
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

    @JsonProperty("vModifyUserId")
    public String getVModifyUserId() {
        return vModifyUserId;
    }

    public void setVModifyUserId(String vModifyUserId) {
        this.vModifyUserId = vModifyUserId;
    }

    @JsonProperty("iDelFlag")
    public Integer getIDelFlag() {
        return iDelFlag;
    }

    public void setIDelFlag(Integer iDelFlag) {
        this.iDelFlag = iDelFlag;
    }

    public Integer getRecognitionResult() {
        return recognitionResult;
    }

    public void setRecognitionResult(Integer recognitionResult) {
        this.recognitionResult = recognitionResult;
    }

}