package com.shmashine.api.module.device;


/**
 * 上传升级文件实体类
 *
 * @author little.li
 */
public class UploadFileModule {

    /**
     * 文件id
     */
    private String deviceFileId;

    /**
     * 固件版本
     */
    private String version;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 标签
     */
    private String label;

    /**
     * 签名
     */
    private String signature;

    /**
     * 硬件版本号
     */
    private String hWVersion;

    /**
     * 删除标识 0-未删除，1-已删除
     */
    private Integer delFlag;

    private String eType;

    public String getDeviceFileId() {
        return deviceFileId;
    }

    public void setDeviceFileId(String deviceFileId) {
        this.deviceFileId = deviceFileId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String gethWVersion() {
        return hWVersion;
    }

    public void sethWVersion(String hWVersion) {
        this.hWVersion = hWVersion;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String geteType() {
        return eType;
    }

    public void seteType(String eType) {
        this.eType = eType;
    }
}
