package com.shmashine.userclientapplets.service;

import java.util.HashMap;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shmashine.userclientapplets.entity.User;

/**
 * 用户服务
 *
 * @author jiangheng
 * @version V1.0.0 -2022/2/8 18:07
 */
public interface UserService extends IService<User> {

    /**
     * 用户是否为管理员
     *
     * @param userId 用户id
     */
    boolean isAdmin(String userId);

    /**
     * 根据手机号查找
     *
     * @param number 手机号
     */
    User findByMobile(String number);

    /**
     * 根据手机号更新用户openId
     *
     * @param number 手机号
     * @param openid openid
     */
    boolean updateUser(String openid, String number, String userId);

    HashMap getUser(String userId);
}
