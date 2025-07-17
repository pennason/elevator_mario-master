package com.shmashine.camera.service;

public interface BizUserService {

    /**
     * 是否是管理员
     */
    boolean isAdmin(String userId);
}
