package com.shmashine.camera.model;

/**
 * 封装响应参数
 * 获取摄像头相关的视频或图片实体
 *
 * @author Dean Winchester
 */
public class CamerasResponseModule {
    private String vCameraId;
    private Integer iCameraType;
    private String vCloudNumber;
    private String vSerialNumber;
    private String vElevatorCode;
    private String vFaultName;
    private String vFileId;
    private String vFileName;
    private String vFileType;
    private String vUrl;
    private String dtCreateTime;
    private String dtModifyTime;

    public String getvCameraId() {
        return vCameraId;
    }

    public void setvCameraId(String vCameraId) {
        this.vCameraId = vCameraId;
    }

    public Integer getiCameraType() {
        return iCameraType;
    }

    public void setiCameraType(Integer iCameraType) {
        this.iCameraType = iCameraType;
    }

    public String getvCloudNumber() {
        return vCloudNumber;
    }

    public void setvCloudNumber(String vCloudNumber) {
        this.vCloudNumber = vCloudNumber;
    }

    public String getvSerialNumber() {
        return vSerialNumber;
    }

    public void setvSerialNumber(String vSerialNumber) {
        this.vSerialNumber = vSerialNumber;
    }

    public String getvElevatorCode() {
        return vElevatorCode;
    }

    public void setvElevatorCode(String vElevatorCode) {
        this.vElevatorCode = vElevatorCode;
    }

    public String getvFaultName() {
        return vFaultName;
    }

    public void setvFaultName(String vFaultName) {
        this.vFaultName = vFaultName;
    }

    public String getvFileId() {
        return vFileId;
    }

    public void setvFileId(String vFileId) {
        this.vFileId = vFileId;
    }

    public String getvFileName() {
        return vFileName;
    }

    public void setvFileName(String vFileName) {
        this.vFileName = vFileName;
    }

    public String getvFileType() {
        return vFileType;
    }

    public void setvFileType(String vFileType) {
        this.vFileType = vFileType;
    }

    public String getvUrl() {
        return vUrl;
    }

    public void setvUrl(String vUrl) {
        this.vUrl = vUrl;
    }

    public String getDtCreateTime() {
        return dtCreateTime;
    }

    public void setDtCreateTime(String dtCreateTime) {
        this.dtCreateTime = dtCreateTime;
    }

    public String getDtModifyTime() {
        return dtModifyTime;
    }

    public void setDtModifyTime(String dtModifyTime) {
        this.dtModifyTime = dtModifyTime;
    }
}
