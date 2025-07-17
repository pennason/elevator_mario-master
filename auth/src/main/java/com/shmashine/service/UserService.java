package com.shmashine.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.UserDetailsService;

//后期在此新增UserService的业务接口
public interface UserService extends UserDetailsService {
    void getAddr(HttpServletRequest request);
}
