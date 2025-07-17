package com.shmashine.api.module.login.output;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/12/13 14:49
 * <p>
 * 用户历史登录信息
 */
@Data
public class LoginInfo implements Serializable {

    private static final long serialVersionUID = -246548678452357L;

    private String id;
    /**
     * 登录用户
     */
    private String username;
    /**
     * 登录ip
     */
    private String ip;
    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String region;
    /**
     * 城市
     */
    private String city;
    /**
     * 区/县
     */
    private String county;
    /**
     * 互联网服务提供商
     */
    private String isp;
    /**
     * ipv6
     */
    private String ipv6;
    /**
     * 登录时间
     */
    private Date loginTime;


}
