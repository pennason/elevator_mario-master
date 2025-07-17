package com.shmashine.haierCamera.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.haierCamera.entity.HaierCameraFault;
import com.shmashine.haierCamera.entity.HaierCameraResponseResult;
import com.shmashine.haierCamera.service.HaierCameraService;


/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/10/21 10:31
 */
@RestController
@RequestMapping("/camera")
public class HaierCameraFaultController {

    @Autowired
    private HaierCameraService haierCameraService;

    @PostMapping("/pushFault")
    public HaierCameraResponseResult pushFault(@RequestBody HaierCameraFault haierCameraFault) {
        return haierCameraService.pushFault(haierCameraFault);
    }
}
