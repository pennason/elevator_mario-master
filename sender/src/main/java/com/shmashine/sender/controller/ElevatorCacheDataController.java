// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.model.Result;
import com.shmashine.sender.platform.city.shanghai.YidianSender;
import com.shmashine.sender.server.ElevatorCacheServiceI;
import com.shmashine.sender.server.elevator.BizElevatorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/11/27 16:43
 * @since v1.0
 */

@Slf4j
@RestController
@RequestMapping("/sender/cache")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ElevatorCacheDataController {
    private final ElevatorCacheServiceI elevatorCacheService;
    private final YidianSender yidianSender;
    private final BizElevatorService bizElevatorService;

    @GetMapping("/running/{elevatorCode}")
    public Result getRunningDataFromCache(@PathVariable("elevatorCode") String elevatorCode) {
        log.info("获取运行数据缓存 {}", elevatorCode);
        var res = elevatorCacheService.getMonitorCache(elevatorCode);
        return Result.success(res == null ? Map.of() : res, res == null ? "无运行信息" : "成功");
    }

    @GetMapping("/fault-list/{elevatorCode}")
    public Result getFaultListDataFromCache(@PathVariable("elevatorCode") String elevatorCode) {
        log.info("获取故障数据缓存 {}", elevatorCode);
        var res = elevatorCacheService.getFaultMessageCache(elevatorCode);
        return Result.success(CollectionUtils.isEmpty(res)
                ? Collections.emptyList() : res, CollectionUtils.isEmpty(res) ? "无故障记录" : "成功");
    }

    @PostMapping("/pushCountToYiDian/{elevatorCode}")
    public Result pushCountToYiDian(@PathVariable("elevatorCode") String elevatorCode) {
        // 查询 仪电的设备
        TblElevator elevator = bizElevatorService.getByElevatorCode(elevatorCode);
        yidianSender.postStatisticsData(elevator);
        return Result.success(elevator.toString());
    }
}
