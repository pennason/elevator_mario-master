package com.shmashine.commonbigscreen.dao;

import java.util.HashMap;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shmashine.commonbigscreen.entity.Fault;
import com.shmashine.commonbigscreen.entity.SearchElevatorModule;
import com.shmashine.commonbigscreen.entity.SearchFaultModule;

/**
 * 故障DO
 *
 * @author jiangheng
 * @version 1.0 - 2022/3/7 11:14
 */
public interface FaultDao extends BaseMapper<Fault> {

    /**
     * 获取平台故障统计
     *
     * @param searchFaultModule 查询参数
     */
    Integer getFaultCount(SearchFaultModule searchFaultModule);

    /**
     * 根据小区统计电瓶车入梯
     *
     * @param searchFaultModule 查询参数
     */
    List<HashMap<String, Object>> getElectroMobileFaultCount(SearchFaultModule searchFaultModule);

    /**
     * 根据部门获取所有小区信息——根据电梯数排序
     *
     * @param deptIds 部门id
     */
    List<HashMap<String, Object>> getVillageInfoByDeptIdsAndElevatorCount(List<String> deptIds);

    /**
     * 根据部门获取所有小区信息——根据当日困人数排序
     *
     * @param deptIds 部门id
     */
    List<HashMap<String, Object>> getVillageInfoByDeptIdsAndPeopleTrappedCount(List<String> deptIds);

    /**
     * 根据时间获取故障柱状图
     *
     * @param searchFaultModule 查询参数
     */
    List<HashMap<String, Object>> getFaultChartByTime(SearchFaultModule searchFaultModule);

    /**
     * 获取当日困人电梯
     *
     * @param searchElevatorModule 查询参数
     */
    HashMap<String, Object> getTodayPeopleTrappedElevator(SearchElevatorModule searchElevatorModule);

    /**
     * 获取当日故障电梯
     *
     * @param searchElevatorModule 查询参数
     */
    HashMap<String, Object> getTodayFaultElevator(SearchElevatorModule searchElevatorModule);


    /**
     * 获取困人总条数
     *
     * @param searchFaultModule 查询参数
     */
    Integer getTrappedPeopleTotal(SearchFaultModule searchFaultModule);

    /**
     * 救援时长统计麦信平台故障时长
     *
     * @param searchFaultModule 查询参数
     */
    Integer getTrappedPeopleTimeForMX(SearchFaultModule searchFaultModule);

    /**
     * 获取取证文件
     *
     * @param faultId 故障id
     */
    List<HashMap<String, Object>> getFaultFileById(String faultId);
}
