package com.shmashine.pm.api.controller.deviceSensor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.common.utils.SnowFlakeUtils;
import com.shmashine.pm.api.entity.TblDevice;
import com.shmashine.pm.api.entity.TblDeviceSensor;
import com.shmashine.pm.api.entity.TblSensorConfig;
import com.shmashine.pm.api.entity.base.BaseRequestEntity;
import com.shmashine.pm.api.entity.base.ResponseResult;
import com.shmashine.pm.api.redis.RedisService;
import com.shmashine.pm.api.service.deviceSensor.TblDeviceSensorService;
import com.shmashine.pm.api.service.elevator.TblDeviceService;
import com.shmashine.pm.api.service.sensorConfig.TblSensorConfigService;

import lombok.extern.slf4j.Slf4j;

/**
 * 设备传感器配置
 */
@Slf4j
@RequestMapping("deviceSensor")
@RestController
public class DeviceSensorController extends BaseRequestEntity {

    @Autowired
    private TblDeviceSensorService tblDeviceSensorService;
    @Autowired
    private TblSensorConfigService tblSensorConfigService;
    @Autowired
    private TblDeviceService tblDeviceService;
    @Autowired
    private RedisService redisService;

    @PostMapping("/getList")
    public Object getList(@RequestBody TblDeviceSensor tblDeviceSensor) {
        return ResponseResult.successObj(tblDeviceSensorService.selectByEntity(tblDeviceSensor));
    }

    @PostMapping("/getBizList")
    @Transactional(rollbackFor = Exception.class)
    public Object getBizList(@RequestBody TblDeviceSensor tblDeviceSensor) {

        TblDevice searchModule = new TblDevice();
        searchModule.setVElevatorCode(tblDeviceSensor.getvElevatorCode());
        List<TblDevice> tblDevices = tblDeviceService.selectByEntity(searchModule);

        HashMap<String, List<HashMap<String, Object>>> result = new HashMap<>();
        List<HashMap<String, Object>> map = new ArrayList<HashMap<String, Object>>();
        String sensorType;

        for (TblDevice device : tblDevices) {
            tblDeviceSensor.setvDeviceId(device.getVDeviceId());
            List<Map> list = tblDeviceSensorService.getBizList(tblDeviceSensor);
            if (CollectionUtils.isEmpty(list)) {
                sensorType = device.getVSensorType();

                List<TblSensorConfig> tblSensors = tblSensorConfigService.selectBySensorType(sensorType);
                List<TblDeviceSensor> tblDeviceSensors = new ArrayList<>();

                for (TblSensorConfig tblSensorConfig : tblSensors) {
                    TblDeviceSensor deviceSensor = new TblDeviceSensor();
                    deviceSensor.setvDeviceSensorId(SnowFlakeUtils.nextStrId());

                    deviceSensor.setvElevatorCode(device.getVElevatorCode());
                    deviceSensor.setvSensorConfigId(tblSensorConfig.getvSensorConfigId());
                    deviceSensor.setDtCreateTime(new Date());
                    deviceSensor.setDtModifyTime(new Date());
                    deviceSensor.setvCreateUserId(getUserId());
                    deviceSensor.setvModifyUserId(getUserId());
                    deviceSensor.setvDeviceId(device.getVDeviceId());
                    deviceSensor.setiSensorChose(0);
                    deviceSensor.setvSensorType(device.getVSensorType());
                    tblDeviceSensors.add(deviceSensor);
                }
                HashMap<String, Object> subMap = new HashMap<>();
                subMap.put("id", device.getVDeviceId());

                subMap.put("send", redisService.existsSensorConfigKey(device.getVElevatorCode(),
                        device.getVSensorType()));

                subMap.put("deviceSensor", tblDeviceSensors);
                map.add(subMap);
            } else {
                HashMap<String, Object> subMap = new HashMap<>();
                subMap.put("id", device.getVDeviceId());
                subMap.put("send", redisService.existsSensorConfigKey(device.getVElevatorCode(),
                        device.getVSensorType()));

                subMap.put("deviceSensor", list.stream().filter(sensor -> sensor.get("vDeviceId")
                        .equals(device.getVDeviceId())).collect(Collectors.toList()));

                map.add(subMap);
            }
        }
        result.put("devices", map);

        return ResponseResult.successObj(result);
    }

    @PostMapping("/insert")
    public Object insert(@RequestBody TblDeviceSensor tblDeviceSensor) {
        return ResponseResult.successObj(tblDeviceSensorService.insert(tblDeviceSensor));
    }
}
