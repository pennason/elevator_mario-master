package com.shmashine.api.module.camera;


import java.util.List;

import com.shmashine.common.entity.base.PageListParams;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


/**
 * 查询摄像头信息 请求对象
 *
 * @author Dean Winchester
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SearchCamerasModule extends PageListParams {

    private String elevatorId;

    private String elevatorCode;

    private Integer cameraType;

    private String serialNumber;

    private String cloudNumber;

    private String cameraId;

    /**
     * 是否绑定查询字段
     */
    private String isBound;

    /**
     * 摄像头是否激活查询字段
     */
    private Integer isActivate;

    //追加摄像头关联视频或图片的请求参数
    // 0: 图片，1: 视频
    private String fileType;
    // 故障id
    private String faultId;


    //摄像头是否开启
    private String isOnline;

    //授权的电梯列表
    private List<String> eleCodes;

    public String getIsBound() {
        return isBound;
    }

    public void setIsBound(String isBound) {
        this.isBound = isBound;
    }

    public Integer getIsActivate() {
        return isActivate;
    }

    public void setIsActivate(Integer isActivate) {
        this.isActivate = isActivate;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFaultId() {
        return faultId;
    }

    public void setFaultId(String faultId) {
        this.faultId = faultId;
    }

    public String getCloudNumber() {
        return cloudNumber;
    }

    public void setCloudNumber(String cloudNumber) {
        this.cloudNumber = cloudNumber;
    }

    public String getElevatorId() {
        return elevatorId;
    }

    public void setElevatorId(String elevatorId) {
        this.elevatorId = elevatorId;
    }

    public String getElevatorCode() {
        return elevatorCode;
    }

    public void setElevatorCode(String elevatorCode) {
        this.elevatorCode = elevatorCode;
    }

    public Integer getCameraType() {
        return cameraType;
    }

    public void setCameraType(Integer cameraType) {
        this.cameraType = cameraType;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public List<String> getEleCodes() {
        return eleCodes;
    }

    public void setEleCodes(List<String> eleCodes) {
        this.eleCodes = eleCodes;
    }
}
