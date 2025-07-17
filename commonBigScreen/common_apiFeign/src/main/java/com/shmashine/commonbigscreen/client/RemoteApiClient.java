package com.shmashine.commonbigscreen.client;

import java.util.HashMap;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.alibaba.fastjson2.JSONObject;

/**
 * api 接口
 *
 * @author jiangheng
 * @version 1.0
 */
@Component
@FeignClient(value = "shmashine-api")
public interface RemoteApiClient {

    /**
     * 获取所有部门id
     */
    @GetMapping("/api/dept/getDeptIdList")
    List<String> getDeptIds(@RequestHeader("user_id") String userId);

    /**
     * 获取维保列表
     */
    @PostMapping("/api/maintenanceManagement/queryMaintenanceList")
    HashMap searchMaintenanceList(@RequestBody JSONObject jsonObject, @RequestHeader("user_id") String userId);

    /**
     * 获取维修记录列表
     */
    @PostMapping("/api/maintenanceManagement/queryFaultList")
    HashMap queryFaultList(@RequestBody JSONObject jsonObject, @RequestHeader("user_id") String userId);

    /**
     * 获取楼宇地图
     */
    @PostMapping("/api/map/searchVillageMap")
    HashMap<String, Object> searchVillageMap(@RequestHeader("user_id") String userId);

    /**
     * 获取电梯运行热力图
     */
    @PostMapping("/api/elevator/elevatorHeatMapNew")
    JSONObject getElevatorHeatMap(JSONObject request);

    /**
     * 获取视频监控流地址
     */
    @GetMapping("/api/camera/{elevatorId}")
    JSONObject getCameraUrl(@PathVariable("elevatorId") String elevatorId);
}
