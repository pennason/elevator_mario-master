package com.shmashine.api.controller.device;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import com.alibaba.fastjson2.JSON;
import com.shmashine.api.controller.device.vo.DeviceParamConfReqVO;
import com.shmashine.api.controller.device.vo.DeviceParamSyncReqVO;
import com.shmashine.api.controller.device.vo.RebootByElevatorReqVO;
import com.shmashine.api.controller.device.vo.SearchDeviceConfPageReqVO;
import com.shmashine.api.controller.device.vo.SensorConfigReqVO;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.device.SearchDeviceEventRecordModule;
import com.shmashine.api.module.device.UploadDeviceFileModule;
import com.shmashine.api.mongo.utils.MongoTemplateUtil;
import com.shmashine.api.service.system.TblDeviceServiceI;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.common.constants.MessageConstants;
import com.shmashine.common.utils.TimeUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 设备相关接口
 *
 * @author little.li
 */

@Slf4j
@RestController
@RequestMapping("/device")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Validated
public class DeviceController extends BaseRequestEntity {
    private final TblDeviceServiceI deviceService;
    private final RestTemplate restTemplate;
    private final RedisTemplate redisTemplate;
    private final BizUserService bizUserService;

    @Resource
    private MongoTemplateUtil mongoTemplateUtil;

    /**
     * 设备远程调试指令
     */
    @PostMapping("/frep")
    public Object sendDebugToDevice(@RequestBody UploadDeviceFileModule module) {
        String message = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put(MessageConstants.MESSAGE_TYPE, MessageConstants.TYPE_UPDATE);
            map.put(MessageConstants.MESSAGE_STYPE, MessageConstants.STYPE_FREP);
            map.put(MessageConstants.ELEVATOR_CODE, module.getElevatorCode());
            map.put(MessageConstants.SENSOR_TYPE, module.getSensorType());
            map.put(MessageConstants.STYPE_FREP, module.getFrep());

            //向盒子下发消息
            restTemplateSendMessageToCube(map);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult("0001", "msg1_05");
        }
        return ResponseResult.successObj(message);
    }

    @PostMapping("/sendSensorConfigToDevice")
    public ResponseResult sendSensorConfigToDevice(@Valid @RequestBody SensorConfigReqVO reqVO) {

        Map<String, String> map = new HashMap<>();

        var st = reqVO.getST();

        if ("humanType".equals(st)) {

            // type   //0：无人感， 1：Focus，2：蘑菇头人感
            // {"TY":"Update", "ST":"humanType", "etype":"MX201, "eid":"MX3502", "sensorType":"SINGLEBOX", "type":0/1/2}

            String sensorType = reqVO.getSensorType();

            if ("MotorRoom".equals(sensorType)) {
                return ResponseResult.success();
            }
            map.put("eid", reqVO.getEid());
            map.put(MessageConstants.SENSOR_TYPE, sensorType);

            map.put(MessageConstants.MESSAGE_TYPE, MessageConstants.TYPE_UPDATE);
            map.put(MessageConstants.MESSAGE_STYPE, st);
            map.put("etype", reqVO.getEtype());
            map.put("value", String.valueOf(reqVO.getProperties().get("value")));
        }

        try {
            //向盒子下发消息
            restTemplateSendMessageToCube(map);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult("0001", "msg1_05");
        }
        return ResponseResult.success();
    }

    /**
     * 获取设备配置列表
     */
    @PostMapping("/searchDeviceConfPage")
    public Object searchDeviceConfPage(@RequestBody SearchDeviceConfPageReqVO reqVO) {
        reqVO.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        reqVO.setUserId(getUserId());
        PageListResultEntity userMenu = deviceService.searchDeviceConfPage(reqVO);
        return ResponseResult.successObj(userMenu);
    }

    /**
     * 获取设备传感器列表和传感器故障状态
     *
     * @param elevatorCode 电梯编号
     * @return 传感器列表和传感器故障状态
     */
    @GetMapping("/getDeviceSensorAndStatus/{elevatorCode}")
    public ResponseResult getDeviceSensorAndStatus(@PathVariable("elevatorCode") String elevatorCode) {
        return ResponseResult.successObj(deviceService.getDeviceSensorAndStatus(elevatorCode));
    }

    /**
     * 设备远程重启
     */
    @PostMapping("/reboot")
    @Deprecated
    public Object sendRebootToDevice(@RequestBody UploadDeviceFileModule module) {
        String message = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put(MessageConstants.MESSAGE_TYPE, MessageConstants.TYPE_REBOOT);
            map.put(MessageConstants.ELEVATOR_CODE, module.getElevatorCode());
            map.put(MessageConstants.SENSOR_TYPE, module.getSensorType());

            //openfeign调用socket模块对外接口
            //socketClient.sendMessageToCube(message);

            //直接通过restTemplatesendMessageToCube调用Socket模块
            restTemplateSendMessageToCube(map);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult("0001", "msg1_05");
        }
        return ResponseResult.successObj(message);
    }

    /**
     * 设备重启-根据电梯编号批量重启
     */
    @PostMapping("/rebootByElevator")
    public ResponseResult rebootByElevator(@RequestBody RebootByElevatorReqVO reqVO) {

        reqVO.getSensorTypes().stream().forEach(sensorType -> {

            try {
                Map<String, String> map = new HashMap<>();
                map.put(MessageConstants.MESSAGE_TYPE, MessageConstants.TYPE_REBOOT);
                map.put(MessageConstants.ELEVATOR_CODE, reqVO.getElevatorCode());
                map.put(MessageConstants.SENSOR_TYPE, sensorType);

                restTemplateSendMessageToCube(map);

            } catch (Exception e) {
                log.error("设备重启指令下发失败，error：{}", ExceptionUtils.getStackTrace(e));
            }

        });


        return ResponseResult.successObj("success");
    }


    /**
     * 设备上下线记录
     */
    @PostMapping("/eventRecord")
    public Object eventRecord(@RequestBody SearchDeviceEventRecordModule module) {
        try {
            PageListResultEntity deviceEventRecordList = deviceService.searchDeviceEventRecord(module);
            return ResponseResult.successObj(deviceEventRecordList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult("0001", "msg1_08");
        }
    }

    /**
     * 设备历史波形图记录
     */
    @PostMapping("/waveForm")
    public Object waveForm(@RequestBody SearchDeviceEventRecordModule module) {
        try {
            PageListResultEntity deviceEventRecordList = deviceService.searchDeviceWaveForm(module);
            return ResponseResult.successObj(deviceEventRecordList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult("0001", "msg1_08");
        }
    }

    /**
     * 获取升级文件版本列表
     */
    @PostMapping("/getMasterVersions")
    public Object getMasterVersions(@RequestBody SearchDeviceEventRecordModule module) {
        module.setUserId(super.getUserId());
        module.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        return ResponseResult.successObj(deviceService.getMasterVersions(module));
    }

    /**
     * 调用socket服务向盒子下发消息
     *
     * @param map 请求参数
     */
    private void restTemplateSendMessageToCube(Map<String, String> map) {

        String message = JSON.toJSONString(map);

        String elevatorCode = !StringUtils.hasText(map.get("eid")) ? map.get("elevatorCode") : map.get("eid");

        String sensorType = map.get("sensorType");

        String key = "DEVICE:STATUS:" + elevatorCode + ":" + sensorType;
        Map<Object, Object> deviceStatus = redisTemplate.opsForHash().entries(key);
        String nodeIp = String.valueOf(deviceStatus.get("server_ip"));
        String nodePort = String.valueOf(deviceStatus.get("server_port"));

        // 请求路径：
        String url = String.format("http://%s:%d/cube/sendMessage", nodeIp, Integer.valueOf(nodePort));

        // 发出一个post请求
        try {
            String s = restTemplate.postForObject(url, message, String.class);
            log.info("[{}] ---升级指令下发成功，下发地址：[{}]，下发消息：[{}]，返回信息：[{}]\n", TimeUtils.nowTime(), url, message, s);
        } catch (RestClientException e) {
            e.printStackTrace();
            log.info("接口调用失败,入参为{}", message);
        }

    }


    /**
     * 获取设备通讯质量
     *
     * @param elevatorCode 电梯code
     */
    @GetMapping("/devicePingQuality/{elevatorCode}")
    public ResponseResult devicePingQuality(@PathVariable("elevatorCode") String elevatorCode) {

        //获取最近7日设备通讯质量
        var res = mongoTemplateUtil.getPingQualityByElevatorCode(elevatorCode,
                DateUtil.beginOfDay(DateUtil.offsetDay(DateTime.now(), -7)), DateUtil.endOfDay(DateTime.now()));

        return ResponseResult.successObj(res);
    }

    /**
     * 获取设备参数配置
     */
    @GetMapping("/getDeviceParamConf/{elevatorId}")
    public ResponseResult getDeviceParamConf(@PathVariable("elevatorId") String elevatorId) {
        return ResponseResult.successObj(deviceService.getDeviceParamConf(elevatorId));
    }

    /**
     * 设备参数配置
     */
    @PostMapping("/deviceParamConfigure")
    public ResponseResult deviceParamConfigure(@Valid @RequestBody List<DeviceParamConfReqVO> deviceParamConfReqVO) {
        return ResponseResult.successObj(deviceService.deviceParamConfigure(deviceParamConfReqVO, getUserId()));
    }

    /**
     * 获取设备版本列表
     *
     * @return 设备版本列表
     */
    @GetMapping("/getDeviceVersionList")
    public ResponseResult getDeviceVersionList() {
        return ResponseResult.successObj(deviceService.getDeviceVersionList());
    }

    /**
     * 设备参数同步
     */
    @PostMapping("/deviceParamSync")
    public ResponseResult deviceParamSync(@RequestBody DeviceParamSyncReqVO reqVO) {
        return ResponseResult.successObj(deviceService.deviceParamSync(reqVO));
    }

    /**
     * 设置设备是否开启人员检测
     *
     * @param elevatorCode 电梯编号
     * @param status       0：关闭（默认） 1：开启
     */
    @PostMapping("/setDetectedPeopleNumsIsOpen/{elevatorCode}/{status}")
    public ResponseResult setDetectedPeopleNumsIsOpen(@PathVariable("elevatorCode") String elevatorCode,
                                                      @PathVariable("status") Integer status) {
        return ResponseResult.successObj(deviceService.setDetectedPeopleNumsIsOpen(elevatorCode, status));
    }

}
