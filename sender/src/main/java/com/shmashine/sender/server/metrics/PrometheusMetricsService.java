// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.server.metrics;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/7/7 16:31
 * @since v1.0
 */

public interface PrometheusMetricsService {

    /**
     * 统计离线电梯
     */
    void statisticsOfflineElevators();

    /**
     * 按项目统计离线电梯
     */
    void statisticsOfflineProjectElevators();
}
