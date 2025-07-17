// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.server.metrics.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.shmashine.common.entity.TblProject;
import com.shmashine.sender.builder.GaugeBuilder;
import com.shmashine.sender.log.SenderCounterServer;
import com.shmashine.sender.server.elevator.BizElevatorService;
import com.shmashine.sender.server.metrics.PrometheusMetricsService;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/7/7 16:35
 * @since v1.0
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class PrometheusMetricsServiceImpl implements PrometheusMetricsService {
    private final MeterRegistry meterRegistry;
    private final BizElevatorService bizElevatorService;

    private Map<String, GaugeBuilder> offlineBuilderMap = new HashMap<>();

    private Map<String, GaugeBuilder> totalBuilderMap = new HashMap<>();

    private GaugeBuilder offlineBuilder;
    private GaugeBuilder totalBuilder;

    public void statisticsOfflineElevators() {
        log.info("statisticsOfflineElevators start");
        var map = SenderCounterServer.offlineCount("yidian");
        if (offlineBuilder == null) {
            offlineBuilder = new GaugeBuilder(meterRegistry, "mashine.sender.offline.count",
                    builder -> builder
                            .tag("uri", "/counter/group-offline/yidian")
                            .tag("totalElevator", String.valueOf(map.getTotal()))
                            .register(this.meterRegistry));
        }
        if (totalBuilder == null) {
            totalBuilder = new GaugeBuilder(meterRegistry, "mashine.sender.offline.total",
                    builder -> builder
                            .tag("uri", "/counter/group-offline/yidian")
                            .tag("totalElevator", String.valueOf(map.getTotal()))
                            .register(this.meterRegistry));
        }

        var offlineCount = offlineBuilder.setCounter(map.getOffline()).build();
        var totalCount = totalBuilder.setCounter(map.getTotal()).build();

        log.info("offline/total elevators : {}/{}", offlineCount.value(), totalCount.value());
    }

    public void statisticsOfflineProjectElevators() {
        log.info("statisticsOfflineProjectElevators start");
        var map = SenderCounterServer.offlineCountByProject("yidian");
        if (CollectionUtils.isEmpty(map)) {
            return;
        }
        log.info("statisticsOfflineProjectElevators project list {}", map.keySet());
        // 获取项目名
        var projectList = bizElevatorService.listProjectByIds(map.keySet())
                .stream().collect(Collectors.toMap(TblProject::getVProjectId, Function.identity()));

        for (var item : map.entrySet()) {
            if (offlineBuilderMap.get(item.getKey()) == null) {
                var offlineBuilderItem = new GaugeBuilder(meterRegistry, "mashine.sender.offline.project.count",
                        builder -> builder
                                .tag("uri", "/counter/group-offline/yidian")
                                .tag("projectId", item.getKey())
                                .tag("projectName", projectList.get(item.getKey()).getVProjectName())
                                .tag("totalElevator", String.valueOf(item.getValue().getTotal()))
                                .register(this.meterRegistry));
                offlineBuilderMap.put(item.getKey(), offlineBuilderItem);
            }
            if (totalBuilderMap.get(item.getKey()) == null) {
                var totalBuilderItem = new GaugeBuilder(meterRegistry, "mashine.sender.offline.project.total",
                        builder -> builder
                                .tag("uri", "/counter/group-offline/yidian")
                                .tag("projectId", item.getKey())
                                .tag("projectName", projectList.get(item.getKey()).getVProjectName())
                                .tag("totalElevator", String.valueOf(item.getValue().getTotal()))
                                .register(this.meterRegistry));
                totalBuilderMap.put(item.getKey(), totalBuilderItem);
            }
            var offlineCount = offlineBuilderMap.get(item.getKey()).setCounter(item.getValue().getOffline()).build();
            var totalCount = totalBuilderMap.get(item.getKey()).setCounter(item.getValue().getTotal()).build();
            log.info("offline/total {} elevators : {}/{}", projectList.get(item.getKey()).getVProjectName(),
                    offlineCount.value(), totalCount.value());
        }

    }
}
