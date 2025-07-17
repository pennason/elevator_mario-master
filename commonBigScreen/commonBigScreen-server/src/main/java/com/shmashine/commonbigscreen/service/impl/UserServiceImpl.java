package com.shmashine.commonbigscreen.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shmashine.commonbigscreen.dao.UserDao;
import com.shmashine.commonbigscreen.entity.User;
import com.shmashine.commonbigscreen.service.UserService;

/**
 * 用户服务实现
 *
 * @author: jiangheng
 * @version: 1.0
 * @date: 2022/2/8 18:09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Override
    public boolean isAdmin(String userId) {

        String admin = baseMapper.isAdmin(userId);

        return admin != null;
    }
}
