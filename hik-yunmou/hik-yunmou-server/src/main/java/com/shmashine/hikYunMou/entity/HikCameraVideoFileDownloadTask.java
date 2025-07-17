package com.shmashine.hikYunMou.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Builder;
import lombok.Data;

/**
 * @author  jiangheng
 * @version 2022/11/10 16:34
 * @description: 海康开放平台视频取证任务
 */
@Builder
@Data
@TableName("tbl_hikCamera_fileDownload_task")
public class HikCameraVideoFileDownloadTask implements Serializable {

    /**
     * 主键.唯一标识
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 0：待下载 1：下载成功 2：下载中（请求成功） 3：请求失败（等待重试） 4：文件上传阿里解析失败
     */
    private Integer fileStatus;

    /**
     * 文件类型 1：视频 0：图片
     */
    private Integer fileType;

    /**
     * 电梯code
     */
    private String elevatorCode;

    /**
     * 摄像头序列号
     */
    private String deviceSerial;

    /**
     * 故障类型
     */
    private String faultType;

    /**
     * 故障id
     */
    private String faultId;

    /**
     * 视频下载开始时间
     */
    private String startTime;

    /**
     * 视频下载结束时间
     */
    private String endTime;

    /**
     * 下载失败次数
     */
    private Integer downloadFailedNum;

    /**
     * 上传文件失败次数
     */
    private Integer uploadFailedNum;

    /**
     * 失败状态码
     */
    private Integer errorCode;

    /**
     * 失败原因
     */
    private String errorMsg;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

}
