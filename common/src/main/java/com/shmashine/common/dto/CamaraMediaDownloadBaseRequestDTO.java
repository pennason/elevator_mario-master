// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.dto;

import java.io.Serializable;

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
 * @version v1.0  -  2023/3/7 10:58
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(title = "CamaraMediaDownloadBaseRequestDTO", description = "下载视频请求基础类")
public class CamaraMediaDownloadBaseRequestDTO implements Serializable {
    @Schema(title = "电梯编号", description = "电梯编号， 如果有故障ID，此值可不传")
    private String elevatorCode;

    @Schema(title = "所在楼层", description = "所在楼层， 拍照时可能需要提供此字段")
    private String floor;

    @Schema(title = "任务类型", description = "任务类型：参考CameraTaskTypeEnum", defaultValue = "FAULT")
    private CameraTaskTypeEnum taskType;

    @Schema(title = "文件类型", description = "文件类型", defaultValue = "MP4")
    private CameraMediaTypeEnum mediaType;
}
