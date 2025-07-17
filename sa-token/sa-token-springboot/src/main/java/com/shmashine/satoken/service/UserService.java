package com.shmashine.satoken.service;

/**
 * 用户服务
 *
 * @author jiangheng
 * @version v1.0.0 - 2024/3/18 16:27
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param name 用户名
     * @param pwd  密码
     * @return token值
     */
    String login(String name, String pwd);

    /**
     * 重新加载登录权限
     *
     * @param userId 用户id
     * @return Str
     */
    void reloadPermission(String userId);
}
