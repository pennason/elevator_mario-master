// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 17:32
 * @since v1.0
 */

@Getter
@AllArgsConstructor
public enum TyslVideoProtoEnum {
    // 视频协议
    RTSP(1, "RTSP"),
    HLS(2, "HLS"),
    FLV(3, "FLV"),
    RTMP(4, "RTMP"),
    MP4(5, "MP4"),
    HLS_HTTPS(6, "HLS_HTTPS");

    private final Integer code;
    private final String name;

    public static TyslVideoProtoEnum getByCode(Integer code) {
        for (var value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}