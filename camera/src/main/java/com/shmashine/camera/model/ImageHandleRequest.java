package com.shmashine.camera.model;


/**
 * 截取图片请求封装
 *
 * @author Dean Winchester
 */

public class ImageHandleRequest {
    /**
     * 电梯编号
     */
    private String elevatorCode;

    private String stype;
    /**
     * 故障id
     */
    private String faultId;

    public String getElevatorCode() {
        return elevatorCode;
    }

    public void setElevatorCode(String elevatorCode) {
        this.elevatorCode = elevatorCode;
    }

    public String getStype() {
        return stype;
    }

    public void setStype(String stype) {
        this.stype = stype;
    }

    public String getFaultId() {
        return faultId;
    }

    public void setFaultId(String faultId) {
        this.faultId = faultId;
    }
}
