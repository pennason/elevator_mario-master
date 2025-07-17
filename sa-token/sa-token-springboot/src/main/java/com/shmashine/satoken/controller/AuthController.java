package com.shmashine.satoken.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.annotation.SaIgnore;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.satoken.service.UserService;

/**
 * 前端适配接口
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/4/19 11:39
 * @Since: 1.0.0
 */
@RestController
@RequestMapping("/auth/oauth/")
public class AuthController {

    @Resource
    private UserService userService;

    /**
     * 登录-前端历史接口适配
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    @SaIgnore
    @RequestMapping("token")
    public JSONObject doLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password) {

        // 使用账号密码，进行登录
        String token = userService.login(username, password);
        JSONObject result = new JSONObject();
        result.put("access_token", token);
        result.put("expires_in", 115748);
        result.put("refresh_token", "");
        result.put("scope", "");
        result.put("token_type", "bearer");

        return result;
    }

}
