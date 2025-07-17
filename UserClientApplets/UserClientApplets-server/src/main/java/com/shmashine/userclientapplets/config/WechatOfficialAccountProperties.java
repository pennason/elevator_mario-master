package com.shmashine.userclientapplets.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 微信公众号配置
 *
 * @author jiangheng
 * @date: 2023/9/13 17:11
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wx.official")
public class WechatOfficialAccountProperties {

    //唯一凭证
    private String appId;

    //唯一凭证密钥
    private String secret;

}
