package com.shmashine.socket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.common.model.Result;
import com.shmashine.socket.elevator.service.TblRedirectElevatorMappingService;

/**
 * 配置接口
 */
@RestController
@RequestMapping("/setting")
public class SettingController {

    @Autowired
    private TblRedirectElevatorMappingService tblRedirectElevatorMappingService;

    /**
     * 清空配置
     */
    @GetMapping("/redirect/clear")
    public Result redirectClear() {
        tblRedirectElevatorMappingService.clear();
        return Result.success("clear success!");
    }

}
