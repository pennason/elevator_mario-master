package com.shmashine.camera.dao;

public interface BizUserDao {

    /**
     * 判断是否是 超级管理员
     */
    String isAdmin(String userId);
}
