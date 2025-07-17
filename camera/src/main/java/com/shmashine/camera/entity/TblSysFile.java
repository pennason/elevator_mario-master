package com.shmashine.camera.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 系统文件表(TblSysFile)实体类
 *
 * @author little.li
 * @since 2020-06-21 04:40:58
 */
public class TblSysFile implements Serializable {

    private static final long serialVersionUID = -85672260726605364L;
    /**
     * 文件唯一标识
     */
    private String vFileId;
    /**
     * 文件类型,0:图片，1:视频，2:bin(设备相关文件)，3:excel
     */
    private String vFileType;
    /**
     * 文件名
     */
    private String vFileName;
    /**
     * 业务id
     */
    private String vBusinessId;
    /**
     * 业务类型 1：工单 2：故障
     */
    private Integer iBusinessType;
    /**
     * 文件路径（全路径）
     */
    private String vUrl;
    /**
     * 备注
     */
    private String vRemark;
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

    @JsonProperty("vFileId")
    public String getVFileId() {
        return vFileId;
    }

    public void setVFileId(String vFileId) {
        this.vFileId = vFileId;
    }

    @JsonProperty("vFileType")
    public String getVFileType() {
        return vFileType;
    }

    public void setVFileType(String vFileType) {
        this.vFileType = vFileType;
    }

    @JsonProperty("vFileName")
    public String getVFileName() {
        return vFileName;
    }

    public void setVFileName(String vFileName) {
        this.vFileName = vFileName;
    }

    @JsonProperty("vBusinessId")
    public String getVBusinessId() {
        return vBusinessId;
    }

    public void setVBusinessId(String vBusinessId) {
        this.vBusinessId = vBusinessId;
    }

    @JsonProperty("iBusinessType")
    public Integer getIBusinessType() {
        return iBusinessType;
    }

    public void setIBusinessType(Integer iBusinessType) {
        this.iBusinessType = iBusinessType;
    }

    @JsonProperty("vUrl")
    public String getVUrl() {
        return vUrl;
    }

    public void setVUrl(String vUrl) {
        this.vUrl = vUrl;
    }

    @JsonProperty("vRemark")
    public String getVRemark() {
        return vRemark;
    }

    public void setVRemark(String vRemark) {
        this.vRemark = vRemark;
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

}