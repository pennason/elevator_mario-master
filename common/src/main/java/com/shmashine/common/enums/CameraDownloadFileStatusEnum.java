// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/9 17:51
 * @since v1.0
 */

@AllArgsConstructor
public enum CameraDownloadFileStatusEnum {
    // 下载状态枚举
    WAITING(0, "待下载"),
    SUCCESS(1, "下载成功"),
    START_DEAL(2, "开始任务"),
    DOWNLOADING(3, "下载中（请求成功）"),
    WAIT_UPLOAD_OSS(4, "待上传OSS"),
    REQUEST_FAILED(5, "请求失败（等待重试）"),
    UPLOAD_OSS_FAILED(6, "文件上传阿里解析失败");


    @Getter
    private Integer status;

    @Getter
    private String description;
}
