package com.shmashine.camera.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.camera.dao.BizUserDao;
import com.shmashine.camera.service.BizUserService;

@Service
public class BizUserServiceImpl implements BizUserService {

    @Autowired
    private BizUserDao bizUserDao;

    @Override
    public boolean isAdmin(String userId) {
        String admin = bizUserDao.isAdmin(userId);
        return admin != null;
    }
}
