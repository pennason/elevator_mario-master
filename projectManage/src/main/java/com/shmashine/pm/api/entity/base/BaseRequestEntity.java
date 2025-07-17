package com.shmashine.pm.api.entity.base;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import cn.dev33.satoken.stp.StpUtil;

/**
 * 基础请求类
 */

public class BaseRequestEntity {
    @Autowired
    private HttpServletRequest request;

    public String getUserId() {
        if (request != null) {
            if (StringUtils.hasText(request.getHeader("user_id"))) {
                return request.getHeader("user_id");
            }
        }
        return StpUtil.getLoginIdAsString();
    }

}
