// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/9 11:32
 * @since v1.0
 */

@AllArgsConstructor
public enum CameraMediaTypeEnum {
    // 文件类型
    JPG("jpg", "image", "图像"),
    JPEG("jpeg", "image", "图像"),
    PNG("png", "image", "图像"),
    MP4("mp4", "video", "视频"),
    M3U8("m3u8", "video", "视频"),
    RTMP("rtmp", "video", "视频"),
    FLV("flv", "video", "视频");

    @Getter
    private final String mediaType;

    @Getter
    private final String fileType;

    @Getter
    private final String description;

    public static String getFileTypeByMediaType(String mediaType) {
        for (var item : values()) {
            if (Objects.equals(item.getMediaType(), mediaType)) {
                return item.getFileType();
            }
        }
        throw new RuntimeException("请在com.shmashine.common.enums.CameraMediaType 中定义相关任务类型");
    }

    public static String getDescriptionMediaType(String mediaType) {
        for (var item : values()) {
            if (Objects.equals(item.getMediaType(), mediaType)) {
                return item.getDescription();
            }
        }
        throw new RuntimeException("请在com.shmashine.common.enums.CameraMediaType 中定义相关任务类型");
    }

    public static List<String> getMediaTypeListByFileType(String fileType) {
        var res = new ArrayList<String>();
        for (var item : values()) {
            if (Objects.equals(item.getFileType(), fileType)) {
                res.add(item.getMediaType());
            }
        }
        return res;
    }
}
