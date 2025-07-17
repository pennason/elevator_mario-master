package com.shmashine.api.controller.elevator;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.entity.TblElevatorHeatMap;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.service.elevator.ElevatorHeatMapService;

@RestController
@RequestMapping("/elevator")
public class ElevatorHeatMapController {

    @Resource
    private ElevatorHeatMapService elevatorHeatMapService;

    @PostMapping("/elevatorHeatMap")
    public Object elevatorHeatMap(@RequestBody TblElevatorHeatMap elevatorHeatMapParam) {
        List result = elevatorHeatMapService.getElevatorHeatMap(elevatorHeatMapParam);
        return ResponseResult.successObj(result);
    }

    @PostMapping("/elevatorHeatMapNew")
    public Object elevatorHeatMapNew(@RequestBody TblElevatorHeatMap elevatorHeatMapParam) {
        List result = elevatorHeatMapService.getElevatorHeatMapNew(elevatorHeatMapParam);
        return ResponseResult.successObj(result);
    }
}
