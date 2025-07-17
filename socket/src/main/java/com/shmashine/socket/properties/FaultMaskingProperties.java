// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.socket.properties;

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
@ConfigurationProperties(prefix = "fault-masking")
public class FaultMaskingProperties {

    /**
     * 北向推送, 每日故障上报次数大于x次-屏蔽
     */
    private Integer northFaultCountDay;
    /**
     * 平台推送, 每小时故障上报次数大于x次-屏蔽
     */
    private Integer platformFaultCountHour;

}
