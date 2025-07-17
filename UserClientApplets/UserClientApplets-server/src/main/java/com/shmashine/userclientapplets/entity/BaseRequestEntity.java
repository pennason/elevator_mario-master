package com.shmashine.userclientapplets.entity;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;

import cn.dev33.satoken.stp.StpUtil;


/**
 * 请求基类 如需要header中的参数，则可以继承该类使用
 *
 * @author Liulifu
 * @version Version v1.0 - 2020/3/12 1:39
 */
@ControllerAdvice
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
