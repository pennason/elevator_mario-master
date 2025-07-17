package com.shmashine.api.controller.elevator;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.service.elevator.TblElevatorSensorServiceI;

/**
 * 电梯对应传感器接口
 *
 * @author little.li
 */
@RestController
@RequestMapping("/elevator/sensor")
public class ElevatorSensorController extends BaseRequestEntity {


    @Autowired
    private TblElevatorSensorServiceI elevatorSensorService;


    /**
     * 获取电梯对应传感器列表
     *
     * @param elevatorCode 电梯编号
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @GetMapping("/sensorList/{elevatorCode}")
    public Object getElevatorSensorList(@PathVariable String elevatorCode) {
        List<Map<String, Object>> sensorList = elevatorSensorService.getSensorListByElevatorCode(elevatorCode);
        return ResponseResult.successObj(sensorList);
    }


    /**
     * 新增电梯传感器对应关系
     *
     * @param elevatorCode 电梯编号
     * @param arr          传感器列表
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/insert/{elevatorCode}")
    public Object insertElevatorSensor(@PathVariable("elevatorCode") String elevatorCode, @RequestBody List<String> arr) {

        // 数据库操作
        elevatorSensorService.insertElevatorSensor(elevatorCode, arr);

        // TODO 向设备下发故障黑名单

        return ResponseResult.success();
    }


    /**
     * 获取电梯对应传感器列表 - 监控屏蔽列表
     *
     * @param elevatorCode 电梯编号
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @GetMapping("/pageDisplay/{elevatorCode}")
    public Object getPageDisplay(@PathVariable("elevatorCode") String elevatorCode) {
        Map<String, Map<String, Object>> pageDisplay = elevatorSensorService.getPageDisplayByElevatorCode(elevatorCode);
        return ResponseResult.successObj(pageDisplay);
    }


}
