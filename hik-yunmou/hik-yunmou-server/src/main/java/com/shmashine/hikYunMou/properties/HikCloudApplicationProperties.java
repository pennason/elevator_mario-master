// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hikYunMou.properties;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 海康云眸配置
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/16 16:42
 * @since v1.0
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "hik-cloud.application")
public class HikCloudApplicationProperties implements Serializable {
    private String clientId;
    private String clientSecret;
    private String grantType;
}

