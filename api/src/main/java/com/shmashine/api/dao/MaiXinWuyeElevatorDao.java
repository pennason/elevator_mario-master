package com.shmashine.api.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.module.ruijin.FaultStatisticalQuantitySearchModule;
import com.shmashine.common.entity.TblElevator;

public interface MaiXinWuyeElevatorDao {

    /**
     * 基本电梯信息查询 大屏
     */
    Map getElevatorInfo(@Param("register_number") String register_number);

    /**
     * 电梯热力图
     *
     * @param faultStatisticalQuantitySearchModule
     * @return
     */
    List<HashMap<String, Object>> getElevatorHeatMapNew(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);

    /**
     * 获取用户电梯列表
     *
     * @param userId
     * @return
     */
    List<TblElevator> getElevatorListByUser(@Param("userId") String userId, @Param("isAdmin") boolean isAdmin);

    /**
     * 根据小区获取电梯列表
     *
     * @param userId    用户id
     * @param villageId 小区id
     * @return
     */
    List<TblElevator> getElevatorListByVillage(@Param("userId") String userId, @Param("isAdmin") boolean isAdmin, @Param("villageId") String villageId);

    /**
     * 根据电梯id获取电梯列表
     */
    List<TblElevator> searchByElevatorIds(@Param("userId") String userId, @Param("isAdmin") boolean isAdmin,
                                          @Param("elevatorIds") List<String> elevatorIds);

}
