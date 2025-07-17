package com.shmashine.common.entity;


import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * 设备表(TblDevice)实体类
 *
 * @author little.li
 * @since 2020-06-12 13:56:11
 */
public class TblDevice implements Serializable {

    @Serial
    private static final long serialVersionUID = 885091086381299674L;

    /**
     * 设备唯一id
     */
    private String vDeviceId;
    /**
     * 设备编号
     */
    private String vDeviceCode;
    /**
     * 设备类型
     */
    private String vSensorType;
    /**
     * 电梯id
     */
    private String vElevatorId;
    /**
     * 设备安装电梯编号
     */
    private String vElevatorCode;
    /**
     * 设备安装电梯位置
     */
    private String vElevatorAddress;
    /**
     * 安装状态 0-未安装，1-已安装，2-已拆除
     */
    private Integer iInstallStatus;
    /**
     * 设备开始运行时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtStartRunTime;
    /**
     * 在线状态 0-离线，1-在线
     */
    private Integer iOnlineStatus;
    /**
     * 最近上线时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtOnlineTime;
    /**
     * 最近离线时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtOfflineTime;
    /**
     * 最近重启时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtRebootTime;
    /**
     * 连接server ip
     */
    private String vServerIp;
    /**
     * 设备电量
     */
    private Integer iBattery;
    /**
     * 硬件版本
     */
    private String vHwVersion;
    /**
     * 软件版本
     */
    private String vSwVersion;
    /**
     * 固件版本号
     */
    private String vFwVersion;
    /**
     * 主设备软件版本
     */
    private String vMasterVersion;
    /**
     * 从设备软件版本
     */
    private String vSlaveVersion;
    /**
     * 固件更新方式
     */
    private String vUpdateMethod;
    /**
     * sim卡ccid
     */
    private String vCcid;
    /**
     * sim卡imei
     */
    private String vImei;
    /**
     * 网络（0：宽带 1：定向卡  2：非定向卡）
     */
    private Integer iNetworkType;
    /**
     * 设备用户名
     */
    private String vDeviceUsername;
    /**
     * 设备密码
     */
    private String vDevicePassword;
    /**
     * 阿里云IOT对接key
     */
    private String vAliiotKey;
    /**
     * 阿里云IOT对接secret
     */
    private String vAliiotSecret;
    /**
     * 阿里云IOT对接topic
     */
    private String vAliiotTopic;
    /**
     * 电信平台标识（小慧NB设备）
     */
    private String vPlatformType;
    /**
     * 电信平台标识（小慧NB设备）
     */
    private String vXhnbToUsr;
    /**
     * 消毒灯开始工作时间（小慧NB设备）
     */
    private String vXhnbBrakeStartTime;
    /**
     * 消毒灯停止工作时间（小慧NB设备）
     */
    private String vXhnbBrakeStopTime;
    /**
     * 消毒灯工作延时时间（小慧NB设备）
     */
    private String vXhnbBrakeDelayTime;
    /**
     * 消毒灯工作持续时间（小慧NB设备）
     */
    private String vXhnbBrakeContinueTime;
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
     * 终端类型
     */
    private String eType;

    /**
     * 协议版本
     */
    private String protocalVersion;

    /**
     * 设备安装位置
     */
    private String vDevicePosition;


    @JsonProperty("vDeviceId")
    public String getVDeviceId() {
        return vDeviceId;
    }

    public void setVDeviceId(String vDeviceId) {
        this.vDeviceId = vDeviceId;
    }

    @JsonProperty("vDeviceCode")
    public String getVDeviceCode() {
        return vDeviceCode;
    }

    public void setVDeviceCode(String vDeviceCode) {
        this.vDeviceCode = vDeviceCode;
    }

    @JsonProperty("vSensorType")
    public String getVSensorType() {
        return vSensorType;
    }

    public void setVSensorType(String vSensorType) {
        this.vSensorType = vSensorType;
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

    @JsonProperty("vElevatorAddress")
    public String getVElevatorAddress() {
        return vElevatorAddress;
    }

    public void setVElevatorAddress(String vElevatorAddress) {
        this.vElevatorAddress = vElevatorAddress;
    }

    @JsonProperty("iInstallStatus")
    public Integer getIInstallStatus() {
        return iInstallStatus;
    }

    public void setIInstallStatus(Integer iInstallStatus) {
        this.iInstallStatus = iInstallStatus;
    }

    @JsonProperty("dtStartRunTime")
    public Date getDtStartRunTime() {
        return dtStartRunTime;
    }

    public void setDtStartRunTime(Date dtStartRunTime) {
        this.dtStartRunTime = dtStartRunTime;
    }

    @JsonProperty("iOnlineStatus")
    public Integer getIOnlineStatus() {
        return iOnlineStatus;
    }

    public void setIOnlineStatus(Integer iOnlineStatus) {
        this.iOnlineStatus = iOnlineStatus;
    }

    @JsonProperty("dtOnlineTime")
    public Date getDtOnlineTime() {
        return dtOnlineTime;
    }

    public void setDtOnlineTime(Date dtOnlineTime) {
        this.dtOnlineTime = dtOnlineTime;
    }

    @JsonProperty("dtOfflineTime")
    public Date getDtOfflineTime() {
        return dtOfflineTime;
    }

    public void setDtOfflineTime(Date dtOfflineTime) {
        this.dtOfflineTime = dtOfflineTime;
    }

    @JsonProperty("dtRebootTime")
    public Date getDtRebootTime() {
        return dtRebootTime;
    }

    public void setDtRebootTime(Date dtRebootTime) {
        this.dtRebootTime = dtRebootTime;
    }

    @JsonProperty("vServerIp")
    public String getVServerIp() {
        return vServerIp;
    }

    public void setVServerIp(String vServerIp) {
        this.vServerIp = vServerIp;
    }

    @JsonProperty("iBattery")
    public Integer getIBattery() {
        return iBattery;
    }

    public void setIBattery(Integer iBattery) {
        this.iBattery = iBattery;
    }

    @JsonProperty("vHwVersion")
    public String getVHwVersion() {
        return vHwVersion;
    }

    public void setVHwVersion(String vHwVersion) {
        this.vHwVersion = vHwVersion;
    }

    @JsonProperty("vSwVersion")
    public String getVSwVersion() {
        return vSwVersion;
    }

    public void setVSwVersion(String vSwVersion) {
        this.vSwVersion = vSwVersion;
    }

    @JsonProperty("vFwVersion")
    public String getVFwVersion() {
        return vFwVersion;
    }

    public void setVFwVersion(String vFwVersion) {
        this.vFwVersion = vFwVersion;
    }

    @JsonProperty("vMasterVersion")
    public String getVMasterVersion() {
        return vMasterVersion;
    }

    public void setVMasterVersion(String vMasterVersion) {
        this.vMasterVersion = vMasterVersion;
    }

    @JsonProperty("vSlaveVersion")
    public String getVSlaveVersion() {
        return vSlaveVersion;
    }

    public void setVSlaveVersion(String vSlaveVersion) {
        this.vSlaveVersion = vSlaveVersion;
    }

    @JsonProperty("vUpdateMethod")
    public String getVUpdateMethod() {
        return vUpdateMethod;
    }

    public void setVUpdateMethod(String vUpdateMethod) {
        this.vUpdateMethod = vUpdateMethod;
    }

    @JsonProperty("vCcid")
    public String getVCcid() {
        return vCcid;
    }

    public void setVCcid(String vCcid) {
        this.vCcid = vCcid;
    }

    @JsonProperty("vImei")
    public String getVImei() {
        return vImei;
    }

    public void setVImei(String vImei) {
        this.vImei = vImei;
    }

    @JsonProperty("iNetworkType")
    public Integer getINetworkType() {
        return iNetworkType;
    }

    public void setINetworkType(Integer iNetworkType) {
        this.iNetworkType = iNetworkType;
    }

    @JsonProperty("vDeviceUsername")
    public String getVDeviceUsername() {
        return vDeviceUsername;
    }

    public void setVDeviceUsername(String vDeviceUsername) {
        this.vDeviceUsername = vDeviceUsername;
    }

    @JsonProperty("vDevicePassword")
    public String getVDevicePassword() {
        return vDevicePassword;
    }

    public void setVDevicePassword(String vDevicePassword) {
        this.vDevicePassword = vDevicePassword;
    }

    @JsonProperty("vAliiotKey")
    public String getVAliiotKey() {
        return vAliiotKey;
    }

    public void setVAliiotKey(String vAliiotKey) {
        this.vAliiotKey = vAliiotKey;
    }

    @JsonProperty("vAliiotSecret")
    public String getVAliiotSecret() {
        return vAliiotSecret;
    }

    public void setVAliiotSecret(String vAliiotSecret) {
        this.vAliiotSecret = vAliiotSecret;
    }

    @JsonProperty("vAliiotTopic")
    public String getVAliiotTopic() {
        return vAliiotTopic;
    }

    public void setVAliiotTopic(String vAliiotTopic) {
        this.vAliiotTopic = vAliiotTopic;
    }

    @JsonProperty("vPlatformType")
    public String getVPlatformType() {
        return vPlatformType;
    }

    public void setVPlatformType(String vPlatformType) {
        this.vPlatformType = vPlatformType;
    }

    @JsonProperty("vXhnbToUsr")
    public String getVXhnbToUsr() {
        return vXhnbToUsr;
    }

    public void setVXhnbToUsr(String vXhnbToUsr) {
        this.vXhnbToUsr = vXhnbToUsr;
    }

    @JsonProperty("vXhnbBrakeStartTime")
    public String getVXhnbBrakeStartTime() {
        return vXhnbBrakeStartTime;
    }

    public void setVXhnbBrakeStartTime(String vXhnbBrakeStartTime) {
        this.vXhnbBrakeStartTime = vXhnbBrakeStartTime;
    }

    @JsonProperty("vXhnbBrakeStopTime")
    public String getVXhnbBrakeStopTime() {
        return vXhnbBrakeStopTime;
    }

    public void setVXhnbBrakeStopTime(String vXhnbBrakeStopTime) {
        this.vXhnbBrakeStopTime = vXhnbBrakeStopTime;
    }

    @JsonProperty("vXhnbBrakeDelayTime")
    public String getVXhnbBrakeDelayTime() {
        return vXhnbBrakeDelayTime;
    }

    public void setVXhnbBrakeDelayTime(String vXhnbBrakeDelayTime) {
        this.vXhnbBrakeDelayTime = vXhnbBrakeDelayTime;
    }

    @JsonProperty("vXhnbBrakeContinueTime")
    public String getVXhnbBrakeContinueTime() {
        return vXhnbBrakeContinueTime;
    }

    public void setVXhnbBrakeContinueTime(String vXhnbBrakeContinueTime) {
        this.vXhnbBrakeContinueTime = vXhnbBrakeContinueTime;
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

    @JsonProperty("eType")
    public String geteType() {
        return eType;
    }

    public void seteType(String eType) {
        this.eType = eType;
    }

    @JsonProperty("protocalVersion")
    public String getProtocalVersion() {
        return protocalVersion;
    }

    public void setProtocalVersion(String protocalVersion) {
        this.protocalVersion = protocalVersion;
    }

    @JsonProperty("vDevicePosition")
    public String getvDevicePosition() {
        return vDevicePosition;
    }

    public void setvDevicePosition(String vDevicePosition) {
        this.vDevicePosition = vDevicePosition;
    }

}