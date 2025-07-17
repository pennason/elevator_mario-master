package com.shmashine.commonbigscreen.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shmashine.commonbigscreen.entity.Elevator;
import com.shmashine.commonbigscreen.entity.PageListResultEntity;
import com.shmashine.commonbigscreen.entity.SearchElevatorModule;
import com.shmashine.commonbigscreen.entity.SearchElevatorStatus;
import com.shmashine.commonbigscreen.entity.SearchFaultModule;

/**
 * 电梯服务
 *
 * @author jiangheng
 * @version V1.0.0 - 2022/3/3 16:12
 */
public interface ElevatorService extends IService<Elevator> {

    /**
     * 获取用户电梯统计
     */
    Integer getElevatorCount(SearchElevatorModule searchElevatorModule);

    /**
     * 根据小区统计电梯数
     *
     * @param searchElevatorModule 查询参数
     */
    List<HashMap<String, Integer>> getElevatorCountByVillage(SearchElevatorModule searchElevatorModule);

    /**
     * 智能监管
     *
     * @param searchElevatorModule 查询参数
     */
    List<HashMap<String, Object>> intelligentSupervision(SearchElevatorModule searchElevatorModule);

    /**
     * 获取电梯基础信息
     *
     * @param elevatorCode 电梯编号
     */
    HashMap<String, Object> getElevatorBaseInfo(String elevatorCode);

    /**
     * 获取楼宇地图
     *
     * @param userId 用户id
     */
    HashMap<String, Object> searchVillageMap(String userId);

    /**
     * 电梯热力图
     *
     * @param searchFaultModule 查询参数
     */
    JSONObject getElevatorHeatMap(SearchFaultModule searchFaultModule);

    /**
     * 获取电梯安全管理员&维保人员
     *
     * @param registerNumber 注册码
     */
    HashMap<String, String> getElevatorSafetyAdministratorAndMaintainer(String registerNumber);

    /**
     * 视频流地址
     *
     * @param elevatorId 电梯id
     */
    JSONObject getElevatorVideoUrl(String elevatorId);

    /**
     * 电梯运行状态-仪电工单数据
     *
     * @param searchElevatorStatus 查询参数
     */
    PageListResultEntity getElevatorsStatus(SearchElevatorStatus searchElevatorStatus);

    /**
     * 电梯运行状态-麦信数据
     *
     * @param searchElevatorStatus 电梯运行状态
     */
    List<Map> getElevatorsStatusWithMxData(SearchElevatorStatus searchElevatorStatus);


    /**
     * 获取电梯健康度
     *
     * @param elevatorId 电梯id
     */
    Map<String, Integer> getHealthRadarChart(String elevatorId);

    /**
     * 获取楼宇id
     *
     * @param userId 用户id
     */
    List<String> getBuildId(String userId);

    Object searchElevatorByBuildId(String buildId);
}
