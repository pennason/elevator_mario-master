package com.shmashine.hkCameraForTY.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * TblResponseHkReport
 * @author: jiangheng
 * @version: 1.0
 * @date: 2021/6/15 15:07
 */
public class TblResponseHkReport implements Serializable {

    private static final long serialVersionUID = 236442493281585525L;

    /**
     * id
     */
    private Integer id;

    /**
     * 文件状态：0：待下载 1：下载成功 2：下载中（请求成功）
     * 3：请求失败（等待重试） 4：文件上传阿里解析失败
     */
    private Integer fileStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date modifyTime;

    /**
     * 返回code
     */
    private Integer returnCode;

    /**
     * 电梯code
     */
    private String elevatorCode;

    /**
     * 故障id
     */
    private String faultId;

    /**
     * 视频下载开始时间
     */
    private Date startTime;

    /**
     * 视频下载结束时间
     */
    private Date endTime;

    /**
     * 删除标识
     */
    private Integer delFlag;

    /**
     * 请求失败次数
     */
    private Integer requestFailedNum;

    /**
     * 故障类型
     */
    private String faultType;

    /**
     * 备注
     */
    private String comment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(Integer fileStatus) {
        this.fileStatus = fileStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
    }

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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getRequestFailedNum() {
        return requestFailedNum;
    }

    public void setRequestFailedNum(Integer requestFailedNum) {
        this.requestFailedNum = requestFailedNum;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFaultType() {
        return faultType;
    }

    public void setFaultType(String faultType) {
        this.faultType = faultType;
    }
}
