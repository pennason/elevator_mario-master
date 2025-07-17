// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 11:16
 * @since v1.0
 */

@Getter
@AllArgsConstructor
public enum TyslResponseStatusEnum {
    // 天翼视联统一返回状态枚举
    OK(200, "成功"),
    HTTP_ERROR(300, "http响应异常"),
    PARAM_ERROR(400, "请求参数错误"),
    AUTH_ERROR(401, "鉴权信息有误/⽤户登录错误"),
    NO_TOKEN(4010, "⽆token"),
    TOKEN_INVALID(4011, "token⽆效"),
    TOKEN_EXPIRED(4012, "token过期"),
    PARAM_MISSING(404, "参数缺失"),
    FACTORY_NOT_EXIST(405, "⼚商不存在"),
    DATA_NOT_EXIST(406, "数据不存在"),
    RSA_DECODE_ERROR(407, "RSA解码错误"),
    INTERNAL_ERROR(500, "内部异常，需联系管理员/未知错误"),
    FACTORY_SERVICE_ERROR(502, "⼚商平台服务异常"),
    FACTORY_RESPONSE_ERROR(567, "⼚商返回失败");

    private final Integer code;
    private final String msg;

    public static TyslResponseStatusEnum getByCode(Integer code) {
        for (var value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}