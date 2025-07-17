package com.shmashine.userclientapplets.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * SmsProperties
 *
 * @author jiangheng
 */
@Data
@ConfigurationProperties(prefix = "shmashine.sms")
public class SmsProperties {
    /**
     * 账号
     */
    String accessKeyID;
    /**
     * 密钥
     */
    String accessKeySecret;
    /**
     * 短信签名
     */
    String signName;
    /**
     * 短信模板
     */
    String verifyCodeTemplate;
    /**
     * 发送短信请求的域名
     */
    String domain;
    /**
     * API版本
     */
    String version;
    /**
     * API类型
     */
    String action;
    /**
     * 区域
     */
    String regionID;
}
