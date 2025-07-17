package com.shmashine.camera.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 下载故障历史视屏封装类
 *
 * @author Dean Winchester
 */
public class FaultCameraModule {

    /**
     * 故障id
     */
    private String id;

    /**
     * 不文明行为标识，0：故障，1：不文明行为
     */
    private Integer uncivilizedBehaviorFlag;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUncivilizedBehaviorFlag() {
        return uncivilizedBehaviorFlag;
    }

    public void setUncivilizedBehaviorFlag(Integer uncivilizedBehaviorFlag) {
        this.uncivilizedBehaviorFlag = uncivilizedBehaviorFlag;
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
}
