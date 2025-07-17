package com.shmashine.api.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.api.module.elevator.SearchElevatorModule;
import com.shmashine.api.module.fault.input.FaultStatisticsModule;

@Mapper
public interface WuyeElevatorDao {
    /**
     * 获取用户电梯统计
     *
     * @return
     */
    Integer getAllElevatorCount(SearchElevatorModule searchElevatorModule);

    /**
     * 根据小区统计电梯数
     *
     * @param searchElevatorModule
     * @return
     */
    List<HashMap<String, Object>> getElevatorCountByVillage(SearchElevatorModule searchElevatorModule);

    /**
     * 基本电梯信息查询 大屏
     */
    Map getElevatorInfo(@Param("register_number") String register_number);

    /**
     * 电梯日运行数据统计
     *
     * @param faultStatisticsModule
     * @return
     */
    List<Map<Object, Object>> getElevatorRunDataStatistics(FaultStatisticsModule faultStatisticsModule);

    /**
     * 电梯日运行数据统计, 电梯分组
     *
     * @param faultStatisticsModule
     * @return
     */
    List<Map<Object, Object>> getElevatorRunDataStatisticsGroupByElevator(FaultStatisticsModule faultStatisticsModule);

    /**
     * 根据项目id获取电梯列表
     *
     * @param projectIds 项目id
     * @return
     */
    List<String> getElevatorIdsByProjectIds(List<String> projectIds);

    /**
     * 根据小区id获取电梯列表
     *
     * @param villageIds 项目id
     * @return
     */
    List<String> getElevatorIdsByVillageIds(List<String> villageIds);
}
