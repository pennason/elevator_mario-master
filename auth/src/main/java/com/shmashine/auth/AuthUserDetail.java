package com.shmashine.auth;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;

import com.shmashine.model.TblSysUser;

import lombok.Data;

@Data
public class AuthUserDetail extends User {
    private TblSysUser tUser;

    public AuthUserDetail(TblSysUser user) {
        super(user.getV_user_id(), user.getV_password(), true, true, true, true, Collections.EMPTY_SET);
        this.tUser = user;
    }
}
