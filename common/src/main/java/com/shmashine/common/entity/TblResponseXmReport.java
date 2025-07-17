package com.shmashine.common.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 文件记录表
 *
 * @author Dean Winchester
 */

@Data
public class TblResponseXmReport implements Serializable {

    private static final long serialVersionUID = -85672360711605234L;

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
     * 取证开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date startTime;
    /**
     * 取证结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date endTime;


    /**
     * 逻辑删除标记
     */
    private Integer delFlag;

    /**
     * 请求文件失败次数
     */
    private Integer requestFailedNum;

    /**
     * 文件解析失败次数
     */
    private Integer uploadFailedNum;

    /**
     * 备注
     */
    private String comment;

}

