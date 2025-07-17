package com.shmashine.userclientapplets.client;

import java.util.HashMap;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.alibaba.fastjson2.JSONObject;

/**
 * 远程调用api
 *
 * @author jiangheng
 * @version V1.0.0 -2022/2/9 13:53
 */
@Component
@FeignClient(url = "${endpoint.shmashine-api:shmashine-api:8080}", value = "shmashine-api")
public interface RemoteApiClient {

    /**
     * 获取维保列表
     *
     * @param jsonObject 请求体
     */
    @PostMapping("/api/maintenanceManagement/queryMaintenanceList")
    HashMap searchMaintenanceList(@RequestBody JSONObject jsonObject, @RequestHeader("user_id") String userId);

    /**
     * 获取维修记录列表
     *
     * @param jsonObject 请求体
     */
    @PostMapping("/api/maintenanceManagement/queryFaultList")
    HashMap queryFaultList(@RequestBody JSONObject jsonObject, @RequestHeader("user_id") String userId);

    /**
     * 获取故障详情
     *
     * @param faultId 故障id
     */
    @GetMapping("/api/fault/getFaultDetail/{faultId}")
    HashMap getFaultDetail(@PathVariable("faultId") String faultId);

    /**
     * 添加收藏电梯
     *
     * @param jsonObject 请求体
     * @param userId     用户id
     */
    @PostMapping("/api/elevatorCollect/insertUserCollectElevatorInfo")
    HashMap insertCollectElevator(@RequestBody JSONObject jsonObject, @RequestHeader("user_id") String userId);

    /**
     * 取消电梯收藏
     *
     * @param jsonObject 请求体
     * @param userId     用户id
     */
    @PostMapping("/api/elevatorCollect/deleteUserCollectElevatorInfo")
    HashMap cancelCollectElevator(@RequestBody JSONObject jsonObject, @RequestHeader("user_id") String userId);

    /**
     * 获取电梯收藏列表
     *
     * @param userId 用户id
     */
    @PostMapping("/api/elevatorCollect/searchElevatorCollectList")
    HashMap searchElevatorCollectList(@RequestBody JSONObject jsonObject, @RequestHeader("user_id") String userId);

    /**
     * 获取电梯详情
     *
     * @param elevatorId 电梯id
     */
    @GetMapping("/api/elevator/{elevatorId}")
    JSONObject getElevatorInfoById(@PathVariable("elevatorId") String elevatorId);

    /**
     * 获取电梯运行热力图
     *
     * @param jsonObject 请求体
     */
    @PostMapping("/api/elevator/elevatorHeatMapNew")
    JSONObject getElevatorHeatMap(@RequestBody JSONObject jsonObject);

    /**
     * 获取故障类型
     *
     * @param elevatorType 电梯类型
     * @param eventType    事件类型
     */
    @GetMapping("/api/fault/definition/definitionSearch/{elevatorType}/{eventType}")
    JSONObject getFaultType(@PathVariable("elevatorType") Integer elevatorType,
                            @PathVariable("eventType") Integer eventType);

    /**
     * 获取视频监控流地址
     *
     * @param elevatorId 电梯id
     */
    @GetMapping("/api/camera/{elevatorId}")
    JSONObject getCameraUrl(@PathVariable("elevatorId") String elevatorId);

    /**
     * 获取用户菜单权限
     *
     * @param jsonObject 请求体
     * @param userId     用户id
     */
    @PostMapping("/api/loginV2")
    JSONObject loginV2(@RequestBody JSONObject jsonObject, @RequestHeader("user_id") String userId);

    /**
     * 获取年检电梯记录列表
     *
     * @param jsonObject 请求体
     * @return 年检电梯记录列表
     */
    @PostMapping("/api/maintenanceManagement/queryAnnualInspectionList")
    HashMap getUnAnnualInspectionElevatorByPage(@RequestBody JSONObject jsonObject,
                                                @RequestHeader("user_id") String userId);

    /**
     * 获取年检记录列表
     *
     * @param jsonObject 请求体
     * @return 年检记录
     */
    @PostMapping("/api/maintenanceManagement/queryRuiJinAnnualCheckList")
    HashMap queryRuiJinAnnualCheckList(@RequestBody JSONObject jsonObject, @RequestHeader("user_id") String userId);
}
