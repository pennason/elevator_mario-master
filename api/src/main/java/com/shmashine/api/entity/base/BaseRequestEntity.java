package com.shmashine.api.entity.base;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import cn.dev33.satoken.stp.StpUtil;

import com.alibaba.fastjson2.JSON;
import com.shmashine.common.dto.ResponseResultDTO;

/**
 * 请求基类 如需要header中的参数，则可以继承该类使用
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

    public <T> void doWriteResponseResult(HttpServletResponse response, ResponseResultDTO<T> data) {
        response.reset();
        response.setContentType(MediaType.APPLICATION_JSON);
        response.setCharacterEncoding("utf-8");
        try {
            response.getWriter().println(JSON.toJSONString(data));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
