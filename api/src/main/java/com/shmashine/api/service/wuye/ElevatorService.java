package com.shmashine.api.service.wuye;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.api.module.elevator.SearchElevatorModule;
import com.shmashine.api.module.fault.input.FaultStatisticsModule;
import com.shmashine.api.module.fault.input.SearchFaultModule;
import com.shmashine.api.module.ruijin.FaultStatisticalQuantitySearchModule;

/**
 * 获取电梯信息
 *
 * @author shmashine
 */

public interface ElevatorService {

    /**
     * 获取用户电梯统计
     */
    Integer getElevatorCount(SearchElevatorModule searchElevatorModule);

    /**
     * 根据小区统计电梯数
     */
    List<HashMap<String, Object>> getElevatorCountByVillage(SearchElevatorModule searchElevatorModule);

    /**
     * 获取电梯基础信息
     */
    HashMap<String, Object> getElevatorBaseInfo(String elevatorCode);

    /**
     * 获取楼宇地图
     */
    HashMap<String, Object> searchVillageMap(String userId);

    /**
     * 电梯热力图
     */
    JSONObject getElevatorHeatMap(SearchFaultModule searchFaultModule);

    /**
     * 获取电梯安全管理员&维保人员
     */
    HashMap<String, String> getElevatorSafetyAdministratorAndMaintainer(String registerNumber);

    /**
     * 视频流地址
     */
    JSONObject getElevatorVideoUrl(String elevatorId);


    /**
     * 获取电梯健康度
     */
    Map<String, Integer> getHealthRadarChart(String elevatorId);

    /**
     * 获取电梯基本信息
     */
    Map getElevatorInfo(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 获取楼宇id
     */
    List<String> getBuildId(String userId);


    Object searchElevatorByBuildId(String buildId);

    /**
     * 获取电梯每日运行数据统计
     */
    Map<String, Object> getElevatorRunDataStatistics(FaultStatisticsModule faultStatisticsModule);


    /**
     * 获取电梯每日运行数据统计, 电梯分组
     */
    Map<String, Object> getElevatorRunDataStatisticsGroupByElevator(FaultStatisticsModule faultStatisticsModule);
}
