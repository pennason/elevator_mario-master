package com.shmashine.satoken.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;

import com.shmashine.satoken.service.UserService;

/**
 * 登录
 *
 * @author click33
 */
@RestController
@RequestMapping("/user/")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 登录
     *
     * @param name 用户名
     * @param pwd  密码
     * @return 登录结果
     */
    @SaIgnore
    @RequestMapping("doLogin")
    public SaResult doLogin(String name, String pwd) {

        // 使用账号密码，进行登录
        return SaResult.data(userService.login(name, pwd));

    }

    /**
     * 重新加载登录权限
     *
     * @param userId 用户id
     */
    @SaIgnore
    @RequestMapping("reloadPermission")
    public SaResult reloadPermission(String userId) {
        userService.reloadPermission(userId);
        return SaResult.ok();
    }

    /**
     * 查询登录状态
     */
    @RequestMapping("isLogin")
    public SaResult isLogin() {
        return SaResult.ok("是否登录：" + StpUtil.isLogin());
    }

    /**
     * 查询登录 Token 信息
     */
    @RequestMapping("tokenInfo")
    public SaResult tokenInfo() {
        return SaResult.data(StpUtil.getTokenInfo());
    }

    /**
     * 注销
     */
    @RequestMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }

}
