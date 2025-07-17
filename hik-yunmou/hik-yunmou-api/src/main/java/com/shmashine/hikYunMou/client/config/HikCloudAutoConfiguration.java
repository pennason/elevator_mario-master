// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hikYunMou.client.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/6/12 11:02
 * @since v1.0
 */

@Configuration
@ComponentScan(basePackages = {"com.shmashine.hikYunMou.client"})
@EnableFeignClients(basePackages = {"com.shmashine.hikYunMou.client"})
//@ConfigurationPropertiesScan(basePackages = {"com.shmashine.hikYunMou.client"})
public class HikCloudAutoConfiguration {
}
