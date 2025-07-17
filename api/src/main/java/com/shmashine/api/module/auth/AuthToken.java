package com.shmashine.api.module.auth;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/3/29.
 */
public class AuthToken implements Serializable {

    private static final long serialVersionUID = -1805134988246951634L;

    private String accessToken;

    /**
     * 过期时间 epoch时间，以秒计
     */
    private Integer expiresTime;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(Integer expiresTime) {
        this.expiresTime = expiresTime;
    }

}
