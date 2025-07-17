package com.shmashine.api.service.wuye;

import java.util.List;
import java.util.Map;

import com.shmashine.api.module.elevator.ElevatorScreenModule;
import com.shmashine.api.module.ruijin.FaultStatisticalQuantitySearchModule;
import com.shmashine.common.entity.TblElevator;

public interface MaiXinElevatorService {

    /**
     * 获取电梯基本信息
     */
    Map getElevatorInfo(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 获取电梯热力柱状图
     *
     * @return
     */
    Map<String, List> getElevatorHeatMapNew(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 天气预报
     *
     * @param location
     * @return
     */
    ElevatorScreenModule getWeatherInfo(String location);

    /**
     * 根据用户获取电梯列表
     *
     * @param userId
     * @return
     */
    List<TblElevator> getElevatorListByUser(String userId, boolean isAdmin);

    /**
     * 根据小区获取电梯列表
     *
     * @param userId    用户id
     * @param villageId 小区id
     * @return
     */
    List<TblElevator> getElevatorListByVillage(String userId, boolean isAdmin, String villageId);

    /**
     * 根据电梯id获取电梯列表
     *
     * @param elevatorIds
     * @return
     */
    List<TblElevator> getElevatorListByIds(String userId, boolean isAdmin, List<String> elevatorIds);
}
