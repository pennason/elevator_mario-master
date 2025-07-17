package com.shmashine.commonbigscreen.entity;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import cn.dev33.satoken.stp.StpUtil;

/**
 * 请求基类 如需要header中的参数，则可以继承该类使用
 *
 * @author Liulifu
 * @version v1.0 - 2020/3/12 1:39
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
