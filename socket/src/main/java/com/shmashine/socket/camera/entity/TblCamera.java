package com.shmashine.socket.camera.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 摄像头表(TblCamera)实体类
 *
 * @author little.li
 * @since 2020-06-20 18:25:25
 */
public class TblCamera implements Serializable {

    private static final long serialVersionUID = 191373830322379863L;

    private String vCameraId;
    /**
     * 所属电梯id
     */
    private String vElevatorId;
    /**
     * 所属电梯编号
     */
    private String vElevatorCode;
    /**
     * 摄像头类型 1：海康，2：雄迈
     */
    private Integer iCameraType;
    /**
     * 摄像头序列号
     */
    private String vSerialNumber;
    /**
     * 用户名
     */
    private String vUsername;
    /**
     * 密码
     */
    private String vPassword;
    /**
     * 云平台序列号
     */
    private String vCloudNumber;
    private String vHlsUrl;
    private String vRtmpUrl;
    private String vPrivateUrl;
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

    @JsonProperty("vCameraId")
    public String getVCameraId() {
        return vCameraId;
    }

    public void setVCameraId(String vCameraId) {
        this.vCameraId = vCameraId;
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

    @JsonProperty("iCameraType")
    public Integer getICameraType() {
        return iCameraType;
    }

    public void setICameraType(Integer iCameraType) {
        this.iCameraType = iCameraType;
    }

    @JsonProperty("vSerialNumber")
    public String getVSerialNumber() {
        return vSerialNumber;
    }

    public void setVSerialNumber(String vSerialNumber) {
        this.vSerialNumber = vSerialNumber;
    }

    @JsonProperty("vUsername")
    public String getVUsername() {
        return vUsername;
    }

    public void setVUsername(String vUsername) {
        this.vUsername = vUsername;
    }

    @JsonProperty("vPassword")
    public String getVPassword() {
        return vPassword;
    }

    public void setVPassword(String vPassword) {
        this.vPassword = vPassword;
    }

    @JsonProperty("vCloudNumber")
    public String getVCloudNumber() {
        return vCloudNumber;
    }

    public void setVCloudNumber(String vCloudNumber) {
        this.vCloudNumber = vCloudNumber;
    }

    @JsonProperty("vHlsUrl")
    public String getVHlsUrl() {
        return vHlsUrl;
    }

    public void setVHlsUrl(String vHlsUrl) {
        this.vHlsUrl = vHlsUrl;
    }

    @JsonProperty("vRtmpUrl")
    public String getVRtmpUrl() {
        return vRtmpUrl;
    }

    public void setVRtmpUrl(String vRtmpUrl) {
        this.vRtmpUrl = vRtmpUrl;
    }

    @JsonProperty("vPrivateUrl")
    public String getVPrivateUrl() {
        return vPrivateUrl;
    }

    public void setVPrivateUrl(String vPrivateUrl) {
        this.vPrivateUrl = vPrivateUrl;
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

}