package com.shmashine.haierCamera.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.haierCamera.service.HaierCameraService;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/10/18 14:48
 */
@RestController
@RequestMapping("/haierCamera")
public class HaierCameraController {

    @Autowired
    private HaierCameraService haierCameraService;

    @GetMapping("/getCameraHlsUrlByElevatorId/{elevatorId}")
    public String getCameraHlsUrlByElevatorId(@PathVariable String elevatorId) {
        return haierCameraService.getCameraHlsUrlByElevatorId(elevatorId);
    }


}
