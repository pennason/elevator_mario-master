package com.shmashine.hkcamerabyys.client.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 海康摄像头萤石平台下载文件表
 *
 * @author jiangheng
 * @version v1.0.0 - 2021/11/8 10:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TblHKCameraDownload implements Serializable {

    private static final Long serialVersionUID = 679874653184364L;

    private String id;

    /**
     * 文件类型（0：图片，1：视频）
     */
    private Integer fileType;

    /**
     * 0：待下载 1：下载成功 2：下载中（请求成功）
     * 3：请求失败（等待重试） 4：文件上传阿里解析失败
     */
    private Integer fileStatus;

    /**
     * 电梯码
     */
    private String elevatorCode;

    /**
     * 故障id
     */
    private String faultId;

    /**
     * 摄像头序列号
     */
    private String serialNumber;

    /**
     * 历史视频下载开始时间
     */
    private String startTime;

    /**
     * 历史视频下载结束时间
     */
    private String endTime;

    /**
     * 上传失败次数标记
     */
    private Integer uploadFailedNum;

    /**
     * 请求失败次数标记
     */
    private Integer requestFailedNum;

    /**
     * 返回码
     */
    private Integer returnCode;

    /**
     * 错误原因
     */
    private String errMessage;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date modifyTime;

    /**
     * 故障类型
     */
    private String faultType;

}
