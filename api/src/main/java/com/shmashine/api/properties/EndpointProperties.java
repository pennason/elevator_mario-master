// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/6/25 14:32
 * @since v1.0
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "endpoint")
public class EndpointProperties {

    private String shmashineApplets;
    private String shmashineSatoken;
    private String shmashineCamera;
    private String shmashineHkCameraYs;
    private String shmashineHkCameraYm;
    private String aiImageLabel;
    private String senderServer;
    /**
     * 升级文件使用
     */
    private String upgradeFileServer;
}
