package com.shmashine.api.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shmashine.common.entity.base.PageListParams;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * tbl_request_xm_report
 *
 * @author jiangheng
 * @since 2020/12/28 —— 11:29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TblRequestXmReport extends PageListParams implements Serializable {

    private static final long serialVersionUID = -85672360711607894L;

    /**
     * id
     */
    private String id;


    /**
     * 服务器存放地址
     */
    private String url;

    /**
     * 1:请求雄迈 2：雄迈回调
     */
    private String actionType;

    /**
     * 0：图片，1：视频
     */
    private String fileType;
    /**
     * 0：未下载 1：下载成功 2：下载中
     */
    private String fileStatus;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date createTime;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date modifyTime;

    //针对下载历史视屏请求参数
    /**
     * 返回编码
     */
    private Integer returnCode;

    /**
     * 电梯编码
     */
    private String elevatorCode;

    /**
     * 故障id
     */
    private String faultId;

    /**
     * 不文明行为标记
     */
    private Integer uncivilizedBehaviorFlag;

    /**
     * 序列号（对应摄像头表中的电梯编码）
     */
    private String serialNumber;

    /**
     * 故障类型
     */
    private String iFaultType;

    /**
     * 取证开始时间
     */
    private String startTime;

    /**
     * 取证结束时间
     */
    private String endTime;


    /**
     * 逻辑删除标记
     */
    private Integer delFlag;

    /**
     * 文件解析失败次数
     */
    private Integer uploadFailedNum;

    /**
     * 文件下载失败次数
     */
    private Integer requestFailedNum;

    public Integer getRequestFailedNum() {
        return requestFailedNum;
    }

    public void setRequestFailedNum(Integer requestFailedNum) {
        this.requestFailedNum = requestFailedNum;
    }

    @JsonProperty("uploadFailedNum")
    public Integer getUploadFailedNum() {
        return uploadFailedNum;
    }

    public void setUploadFailedNum(Integer uploadFailedNum) {
        this.uploadFailedNum = uploadFailedNum;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("actionType")
    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    @JsonProperty("fileType")
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @JsonProperty("fileStatus")
    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    public String getiFaultType() {
        return iFaultType;
    }

    public void setiFaultType(String iFaultType) {
        this.iFaultType = iFaultType;
    }

    @JsonProperty("createTime")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JsonProperty("modifyTime")
    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @JsonProperty("returnCode")
    public Integer getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
    }

    @JsonProperty("elevatorCode")
    public String getElevatorCode() {
        return elevatorCode;
    }

    public void setElevatorCode(String elevatorCode) {
        this.elevatorCode = elevatorCode;
    }

    @JsonProperty("faultId")
    public String getFaultId() {
        return faultId;
    }

    public void setFaultId(String faultId) {
        this.faultId = faultId;
    }

    @JsonProperty("uncivilizedBehaviorFlag")
    public Integer getUncivilizedBehaviorFlag() {
        return uncivilizedBehaviorFlag;
    }

    public void setUncivilizedBehaviorFlag(Integer uncivilizedBehaviorFlag) {
        this.uncivilizedBehaviorFlag = uncivilizedBehaviorFlag;
    }

    @JsonProperty("serialNumber")
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @JsonProperty("delFlag")
    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
