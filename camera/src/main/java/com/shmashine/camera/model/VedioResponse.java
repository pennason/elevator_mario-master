package com.shmashine.camera.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/3/30.
 */
public class VedioResponse implements Serializable {

    private static final long serialVersionUID = 3458546808420298284L;

    // 视频流url
    private String url;

    private Integer expiration;

    public VedioResponse(String url) {
        this.url = url;
        this.expiration = 12 * 60 * 60;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getExpiration() {
        return expiration;
    }

    public void setExpiration(Integer expiration) {
        this.expiration = expiration;
    }
}
