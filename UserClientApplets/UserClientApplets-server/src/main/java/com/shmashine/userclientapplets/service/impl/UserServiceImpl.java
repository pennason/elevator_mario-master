package com.shmashine.userclientapplets.service.impl;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shmashine.userclientapplets.dao.UserDao;
import com.shmashine.userclientapplets.entity.User;
import com.shmashine.userclientapplets.service.UserService;


/**
 * 用户服务实现
 *
 * @author jiangheng
 * @version V1.0.0 -2022/2/8 18:09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Override
    public boolean isAdmin(String userId) {

        String admin = baseMapper.isAdmin(userId);

        return admin != null;
    }

    @Override
    public User findByMobile(String number) {
        User user = getOne(new QueryWrapper<User>()
                .select("v_user_id", "v_username", "open_id", "v_mobile", "v_name", "v_identity")
                .eq("v_mobile", number).eq("i_status", 0)
                .eq("is_deleted", 0));
        return user;
    }

    @Override
    public boolean updateUser(String openid, String number, String userId) {

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();

        updateWrapper.eq("v_mobile", number).set("open_id", openid).set("v_user_id", userId);

        int updateRes = baseMapper.update(null, updateWrapper);

        return updateRes == 1;
    }


    @Override
    public HashMap getUser(String userId) {
        return baseMapper.getUser(userId);
    }
}
