// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/6/21 17:40
 * @since v1.0
 */

@Configuration
@ComponentScan(basePackages = {"com.shmashine.common"})
public class CommonAutoConfiguration {
}
