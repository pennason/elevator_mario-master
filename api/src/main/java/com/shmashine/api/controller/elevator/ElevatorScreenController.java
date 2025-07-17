package com.shmashine.api.controller.elevator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.elevator.ElevatorScreenModule;
import com.shmashine.api.service.elevator.BizElevatorService;


/**
 * 梯内广告屏接口
 *
 * @author little.li
 */
@RestController
@RequestMapping("/elevatorScreen")
public class ElevatorScreenController {


    private final BizElevatorService elevatorService;

    @Autowired
    public ElevatorScreenController(BizElevatorService elevatorService) {
        this.elevatorService = elevatorService;
    }


    /**
     * 梯内小屏 - 电梯详情接口
     *
     * @param elevatorCode 电梯编号
     * @return #type:com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.module.elevator.ElevatorScreenModule#
     */
    @GetMapping("/elevatorInfo/{elevatorCode}")
    public Object elevatorInfo(@PathVariable("elevatorCode") String elevatorCode) {
        ElevatorScreenModule elevatorScreenModule = elevatorService.getElevatorScreenInfo(elevatorCode);
        return ResponseResult.successObj(elevatorScreenModule);
    }


}
