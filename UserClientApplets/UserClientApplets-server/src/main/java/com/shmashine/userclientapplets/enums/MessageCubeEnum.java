// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.userclientapplets.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 服务器与设备通信消息类型枚举
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/1/9 14:28
 * @since v1.0
 */

@AllArgsConstructor
public enum MessageCubeEnum {
    // 服务器与设备通信消息类型枚举
    // 开启Dlog
    DLOG_START("Dlog", "start", "SINGLEBOX"),
    // 关闭Dlog
    DLOG_STOP("Dlog", "stop", "SINGLEBOX"),

    // 开启自检
    SELF_CHECK_START("SelfCheck", "start", "SINGLEBOX"),
    // 关闭自检
    SELF_CHECK_STOP("SelfCheck", "stop", "SINGLEBOX");

    /**
     * 消息类型 TY
     */
    @Getter
    private final String type;

    /**
     * 消息子类型 ST
     */
    @Getter
    private final String subType;

    @Getter
    private final String sensorType;
}
