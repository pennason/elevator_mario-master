// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.kafka.forward.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSON;
import com.shmashine.common.model.Result;
import com.shmashine.kafka.forward.service.RedisServiceI;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/10/16 11:49
 * @since v1.0
 */

@Slf4j
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class MessageController {
    private final RedisServiceI redisService;

    @Operation(summary = "根据电梯变化获取最新监控状态")
    @GetMapping("/monitor/{elevatorCode}")
    public Result<Map> getMonitorInfoByElevatorCode(@PathVariable("elevatorCode") String elevatorCode) {
        var res = redisService.getMonitorInfoByElevatorCode(elevatorCode);
        if (res == null) {
            return Result.error("未获取到监控信息");
        }
        return Result.success(JSON.parseObject(res.toString(), Map.class), "成功");
    }

    @Operation(summary = "根据电梯变化获取最新故障记录")
    @GetMapping("/fault/{elevatorCode}")
    public Result<Map> getFaultInfoByElevatorCode(@PathVariable("elevatorCode") String elevatorCode) {
        var res = redisService.getFaultInfoByElevatorCode(elevatorCode);
        if (res == null) {
            return Result.error("未获取到故障信息");
        }
        return Result.success(JSON.parseObject(res.toString(), Map.class), "成功");
    }

}
