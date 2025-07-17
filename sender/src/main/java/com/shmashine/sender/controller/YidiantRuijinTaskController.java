// Copyright (C) 2025 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.sender.platform.city.shanghai.YidianRuiJinServer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2025/5/19 16:05
 * @since v1.0
 */

@Slf4j
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class YidiantRuijinTaskController {
    private final YidianRuiJinServer yidianRuiJinServer;

    @GetMapping("/updateThirdPartyRuiJinElevatorTask")
    public String updateThirdPartyRuiJinElevatorTask() {
        log.info("updateThirdPartyRuiJinElevatorTask>>>>>电梯基本信息更新开始");
        yidianRuiJinServer.updateThirdPartyRuiJinElevator();
        return "success";
    }

    @GetMapping("/updateThirdPartyRuiJinMaintenanceTask")
    public String updateThirdPartyRuiJinMaintenanceTask() {
        log.info("updateThirdPartyRuiJinMaintenanceTask>>>>>维保信息更新开始");
        yidianRuiJinServer.updateThirdPartyRuiJinMaintenance();
        return "success";
    }

}
