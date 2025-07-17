package com.shmashine.common.model.request;

/**
 * 面部识别请求
 *
 * @author Dean Winchester
 */
public class FaceRecognitionRequest {

    /**
     * 电梯编号
     */
    private String elevatorCode;

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

    public String getFaultId() {
        return faultId;
    }

    public void setFaultId(String faultId) {
        this.faultId = faultId;
    }
}
