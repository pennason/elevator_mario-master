// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.dto;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shmashine.common.enums.CameraMediaTypeEnum;
import com.shmashine.common.enums.CameraTaskTypeEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/8 16:56
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(title = "CamareMediaDownloadRequestDTO", description = "下载视频请求参数")
public class CamareMediaDownloadRequestDTO implements Serializable {
    @Schema(title = "电梯编号", description = "电梯编号")
    private String elevatorCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(title = "记录采集时间 yyyy-MM-dd HH:mm:ss", description = "记录采集时间 yyyy-MM-dd HH:mm:ss, 如果不传则为 startTime")
    private String collectTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(title = "开始时间获取视频使用 yyyy-MM-dd HH:mm:ss", description = "开始时间获取视频使用 yyyy-MM-dd HH:mm:ss")
    private String startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(title = "结束时间获取视频使用 yyyy-MM-dd HH:mm:ss", description = "结束时间 yyyy-MM-dd HH:mm:ss 与 duringSeconds 二选一")
    private String endTime;

    @Schema(title = "持续时长(秒)", description = "持续时长（秒），与endTime结束时间，二选一填即可")
    private Long duringSeconds;

    @Schema(title = "所在楼层", description = "所在楼层， 拍照时可能需要提供此字段")
    private String floor;

    @Schema(title = "任务类型", description = "任务类型：参考CameraTaskTypeEnum", defaultValue = "FAULT")
    private CameraTaskTypeEnum taskType;

    @Schema(title = "自定义唯一ID，如故障ID", description = "自定义唯一ID，如故障ID, 如果是故障ID，elevatorCode可以不传")
    private String taskCustomId;

    @Schema(title = "自定义类型，如故障类型", description = "自定义类型，如故障类型", defaultValue = "0")
    private Integer taskCustomType;

    @Schema(title = "文件类型", description = "文件类型", defaultValue = "MP4")
    private CameraMediaTypeEnum mediaType;

    @Schema(title = "其他需要额外扩展的字段", description = "其他需要额外扩展的字段， 原值存储在库中")
    private Map<String, String> extendInfo;



    /*@Schema(title = "摄像头类型 HAI_KANG XIONG_MAI", description = "摄像头类型 HAI_KANG XIONG_MAI", defaultValue = "HAI_KANG")
    private CameraTypeEnum cameraType;

    @Schema(title = "云平台序列号，通过该序号获取摄像头视频", description = "云平台序列号，通过该序号获取摄像头视频")
    private String cloudNumber;*/
}
