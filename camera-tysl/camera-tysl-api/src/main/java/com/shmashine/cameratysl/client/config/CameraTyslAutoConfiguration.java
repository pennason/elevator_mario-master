// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.client.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 10:36
 * @since v1.0
 */

@Configuration
@ComponentScan(basePackages = {"com.shmashine.cameratysl.client"})
@EnableFeignClients(basePackages = {"com.shmashine.cameratysl.client"})
public class CameraTyslAutoConfiguration {
}
