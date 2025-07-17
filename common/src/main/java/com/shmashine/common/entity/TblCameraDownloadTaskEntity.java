// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/9 13:55
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TblCameraDownloadTaskEntity implements Serializable {

    /**
     * 任务ID， 自增
     */
    private Long id;
    /**
     * 电梯编号
     */
    private String elevatorCode;
    /**
     * 任务类型 1:告警，2：夜间守护，3：群租， 4：急修急救， 5：电动车识别， 0：其他：参考com.shmashine.common.enums.CameraTaskTypeEnum
     */
    private Integer taskType;
    /**
     * 自定义唯一ID，如故障ID
     */
    private String taskCustomId;
    /**
     * 文件类型 JPG, MP4, JPEG, M3U8 参考：CameraMediaTypeEnum
     */
    private String mediaType;
    /**
     * 摄像头类型 1：海康萤石，2：雄迈， 4：海康云眸 com.shmashine.common.enums.CameraTypeEnum
     */
    private Integer cameraType;
    /**
     * 云平台序列号，通过该序号获取摄像头视频
     */
    private String cloudNumber;
    /**
     * 自定义类型，如故障类型
     */
    private Integer taskCustomType;
    /**
     * 采集时间 yyyy-MM-dd HH:mm:ss
     */
    private String collectTime;
    /**
     * 历史视频下载开始时间 yyyyMMddHHmmss
     */
    private String startTime;
    /**
     * 历史视频下载结束时间 yyyyMMddHHmmss
     */
    private String endTime;
    /**
     * 所在楼层
     */
    private String floor;
    /**
     * 下载执行状态 0：待下载 1：下载成功 2:开始处理，3：下载中（请求成功）, 4:待上传OSS  5：请求失败（等待重试） 6：文件上传阿里解析失败
     */
    private Integer fileStatus;
    /**
     * 远程执行任务ID，如萤石的下载任务ID
     */
    private String cloudTaskId;
    /**
     * 请求失败次数标记
     */
    private Integer requestFailedCount;
    /**
     * 上传失败次数标记
     */
    private Integer uploadFailedCount;
    /**
     * 返回码
     */
    private Integer returnCode;
    /**
     * 失败原因
     */
    private String errMessage;
    /**
     * 文件生成的原地址
     */
    private String sourceUrl;
    /**
     * 文件OSS地址
     */
    private String ossUrl;
    /**
     * 其他扩展信息， 可以是JSON字符串存储
     */
    private String extendInfo;
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
}
