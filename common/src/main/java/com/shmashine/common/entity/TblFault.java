package com.shmashine.common.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 故障记录表(TblFault)实体类
 *
 * @author makejava
 * @since 2020-09-02 18:28:00
 */
public class TblFault implements Serializable {

    @Serial
    private static final long serialVersionUID = -88807104041037701L;

    /**
     * 故障唯一id
     */
    private String vFaultId;
    /**
     * 电梯ID
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
     * 故障上报日期（故障统计使用）
     */
    private Object dReportDate;
    /**
     * 故障类型
     */
    private String iFaultType;
    /**
     * 故障名称
     */
    private String vFaultName;
    /**
     * 故障子类型
     */
    private String vFaultSecondType;
    /**
     * 故障子类型名称
     */
    private String vFaultSecondName;
    /**
     * 故障级别（严重Lv1、重要Lv2、中等Lv3、普通Lv4）
     */
    private Integer iLevel;
    /**
     * 故障级别名称
     */
    private String iLevelName;
    /**
     * 故障次数
     */
    private Integer iFaultNum;

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    private Integer floor;

    /**
     * 楼层
     */
    private Integer iFloor;
    /**
     * 状态（0:故障中、1:已恢复）
     */
    private Integer iStatus;
    /**
     * 不文明行为标识，0：故障，1：不文明行为
     */
    private Integer iUncivilizedBehaviorFlag;
    /**
     * 服务模式(0:正常运行, 1:检修模式，2:停止服务)
     */
    private Integer iModeStatus;
    /**
     * 持续时间
     */
    private Integer iDurationTime;
    /**
     * 手动恢复故障 -2 无任何操作，-1 手动恢复故障，需通知设备，0 失败，1成功，2故障不存在（后面三个都是通知终端后返回结果 ）
     */
    private Integer iManualClear;
    /**
     * 0不推送数据 1推送数据
     */
    private Integer iIsUserVisible;
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
     * 创建记录用户
     */
    private String vCreateUserId;
    /**
     * 修改记录用户
     */
    private String vModifyUserId;
    /**
     * 删除标识 0-未删除，1-已删除
     */
    private Integer iDelFlag;
    /**
     * 故障事件来源
     */
    private String vEventChannel;

    @JsonProperty("vFaultId")
    public String getVFaultId() {
        return vFaultId;
    }

    public void setVFaultId(String vFaultId) {
        this.vFaultId = vFaultId;
    }

    @JsonProperty("vElevatorId")
    public String getVElevatorId() {
        return vElevatorId;
    }

    public void setVElevatorId(String vElevatorId) {
        this.vElevatorId = vElevatorId;
    }

    @JsonProperty("vElevatorCode")
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

    @JsonProperty("dReportDate")
    public Object getDReportDate() {
        return dReportDate;
    }

    public void setDReportDate(Object dReportDate) {
        this.dReportDate = dReportDate;
    }

    @JsonProperty("iFaultType")
    public String getIFaultType() {
        return iFaultType;
    }

    public void setIFaultType(String iFaultType) {
        this.iFaultType = iFaultType;
    }

    @JsonProperty("vFaultName")
    public String getVFaultName() {
        return vFaultName;
    }

    public void setVFaultName(String vFaultName) {
        this.vFaultName = vFaultName;
    }

    @JsonProperty("vFaultSecondType")
    public String getvFaultSecondType() {
        return vFaultSecondType;
    }

    public void setvFaultSecondType(String vFaultSecondType) {
        this.vFaultSecondType = vFaultSecondType;
    }

    @JsonProperty("vFaultSecondName")
    public String getvFaultSecondName() {
        return vFaultSecondName;
    }

    public void setvFaultSecondName(String vFaultSecondName) {
        this.vFaultSecondName = vFaultSecondName;
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

    @JsonProperty("iFaultNum")
    public Integer getIFaultNum() {
        return iFaultNum;
    }

    public void setIFaultNum(Integer iFaultNum) {
        this.iFaultNum = iFaultNum;
    }

    @JsonProperty("iStatus")
    public Integer getIStatus() {
        return iStatus;
    }

    public void setIStatus(Integer iStatus) {
        this.iStatus = iStatus;
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

    @JsonProperty("iDurationTime")
    public Integer getIDurationTime() {
        return iDurationTime;
    }

    public void setIDurationTime(Integer iDurationTime) {
        this.iDurationTime = iDurationTime;
    }

    @JsonProperty("iManualClear")
    public Integer getIManualClear() {
        return iManualClear;
    }

    public void setIManualClear(Integer iManualClear) {
        this.iManualClear = iManualClear;
    }

    @JsonProperty("iIsUserVisible")
    public Integer getIIsUserVisible() {
        return iIsUserVisible;
    }

    public void setIIsUserVisible(Integer iIsUserVisible) {
        this.iIsUserVisible = iIsUserVisible;
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

    @JsonProperty("vCreateUserId")
    public String getVCreateUserId() {
        return vCreateUserId;
    }

    public void setVCreateUserId(String vCreateUserId) {
        this.vCreateUserId = vCreateUserId;
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

    @JsonProperty("vEventChannel")
    public String getVEventChannel() {
        return vEventChannel;
    }

    public void setVEventChannel(String vEventChannel) {
        this.vEventChannel = vEventChannel;
    }

    public Integer getiFloor() {
        return iFloor;
    }

    public void setiFloor(Integer iFloor) {
        this.iFloor = iFloor;
    }

}