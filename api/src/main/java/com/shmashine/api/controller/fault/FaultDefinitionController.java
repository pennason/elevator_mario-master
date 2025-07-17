package com.shmashine.api.controller.fault;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.fault.input.FaultDefinitionModule;
import com.shmashine.api.service.elevator.BizElevatorService;
import com.shmashine.api.service.fault.BizFaultDefinition0902ServiceI;
import com.shmashine.api.service.fault.TblFaultDefinitionServiceI;
import com.shmashine.api.service.system.TblDeviceServiceI;
import com.shmashine.common.constants.SocketConstants;
import com.shmashine.common.entity.TblDevice;
import com.shmashine.common.entity.TblFaultDefinition;
import com.shmashine.common.entity.TblFaultDefinition0902;

/**
 * 故障定义接口
 *
 * @author little.li
 */
@RestController
@RequestMapping("/fault/definition")
public class FaultDefinitionController {


    @Autowired
    private TblFaultDefinitionServiceI faultDefinitionService;

    @Autowired
    private BizFaultDefinition0902ServiceI bizFaultDefinition0902ServiceI;

    @Autowired
    private BizElevatorService elevatorService;

    @Autowired
    private TblDeviceServiceI deviceService;

    /**
     * 获取所有故障列表数据
     *
     * @return #type: com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.entity.TblFaultDefinition#
     */
    @GetMapping("")
    public Object getAllDefinition() {
        List<TblFaultDefinition> faultDefinitionList = faultDefinitionService.getAllFaultDefinition();
        return ResponseResult.successObj(faultDefinitionList);
    }


    /**
     * 根据电梯类型 获取故障列表数据
     *
     * @param elevatorType 电梯类型
     * @return #type: com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.entity.TblFaultDefinition0902#
     */
    @GetMapping("/{elevatorType}/{sensorType}")
    public Object get(@PathVariable("elevatorType") Integer elevatorType, @PathVariable Integer sensorType) {
        List<TblFaultDefinition0902> faultDefinitionList = faultDefinitionService.getFaultDefinitionListByElevatorType(elevatorType, sensorType);
        return ResponseResult.successObj(faultDefinitionList);
    }

    /**
     * 获取故障定义接口
     *
     * @param elevatorType 电梯类型
     * @param eventType    事件类型
     * @return #type: com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.entity.TblFaultDefinition0902#
     */
    @GetMapping("/definitionSearch/{elevatorType}/{eventType}")
    public Object definitionAll(@PathVariable("elevatorType") Integer elevatorType, @PathVariable("eventType") Integer eventType) {
        List<TblFaultDefinition0902> faultDefinitionList = bizFaultDefinition0902ServiceI.getFaultDefinitionSearch(elevatorType, eventType);
        return ResponseResult.successObj(faultDefinitionList);
    }

    /**
     * 获取不文明行为列表
     *
     * @return #type: com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.entity.TblFaultDefinition0902#
     */
    @GetMapping("/definitionUncivilizedBehaviorAll")
    public Object definitionUncivilizedBehaviorAll() {
        List<TblFaultDefinition0902> faultDefinitionList = bizFaultDefinition0902ServiceI.getDefinitionUncivilizedBehaviorAll();
        return ResponseResult.successObj(faultDefinitionList);
    }


    /**
     * 监控页面故障定义接口
     *
     * @param elevatorCode 电梯编号
     * @return #type: com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.entity.TblFaultDefinition0902#
     */
    @GetMapping("/definitionInfo/{elevatorCode}")
    public Object definitionInfo(@PathVariable("elevatorCode") String elevatorCode) {
        List<TblDevice> deviceList = deviceService.deviceListByElevatorCode(elevatorCode);
        Integer elevatorType = elevatorService.getElevatorTypeByElevatorCode(elevatorCode);
        int eventType = 1;

        for (TblDevice device : deviceList) {
            // 如果是前装设备则获取对应的故障定义
            if (device.getVSensorType().equals(SocketConstants.SENSOR_TYPE_FRONT)) {
                eventType = Integer.parseInt(device.getVPlatformType());
            }
            if (device.getVSensorType().equals(SocketConstants.SENSOR_TYPE_ESCALATOR)) {
                eventType = 5;
            }
        }

        List<TblFaultDefinition0902> faultDefinitionList = bizFaultDefinition0902ServiceI.getFaultDefinitionSearch(elevatorType, eventType);
        return ResponseResult.successObj(faultDefinitionList);
    }


    /**
     * 故障屏蔽规则
     *
     * @param elevatorCode 电梯编号
     */
    @GetMapping("/faultShieldInfo/{elevatorCode}")
    public Object faultShieldInfo(@PathVariable("elevatorCode") String elevatorCode) {
        List<Map<String, Object>> faultShieldInfo = faultDefinitionService.faultShieldInfo(elevatorCode);
        return ResponseResult.successObj(faultShieldInfo);
    }


    /**
     * 更新故障屏蔽规则
     *
     * @param elevatorCode 电梯编号
     */
    @PostMapping("/updateFaultShield/{elevatorCode}")
    public Object updateFaultShield(@PathVariable("elevatorCode") String elevatorCode, @RequestBody List<Map<String, String>> faultShieldInfo) {
        faultDefinitionService.updateFaultShield(elevatorCode, faultShieldInfo);
        return ResponseResult.success();
    }


    /**
     * 批量更新故障屏蔽规则
     *
     * @param
     */
    @PostMapping("/batchUpdateFaultShield")
    public Object batchUpdateFaultShield(@RequestBody FaultDefinitionModule faultDefinitionModule) {
        faultDefinitionService.batchUpdateFaultShield(faultDefinitionModule);
        return ResponseResult.success();
    }
}
