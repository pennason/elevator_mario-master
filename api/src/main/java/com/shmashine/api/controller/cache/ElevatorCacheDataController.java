// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.controller.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.service.cache.ElevatorCacheDataServiceI;
import com.shmashine.common.model.Result;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 缓存中获取 运行数据 和 故障列表
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/11/28 15:07
 * @since v1.0
 */

@Slf4j
@RestController
@RequestMapping("/elevator-cache")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Tag(name = "电梯运行/故障数据缓存", description = "电梯运行/故障数据缓存-开发者：（chenxue）")
@SecurityScheme(name = "token", scheme = "bearer", type = SecuritySchemeType.HTTP)
public class ElevatorCacheDataController {
    private final ElevatorCacheDataServiceI cacheDataService;


    @Operation(summary = "获取运行数据缓存", security = {@SecurityRequirement(name = "token")})
    @GetMapping("/running/{elevatorCode}")
    public Result getRunningDataFromCache(@PathVariable("elevatorCode") String elevatorCode) {
        return cacheDataService.getRunningDataFromCache(elevatorCode);
    }

    @Operation(summary = "获取故障列表数据缓存", security = {@SecurityRequirement(name = "token")})
    @GetMapping("/fault-list/{elevatorCode}")
    public Result getFaultListDataFromCache(@PathVariable("elevatorCode") String elevatorCode) {
        return cacheDataService.getFaultListDataFromCache(elevatorCode);
    }

}
