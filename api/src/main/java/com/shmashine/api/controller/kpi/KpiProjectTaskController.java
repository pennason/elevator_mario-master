// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.controller.kpi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.task.KpiProjectTask;
import com.shmashine.common.dto.ResponseResultDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 手动触发统计任务
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/12/6 10:44
 * @since v1.0
 */

@Slf4j
@Profile({"prod"})
@RestController
@RequestMapping("/kpi/project-task")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Tag(name = "项目KPI任务接口-手动触发", description = "项目KPI任务接口-手动触发-开发者：（chenxue）")
public class KpiProjectTaskController {
    private final KpiProjectTask kpiProjectTask;

    @Operation(summary = "项目KPI", security = {@SecurityRequirement(name = "token")})
    @GetMapping("/start_iot-statistics")
    public ResponseResultDTO<String> startProjectIotTask() {
        kpiProjectTask.scheduledKpiProjectIotTask();
        return ResponseResultDTO.successObj("success");
    }

    @Operation(summary = "平台KPI-北向推送", security = {@SecurityRequirement(name = "token")})
    @GetMapping("/start_north-push-statistics")
    public ResponseResultDTO<String> startProjectNorthPushTask() {
        kpiProjectTask.scheduledKpiProjectNorthPushTask();
        return ResponseResultDTO.successObj("success");
    }
}
