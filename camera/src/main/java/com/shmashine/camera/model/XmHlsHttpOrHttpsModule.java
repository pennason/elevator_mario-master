package com.shmashine.camera.model;

/**
 * XmHlsHttpOrHttpsModule
 *
 * @author Dean Winchester
 */
public class XmHlsHttpOrHttpsModule {
    /**
     * 云平台序列号
     */
    private String vCloudNumber;
    /**
     * 电梯码
     */
    private String elevatorCode;
    /**
     * 摄像头类型
     */
    private String type;

    public String getvCloudNumber() {
        return vCloudNumber;
    }

    public void setvCloudNumber(String vCloudNumber) {
        this.vCloudNumber = vCloudNumber;
    }

    public String getElevatorCode() {
        return elevatorCode;
    }

    public void setElevatorCode(String elevatorCode) {
        this.elevatorCode = elevatorCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
