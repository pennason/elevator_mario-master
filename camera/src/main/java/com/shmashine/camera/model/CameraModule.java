package com.shmashine.camera.model;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 摄像头相关数据
 */
public class CameraModule {

    private static final long serialVersionUID = 1L;

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
    /**
     * hls流
     */
    private String vHlsUrl;
    /**
     * rtmp流
     */
    private String vRtmpUrl;
    /**
     * 私有流
     */
    private String vPrivateUrl;

    /**
     * 流访问token
     */
    private String token;


    @JsonProperty("vCameraId")
    public String getvCameraId() {
        return vCameraId;
    }

    public void setvCameraId(String vCameraId) {
        this.vCameraId = vCameraId;
    }

    @JsonProperty("vElevatorId")
    public String getvElevatorId() {
        return vElevatorId;
    }

    public void setvElevatorId(String vElevatorId) {
        this.vElevatorId = vElevatorId;
    }

    @JsonProperty("vElevatorCode")
    public String getvElevatorCode() {
        return vElevatorCode;
    }

    public void setvElevatorCode(String vElevatorCode) {
        this.vElevatorCode = vElevatorCode;
    }

    @JsonProperty("iCameraType")
    public Integer getiCameraType() {
        return iCameraType;
    }

    public void setiCameraType(Integer iCameraType) {
        this.iCameraType = iCameraType;
    }

    @JsonProperty("vSerialNumber")
    public String getvSerialNumber() {
        return vSerialNumber;
    }

    public void setvSerialNumber(String vSerialNumber) {
        this.vSerialNumber = vSerialNumber;
    }

    @JsonProperty("vUsername")
    public String getvUsername() {
        return vUsername;
    }

    public void setvUsername(String vUsername) {
        this.vUsername = vUsername;
    }

    @JsonProperty("vPassword")
    public String getvPassword() {
        return vPassword;
    }

    public void setvPassword(String vPassword) {
        this.vPassword = vPassword;
    }


    @JsonProperty("vCloudNumber")
    public String getvCloudNumber() {
        return vCloudNumber;
    }

    public void setvCloudNumber(String vCloudNumber) {
        this.vCloudNumber = vCloudNumber;
    }

    @JsonProperty("vHlsUrl")
    public String getvHlsUrl() {
        return vHlsUrl;
    }

    public void setvHlsUrl(String vHlsUrl) {
        this.vHlsUrl = vHlsUrl;
    }

    @JsonProperty("vRtmpUrl")
    public String getvRtmpUrl() {
        return vRtmpUrl;
    }

    public void setvRtmpUrl(String vRtmpUrl) {
        this.vRtmpUrl = vRtmpUrl;
    }

    @JsonProperty("vPrivateUrl")
    public String getvPrivateUrl() {
        return vPrivateUrl;
    }

    public void setvPrivateUrl(String vPrivateUrl) {
        this.vPrivateUrl = vPrivateUrl;
    }

    @JsonProperty("token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
