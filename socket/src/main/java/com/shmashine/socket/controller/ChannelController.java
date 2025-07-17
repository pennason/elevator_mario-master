package com.shmashine.socket.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.socket.device.service.TblDeviceService;
import com.shmashine.socket.message.bean.SensorFaultShieldCache;
import com.shmashine.socket.netty.ChannelManager;
import com.shmashine.socket.utils.FloorDockingAlgorithm;

/**
 * socket模块对外接口 - channel接口
 *
 * @author little.li
 */
@RestController
@RequestMapping("/cube")
public class ChannelController {

    @Autowired
    private TblDeviceService deviceService;

    @Resource
    private FloorDockingAlgorithm floorDockingAlgorithm;

    /**
     * 获取所有channel名单
     */
    @GetMapping("/status/{elevatorCode}/{sensorType}")
    public String getStatus(@PathVariable("elevatorCode") String elevatorCode,
                            @PathVariable("sensorType") String sensorType) {

        return ChannelManager.getChannelStatus(elevatorCode, sensorType);
    }

    /**
     * 下发设备信息
     *
     * @param message 消息内容
     */
    @PostMapping("/sendMessage")
    public String sendMessageToCube(@RequestBody String message) {

        // 消息推送到设备
        JSONObject jsonObject = JSONObject.parseObject(message);

        String elevatorCode = !StringUtils.hasText(jsonObject.getString("eid"))
                ? jsonObject.getString("elevatorCode") : jsonObject.getString("eid");

        String sensorType = jsonObject.getString("sensorType");

        if ("sensors".equals(jsonObject.getString("ST"))) { //更新传感器故障屏蔽
            //删除传感器屏蔽缓存
            SensorFaultShieldCache.removeSensorFaultShieldCache(elevatorCode);
            //删除传感器关联故障屏蔽
            SensorFaultShieldCache.removeFaultShieldCache(elevatorCode);

            //获取传感器故障屏蔽列表
            HashMap<String, String> sensorFaultShields = deviceService.getSensorFaultShieldsByElevator(elevatorCode);

            if (sensorFaultShields != null && StringUtils.hasText(sensorFaultShields.get("sensorFaultType"))) {
                List<Integer> sensorFaultTypes =
                        Arrays.stream(sensorFaultShields.get("sensorFaultType").split(",")).distinct()
                                .map(i -> Integer.parseInt(i)).collect(Collectors.toList());

                //添加传感器屏蔽缓存
                SensorFaultShieldCache.addSensorFaultShieldCache(elevatorCode, sensorFaultTypes);
            }


            if (sensorFaultShields != null && StringUtils.hasText(sensorFaultShields.get("faultType"))) {
                List<Integer> faultTypes = Arrays.stream(sensorFaultShields.get("faultType").split(",")).distinct()
                        .map(i -> Integer.parseInt(i)).collect(Collectors.toList());
                //添加传感器关联故障屏蔽
                SensorFaultShieldCache.addFaultShieldCache(elevatorCode, faultTypes);
            }
        }

        ChannelManager.sendMessageToChannel(elevatorCode, sensorType, message);
        return "send success";
    }

    /**
     * 导出传感器故障屏蔽列表以及传感器关联故障屏蔽列表
     *
     * @param elevator 电梯
     * @param response 响应
     * @throws IOException IO异常
     */
    @PostMapping("/getSensorFaultShieldsCache")
    public void getSensorFaultShieldsCache(@RequestParam(required = false) String elevator,
                                           HttpServletResponse response) throws IOException {

        //获取传感器关联故障屏蔽
        Map<String, List<Integer>> faultShields = SensorFaultShieldCache.getFaultShields(elevator);
        //获取传感器故障屏蔽
        Map<String, List<Integer>> sensorFaultShields = SensorFaultShieldCache.getSensorFaultShields(elevator);

        Set<String> elevators = new HashSet<>();

        elevators.addAll(faultShields.keySet());
        elevators.addAll(sensorFaultShields.keySet());

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("传感器故障屏蔽列表", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        List<SensorFaultShieldCache> data = elevators.stream().map(elevatorCode ->
                SensorFaultShieldCache.builder().elevatorCode(elevatorCode)
                        .sensorFailureShields(sensorFaultShields.get(elevatorCode) == null
                                ? null : sensorFaultShields.get(elevatorCode).toString())
                        .failureShields(faultShields.get(elevatorCode) == null
                                ? null : faultShields.get(elevatorCode).toString()).build()
        ).collect(Collectors.toList());

        // 这里需要设置不关闭流
        EasyExcel.write(response.getOutputStream(), SensorFaultShieldCache.class)
                .autoCloseStream(Boolean.FALSE).sheet("传感器故障屏蔽列表1")
                .doWrite(data);

    }

    /**
     * 楼层停靠算法-获取预停靠楼层
     *
     * @param elevatorCode 电梯编号
     * @return 停靠楼层
     */
    @GetMapping("/getDockingFloor/{elevatorCode}")
    public Integer getDockingFloor(@PathVariable("elevatorCode") String elevatorCode) {
        return floorDockingAlgorithm.getDockingFloor(elevatorCode);
    }

}
