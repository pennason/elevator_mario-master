package com.shmashine.userclientapplets.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shmashine.userclientapplets.entity.Elevator;
import com.shmashine.userclientapplets.entity.Maintenance;
import com.shmashine.userclientapplets.entity.PageListResultEntity;
import com.shmashine.userclientapplets.entity.ResponseResult;
import com.shmashine.userclientapplets.entity.SearchElevatorModule;
import com.shmashine.userclientapplets.entity.SearchFaultModule;

/**
 * ElevatorService
 *
 * @author jiangheng
 * @version V1.0 - 2022/2/8 14:35
 */
public interface ElevatorService extends IService<Elevator> {

    /**
     * 统计当月的电梯数、困人次数、维保逾期、急修次数、故障、年检等信息
     *
     * @param searchFaultModule 查询参数
     */
    Map<String, Integer> queryElevatorStatus(SearchFaultModule searchFaultModule);

    /**
     * 添加电梯收藏
     *
     * @param elevatorId 电梯id
     * @param userId     用户id
     */
    HashMap insertCollectElevator(String elevatorId, String userId);

    /**
     * 取消电梯收藏
     *
     * @param elevatorId 电梯id
     * @param userId     用户id
     */
    HashMap cancelCollectElevator(String elevatorId, String userId);

    /**
     * 获取电梯收藏列表
     *
     * @param userId 用户id
     */
    HashMap searchElevatorCollectList(String userId);

    /**
     * 获取今日故障、需维保等信息
     *
     * @param userId 用户id
     * @param admin  是否为管理员
     */
    List<String> getWarningByDay(String userId, boolean admin);

    /**
     * 获取电梯列表
     *
     * @param searchElevatorModule 查询参数
     */
    PageListResultEntity queryElevatorAndCollectList(SearchElevatorModule searchElevatorModule);

    /**
     * 获取电梯详情
     *
     * @param elevatorId 电梯id
     */
    JSONObject getElevatorInfoById(String elevatorId);

    /**
     * 获取健康雷达图
     *
     * @param elevatorId 电梯id
     */
    Map<String, Integer> getHealthRadarChart(String elevatorId);

    /**
     * 获取电梯统计信息
     *
     * @param elevatorId 电梯id
     */
    Map<String, Object> getElevatorCountInfo(String elevatorId);

    /**
     * 获取电梯运行热力图
     *
     * @param searchElevatorModule 查询参数
     */
    JSONObject getElevatorHeatMap(SearchElevatorModule searchElevatorModule);

    /**
     * 获取授权电梯
     *
     * @param userId     用户id
     * @param permission 是否授权
     */
    ResponseResult getPermissionElevators(String requestUserId, boolean admin, String userId,
                                          Integer permission, String villageId, String vProjectId);

    /**
     * 扩展电梯名称，地址信息
     *
     * @param maintenance 实体
     */
    void extendElevatorNameAndAddress(Maintenance maintenance);

    /*
     * 获取故障电梯列表
     * @param searchElevatorModule
     * @return
     */
    PageListResultEntity getFaultElevatorByPage(SearchElevatorModule searchElevatorModule);

    /**
     * 获取待维保电梯
     *
     * @param searchElevatorModule 查询参数
     */
    PageListResultEntity getUnMaintenanceElevatorByPage(SearchElevatorModule searchElevatorModule);

    /**
     * 获取用户授权电梯列表
     *
     * @param userId 用户id
     * @return 电梯列表
     */
    List<Elevator> getUserElevatorList(String userId);
}
