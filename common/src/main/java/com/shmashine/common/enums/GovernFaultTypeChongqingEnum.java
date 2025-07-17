// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 重庆 故障类型枚举
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/24 15:20
 * @since v1.0
 */

@Getter
@AllArgsConstructor
public enum GovernFaultTypeChongqingEnum {
    // 平台定义的故障类型
    SAFETY("1", "安全回路断路", "safety"),
    NOT_CLOSE("2", "关门故障", "notClose"),
    NOT_OPEN("3", "开门故障", "notOpen"),
    LEVEL_STOP("4", "门区外停梯", "levelStop"),
    UCMP("5", "开门走车", "ucmp"),
    TIME_OVER("6", "电动机运转时间限制器动作", "timeOver"),
    SELECTOR("7", "楼层信息丢失", "selector"),
    PREVENT_RE_RUN("8", "防止电梯再运行故障", "preventReRun"),

    SWITCH_POWER_OFF("41", "主电源断开", "switchPowerOff"),

    SERVICE_MODE_AUTO("40", "恢复自动运行模式", "modeAuto"),
    SERVICE_MODE_STOP("42", "进入停止服务", "modeStop"),
    SERVICE_MODE_MAINTENANCE("43", "进入检修运行模式", "maintenanceMode"),
    SERVICE_MODE_FIRE_RETURNS("44", "进入消防返回模式", "modeFireReturns"),
    SERVICE_MODE_FIREFIGHTERS_RUN("45", "进入消防员运行模式", "modeFirefightersRun"),
    SERVICE_MODE_EMERGENCY_POWER("46", "进入应急电源运行", "modeEmergencyPower"),
    SERVICE_MODE_SEISMIC_PATTERNS("47", "地震模式", "modeSeismicPatterns"),

    INTER_PHONE("90", "用户报警", "interPhone"),
    DOWN_LIMIT("91", "蹲底", "downLimit"),
    UP_LIMIT("92", "冲顶", "upLimit"),
    ENTRAP("93", "平层关人", "entrap");

    private final String faultType;

    private final String faultName;

    private final String faultCode;

    public static GovernFaultTypeChongqingEnum getEnumByFaultType(String faultType) {
        for (var e : values()) {
            if (e.getFaultType().equals(faultType)) {
                return e;
            }
        }
        return null;
    }
}
