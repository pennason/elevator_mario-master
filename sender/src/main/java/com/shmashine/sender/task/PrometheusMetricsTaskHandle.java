// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shmashine.sender.server.metrics.PrometheusMetricsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/1/3 11:32
 * @since v1.0
 */

@Slf4j
@Profile({"prod"})
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class PrometheusMetricsTaskHandle {
    private final PrometheusMetricsService prometheusMetricsService;


    @Scheduled(fixedDelay = 30000, initialDelay = 300000)
    public void statisticsOfflineElevators() {
        prometheusMetricsService.statisticsOfflineElevators();
    }

    @Scheduled(fixedDelay = 60000, initialDelay = 360000)
    public void statisticsOfflineProjectElevators() {
        prometheusMetricsService.statisticsOfflineProjectElevators();

    }
}
