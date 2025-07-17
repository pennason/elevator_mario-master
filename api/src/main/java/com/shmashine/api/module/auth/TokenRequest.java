package com.shmashine.api.module.auth;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2019/3/29.
 */
public class TokenRequest implements Serializable {

    private static final long serialVersionUID = -952288735296566273L;

    private String userId;

    private String AppKey;

    private String AppSecret;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonProperty("AppKey")
    public String getAppKey() {
        return AppKey;
    }

    public void setAppKey(String appKey) {
        AppKey = appKey;
    }

    @JsonProperty("AppSecret")
    public String getAppSecret() {
        return AppSecret;
    }

    public void setAppSecret(String appSecret) {
        AppSecret = appSecret;
    }
}