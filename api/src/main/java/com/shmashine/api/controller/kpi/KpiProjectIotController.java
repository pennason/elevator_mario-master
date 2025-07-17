// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.controller.kpi;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.date.DateUtil;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.module.kpi.KpiProjectIotExcelModule;
import com.shmashine.api.service.elevator.BizElevatorService;
import com.shmashine.api.service.kpi.KpiProjectServiceI;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.common.dto.KpiProjectIotDTO;
import com.shmashine.common.dto.KpiProjectIotStatisticsDTO;
import com.shmashine.common.dto.KpiProjectRequestDTO;
import com.shmashine.common.dto.KpiProjectResponseGraphDTO;
import com.shmashine.common.dto.KpiProjectStatisticsDTO;
import com.shmashine.common.dto.ResponseResultDTO;
import com.shmashine.common.entity.KpiProjectIotEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 项目KPI接口
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/18 16:27
 * @since v1.0
 */

@Slf4j
@RestController
@RequestMapping("/kpi/project-iot")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Tag(name = "项目KPI接口", description = "项目KPI接口-开发者：（chenxue）")
@SecurityScheme(name = "token", scheme = "bearer", type = SecuritySchemeType.HTTP)
public class KpiProjectIotController extends BaseRequestEntity {
    private final KpiProjectServiceI kpiProjectService;
    private final BizElevatorService elevatorService;
    private final BizUserService bizUserService;

    // 统计比较

    @Deprecated
    @Operation(summary = "获取项目的KPI统计信息", security = {@SecurityRequirement(name = "token")})
    @GetMapping("/statistics/list-by-projects")
    public ResponseResultDTO<List<KpiProjectIotStatisticsDTO>> listIotStatisticsByProjects() {
        var projectIds = elevatorService.getProjectIdsByUserId(bizUserService.isAdmin(getUserId()), getUserId());
        if (CollectionUtils.isEmpty(projectIds)) {
            return ResponseResultDTO.successObj(Collections.emptyList());
        }
        //按日，周，月获取并比较
        var res = kpiProjectService.listIotStatisticsByProjectIds(projectIds);
        return ResponseResultDTO.successObj(res);
    }

    @Operation(summary = "获取项目的KPI统计信息-统一结果", security = {@SecurityRequirement(name = "token")})
    @GetMapping("/statistics/list-stattistics-by-projects")
    public ResponseResultDTO<List<KpiProjectStatisticsDTO>> listStatisticsByProjectsForIot() {
        var projectIds = elevatorService.getProjectIdsByUserId(bizUserService.isAdmin(getUserId()), getUserId());
        if (CollectionUtils.isEmpty(projectIds)) {
            return ResponseResultDTO.successObj(Collections.emptyList());
        }
        //按日，周，月获取并比较
        var res = kpiProjectService.listStatisticsByProjectIdsForIot(projectIds);
        return ResponseResultDTO.successObj(res);
    }

    /**
     * 折线图 stacked line
     *
     * @param requestDTO 请求参数
     * @return ResponseResultDTO<KpiProjectResponseGraphDTO>
     */
    @Operation(summary = "获取项目的KPI明细，用于图像展示", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/list-detail-for-graph")
    public ResponseResultDTO<KpiProjectResponseGraphDTO> listIotDetailByProjectId(@RequestBody KpiProjectRequestDTO requestDTO) {
        var projectIds = elevatorService.getProjectIdsByUserId(bizUserService.isAdmin(getUserId()), getUserId());
        if (CollectionUtils.isEmpty(projectIds)) {
            return ResponseResultDTO.successObj(KpiProjectResponseGraphDTO.builder().build());
        }
        if (!StringUtils.hasText(requestDTO.getProjectId())) {
            return ResponseResultDTO.error("项目ID不能为空", KpiProjectResponseGraphDTO.class);
        }
        if (!projectIds.contains(requestDTO.getProjectId())) {
            return ResponseResultDTO.error("无权限访问该项目", KpiProjectResponseGraphDTO.class);
        }
        // 最近30天
        var startDate = StringUtils.hasText(requestDTO.getStartDate()) ? requestDTO.getStartDate()
                : DateUtil.formatDate(DateUtil.offsetDay(DateUtil.date(), -30));
        var endDate = StringUtils.hasText(requestDTO.getEndDate()) ? requestDTO.getEndDate()
                : DateUtil.today();
        var detailList = kpiProjectService.listKpiProjectIotDetailByProjectIdsAndDateRange(
                Collections.singletonList(requestDTO.getProjectId()), startDate, endDate);
        if (CollectionUtils.isEmpty(detailList)) {
            return ResponseResultDTO.successObj(KpiProjectResponseGraphDTO.builder().build());
        }

        var res = KpiProjectResponseGraphDTO.builder()
                .projectId(detailList.get(0).getProjectId())
                .projectName(detailList.get(0).getProjectName())
                .categories(detailList.stream().map(KpiProjectIotEntity::getDay).toList())
                .series(new ArrayList<>(8) {{
                    add(new KpiProjectResponseGraphDTO.SeriesItem() {{
                        setName("电梯总数");
                        setData(detailList.stream().map(KpiProjectIotEntity::getElevatorTotal).toList());
                    }});
                    add(new KpiProjectResponseGraphDTO.SeriesItem() {{
                        setName("电梯离线数");
                        setData(detailList.stream().map(KpiProjectIotEntity::getElevatorOfflineMax).toList());
                    }});
                    add(new KpiProjectResponseGraphDTO.SeriesItem() {{
                        setName("电梯故障数");
                        setData(detailList.stream().map(KpiProjectIotEntity::getElevatorFaultMax).toList());
                    }});
                    add(new KpiProjectResponseGraphDTO.SeriesItem() {{
                        setName("摄像头总数");
                        setData(detailList.stream().map(KpiProjectIotEntity::getCameraTotal).toList());
                    }});
                    add(new KpiProjectResponseGraphDTO.SeriesItem() {{
                        setName("摄像头离线数");
                        setData(detailList.stream().map(KpiProjectIotEntity::getCameraOfflineMax).toList());
                    }});
                }})
                .build();
        return ResponseResultDTO.successObj(res);
    }

    // 查询明细

    /**
     * 按查询条件获取每日项目的KPI信息明细列表
     *
     * @param requestDTO 查询条件
     * @return KPI信息明细列表
     */
    @Operation(summary = "按查询条件获取每日项目的KPI信息明细列表", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/list-by-conditions")
    public ResponseResultDTO<List<KpiProjectIotDTO>> listIotByConditions(@RequestBody KpiProjectRequestDTO requestDTO) {
        // 有权限的项目
        var projectIds = elevatorService.getProjectIdsByUserId(bizUserService.isAdmin(getUserId()), getUserId());
        if (CollectionUtils.isEmpty(projectIds)) {
            return ResponseResultDTO.successObj(Collections.emptyList());
        }
        // 参数处理
        checkRequestParams(projectIds, requestDTO);
        if (CollectionUtils.isEmpty(projectIds)) {
            return ResponseResultDTO.successObj(Collections.emptyList());
        }
        var entities = kpiProjectService.listKpiProjectIotDetailByProjectIdsAndDateRange(projectIds,
                requestDTO.getStartDate(), requestDTO.getEndDate());
        if (CollectionUtils.isEmpty(entities)) {
            return ResponseResultDTO.successObj(Collections.emptyList());
        }
        var res = entities.stream()
                .map(item -> KpiProjectIotDTO.builder()
                        .projectId(item.getProjectId())
                        .projectName(item.getProjectName())
                        .day(item.getDay())
                        .elevatorTotal(item.getElevatorTotal())
                        .elevatorOfflineMax(item.getElevatorOfflineMax())
                        .elevatorFaultMax(item.getElevatorFaultMax())
                        .cameraTotal(item.getCameraTotal())
                        .cameraOfflineMax(item.getCameraOfflineMax())
                        .build())
                .toList();
        // 过滤项目名称
        if (StringUtils.hasText(requestDTO.getProjectName())) {
            res = res.stream()
                    .filter(item -> StringUtils.hasText(item.getProjectName())
                            && item.getProjectName().contains(requestDTO.getProjectName()))
                    .toList();
        }
        return ResponseResultDTO.successObj(res);
    }

    // 导出

    @Operation(summary = "按开始和结束日期导出每日项目的KPI信息", security = {@SecurityRequirement(name = "token")})
    @Parameters({
            @Parameter(name = "startDate", description = "开始日期 yyyy-MM-dd", required = true),
            @Parameter(name = "endDate", description = "结束日期 yyyy-MM-dd", required = true)
    })
    @GetMapping("/export/export-by-range/{startDate}/{endDate}")
    public void exportExcelIotByDay(@PathVariable("startDate") String startDate,
                                    @PathVariable("endDate") String endDate, HttpServletResponse response) {
        exportExcelIotByDateRange(getUserId(), startDate, endDate, "项目KPI导出明细-指定日期范围", response);
    }

    @Operation(summary = "按天导出每日项目的KPI信息", security = {@SecurityRequirement(name = "token")})
    @Parameters({
            @Parameter(name = "days", description = "天数，如3天", required = true, example = "3")
    })
    @GetMapping("/export/export-by-day/{days}")
    public void exportExcelIotByDay(@PathVariable("days") Integer days, HttpServletResponse response) {
        // days天前
        var daysAgo = DateUtil.formatDate(DateUtil.offsetDay(DateUtil.date(), -days));
        var today = DateUtil.today();
        exportExcelIotByDateRange(getUserId(), daysAgo, today, "项目KPI导出明细-按天", response);
    }

    @Operation(summary = "按周导出每日项目的KPI信息", security = {@SecurityRequirement(name = "token")})
    @GetMapping("/export/export-by-week")
    public void exportExcelIotByWeek(HttpServletResponse response) {
        // 获取本周第一天
        //var firstDayOfWeek = DateUtil.formatDate(DateUtil.beginOfWeek(DateUtil.date()));
        var firstDayOfWeek = DateUtil.formatDate(DateUtil.offsetDay(DateUtil.date(), -6));
        var today = DateUtil.today();
        exportExcelIotByDateRange(getUserId(), firstDayOfWeek, today, "项目KPI导出明细-按周", response);
    }

    @Operation(summary = "按月导出每日项目的KPI信息", security = {@SecurityRequirement(name = "token")})
    @GetMapping("/export/export-by-month")
    public void exportExcelIotByMonth(HttpServletResponse response) {
        // 获取本月第一天
        //var firstDayOfMonth = DateUtil.formatDate(DateUtil.beginOfMonth(DateUtil.date()));
        var firstDayOfMonth = DateUtil.formatDate(DateUtil.offsetDay(DateUtil.date(), -30));
        var today = DateUtil.today();
        exportExcelIotByDateRange(getUserId(), firstDayOfMonth, today, "项目KPI导出明细-按月", response);
    }


    // 内部方法


    private void checkRequestParams(List<String> projectIds, KpiProjectRequestDTO requestDTO) {
        // 传入参数的项目与有权限的项目取交集
        if (!CollectionUtils.isEmpty(requestDTO.getProjectIds())) {
            projectIds.retainAll(requestDTO.getProjectIds());
        }
        // 单个项目ID
        if (StringUtils.hasText(requestDTO.getProjectId())) {
            projectIds.retainAll(Collections.singletonList(requestDTO.getProjectId()));
        }
        if (!StringUtils.hasText(requestDTO.getStartDate())) {
            requestDTO.setStartDate(DateUtil.today());
        }
        if (!StringUtils.hasText(requestDTO.getEndDate())) {
            requestDTO.setEndDate(requestDTO.getStartDate());
        }
    }

    private void exportExcelIotByDateRange(String userId, String startDate, String endDate, String fileNamePrefix,
                                           HttpServletResponse response) {
        var projectIds = elevatorService.getProjectIdsByUserId(bizUserService.isAdmin(userId), userId);
        if (CollectionUtils.isEmpty(projectIds)) {
            doWriteResponseResult(response, ResponseResultDTO.successObj("无任何项目"));
            return;
        }
        var res = kpiProjectService.listKpiProjectIotDetailByProjectIdsAndDateRange(projectIds, startDate, endDate)
                .stream()
                .sorted(Comparator.comparing(KpiProjectIotEntity::getDay))
                .distinct()
                .toList();
        if (CollectionUtils.isEmpty(res)) {
            doWriteResponseResult(response, ResponseResultDTO.successObj("无任何数据"));
            return;
        }
        doWriteResponseExcel(response, res, fileNamePrefix);
    }

    private void doWriteResponseExcel(HttpServletResponse response, List<KpiProjectIotEntity> res, String fileNamePrefix) {
        // 这里URLEncoder.encode可以防止中文乱码
        var fileName = URLEncoder.encode(fileNamePrefix, StandardCharsets.UTF_8).replaceAll("\\+", "%20")
                + "-" + DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss");
        // 设置头信息
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        try (var excelWriter = EasyExcel.write(response.getOutputStream()).build()) {
            // 按项目分组
            res.stream().collect(Collectors.groupingBy(KpiProjectIotEntity::getProjectId)).forEach((projectId, list) -> {
                var sheetName = list.get(0).getProjectName();
                if (!StringUtils.hasText(sheetName)) {
                    sheetName = projectId;
                }
                String finalSheetName = sheetName;
                var writeSheet = EasyExcel.writerSheet(sheetName)
                        .registerWriteHandler(new CellWriteHandler() {
                            @Override
                            public void afterCellDispose(CellWriteHandlerContext context) {
                                var cell = context.getCell();
                                if (Boolean.TRUE.equals(context.getHead()) && cell.getRowIndex() == 0
                                        && cell.getColumnIndex() == 1) {
                                    // 自定义表头字段
                                    cell.setCellValue(finalSheetName);
                                }
                            }
                        })
                        .head(KpiProjectIotExcelModule.class)
                        .build();
                excelWriter.write(list.stream()
                        .map(item -> KpiProjectIotExcelModule.builder()
                                .day(item.getDay())
                                .elevatorTotal(item.getElevatorTotal())
                                .elevatorOfflineMax(item.getElevatorOfflineMax())
                                .elevatorFaultMax(item.getElevatorFaultMax())
                                .cameraTotal(item.getCameraTotal())
                                .cameraOfflineMax(item.getCameraOfflineMax())
                                .build())
                        .toList(), writeSheet);
            });
        } catch (Exception e) {
            doWriteResponseResult(response, ResponseResultDTO.successObj("导出失败"));
        }
    }


}
