package com.shmashine.commonbigscreen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shmashine.commonbigscreen.entity.User;

/**
 * 用户服务
 *
 * @author: jiangheng
 * @version: 1.0
 * @date: 2022/2/8 18:07
 */
public interface UserService extends IService<User> {

    /**
     * 用户是否为管理员
     *
     * @param userId 用户id
     */
    boolean isAdmin(String userId);
}
