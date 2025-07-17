package com.shmashine.fault.fault.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 故障记录临时表(TblFaultTemp)实体类
 *
 * @author makejava
 * @since 2020-06-29 18:40:09
 */
public class TblFaultTemp implements Serializable {
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
     * 设备统一注册码
     */
    private String registerNumber;
    /**
     * 电梯安装地址
     */
    private String vAddress;
    /**
     * 故障上报时间
     */
    private Date dtReportTime;
    /**
     * 故障结束时间
     */
    private Date dtEndTime;
    /**
     * 故障类型
     */
    private String iFaultType;
    /**
     * 故障名称
     */
    private String vFaultName;

    /**
     * 故障类型
     */
    private String vFaultSecondType;
    /**
     * 故障名称
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
     * 推送平台
     */
    private String httpPtCodes;
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
     * 楼层信息
     */
    private Integer floor;
    /**
     * 确认标识 0-未确认，1-已确认故障， 2-已确认非故障, 3-自动消除
     */
    private Integer iConfirmStatus;
    /**
     * 手动恢复故障 -2 无任何操作，-1 手动恢复故障，需通知设备，0 失败，1成功，2故障不存在（后面三个都是通知终端后返回结果 ）
     */
    private Integer iManualClear;
    /**
     * 创建时间
     */
    private Date dtCreateTime;
    /**
     * 修改时间
     */
    private Date dtModifyTime;
    /**
     * 确认人
     */
    private String vModifyUserId;
    /**
     * 删除标识 0-未删除，1-已删除
     */
    private Integer iDelFlag;


    public String getVFaultId() {
        return vFaultId;
    }

    public void setVFaultId(String vFaultId) {
        this.vFaultId = vFaultId;
    }

    public String getVElevatorId() {
        return vElevatorId;
    }

    public void setVElevatorId(String vElevatorId) {
        this.vElevatorId = vElevatorId;
    }

    public String getVElevatorCode() {
        return vElevatorCode;
    }

    public void setVElevatorCode(String vElevatorCode) {
        this.vElevatorCode = vElevatorCode;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getVAddress() {
        return vAddress;
    }

    public void setVAddress(String vAddress) {
        this.vAddress = vAddress;
    }

    public Date getDtReportTime() {
        return dtReportTime;
    }

    public void setDtReportTime(Date dtReportTime) {
        this.dtReportTime = dtReportTime;
    }

    public Date getDtEndTime() {
        return dtEndTime;
    }

    public void setDtEndTime(Date dtEndTime) {
        this.dtEndTime = dtEndTime;
    }

    public String getIFaultType() {
        return iFaultType;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public void setIFaultType(String iFaultType) {
        this.iFaultType = iFaultType;
    }

    public String getVFaultSecondType() {
        return vFaultSecondType;
    }

    public void setVFaultSecondType(String vFaultSecondType) {
        this.vFaultSecondType = vFaultSecondType;
    }

    public String getVFaultSecondName() {
        return vFaultSecondName;
    }

    public void setVFaultSecondName(String vFaultSecondName) {
        this.vFaultSecondName = vFaultSecondName;
    }

    public String getVFaultName() {
        return vFaultName;
    }

    public void setVFaultName(String vFaultName) {
        this.vFaultName = vFaultName;
    }

    public Integer getILevel() {
        return iLevel;
    }

    public void setILevel(Integer iLevel) {
        this.iLevel = iLevel;
    }

    public String getILevelName() {
        return iLevelName;
    }

    public void setILevelName(String iLevelName) {
        this.iLevelName = iLevelName;
    }

    public String getHttpPtCodes() {
        return httpPtCodes;
    }

    public void setHttpPtCodes(String httpPtCodes) {
        this.httpPtCodes = httpPtCodes;
    }

    public String getIFaultMessage() {
        return iFaultMessage;
    }

    public void setIFaultMessage(String iFaultMessage) {
        this.iFaultMessage = iFaultMessage;
    }

    public Integer getIFaultNum() {
        return iFaultNum;
    }

    public void setIFaultNum(Integer iFaultNum) {
        this.iFaultNum = iFaultNum;
    }

    public Integer getIUncivilizedBehaviorFlag() {
        return iUncivilizedBehaviorFlag;
    }

    public void setIUncivilizedBehaviorFlag(Integer iUncivilizedBehaviorFlag) {
        this.iUncivilizedBehaviorFlag = iUncivilizedBehaviorFlag;
    }

    public Integer getIModeStatus() {
        return iModeStatus;
    }

    public void setIModeStatus(Integer iModeStatus) {
        this.iModeStatus = iModeStatus;
    }

    public Integer getIStatus() {
        return iStatus;
    }

    public void setIStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    public Integer getIConfirmStatus() {
        return iConfirmStatus;
    }

    public void setIConfirmStatus(Integer iConfirmStatus) {
        this.iConfirmStatus = iConfirmStatus;
    }

    public Integer getIManualClear() {
        return iManualClear;
    }

    public void setIManualClear(Integer iManualClear) {
        this.iManualClear = iManualClear;
    }

    public Date getDtCreateTime() {
        return dtCreateTime;
    }

    public void setDtCreateTime(Date dtCreateTime) {
        this.dtCreateTime = dtCreateTime;
    }

    public Date getDtModifyTime() {
        return dtModifyTime;
    }

    public void setDtModifyTime(Date dtModifyTime) {
        this.dtModifyTime = dtModifyTime;
    }

    public String getVModifyUserId() {
        return vModifyUserId;
    }

    public void setVModifyUserId(String vModifyUserId) {
        this.vModifyUserId = vModifyUserId;
    }

    public Integer getIDelFlag() {
        return iDelFlag;
    }

    public void setIDelFlag(Integer iDelFlag) {
        this.iDelFlag = iDelFlag;
    }

}