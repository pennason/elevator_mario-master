package com.shmashine.userclientapplets.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shmashine.userclientapplets.entity.Elevator;
import com.shmashine.userclientapplets.entity.SearchElevatorModule;

/**
 * ElevatorDao
 *
 * @author jiangheng
 * @version V1.0.0 - 2022/2/8 14:38
 */
public interface ElevatorDao extends BaseMapper<Elevator> {

    /**
     * 获取用户电梯
     *
     * @param searchFaultModule 查询参数
     */
    List<Elevator> queryElevatorList(SearchElevatorModule searchFaultModule);

    /**
     * 获取电梯列表
     *
     * @param searchElevatorModule 查询参数
     */
    List<Elevator> queryElevatorAndCollectList(
            @Param("searchElevatorModule") SearchElevatorModule searchElevatorModule);

    /**
     * 获取每日平均运行统计
     *
     * @param searchElevatorModule 查询参数
     */
    Integer getAVGRunCountByDay(SearchElevatorModule searchElevatorModule);

    /**
     * 获取电梯设备列表
     *
     * @param elevatorId 电梯id
     */
    @Select("SELECT v_sensor_type,i_online_status,v_hw_version,v_sw_version,eType FROM tbl_device "
            + "WHERE v_elevator_id = #{elevatorId}")
    List<JSONObject> getDeviceByEleId(String elevatorId);

    /**
     * 获取昨日运行统计信息
     *
     * @param elevatorCode 电梯编号
     */
    HashMap<String, Object> getYesterdayRunCount(String elevatorCode);

    /**
     * 获取授权电梯
     */
    List<HashMap<String, Object>> getPermissionElevators(@Param("requestUserId") String requestUserId,
                                                         @Param("isAdmin") boolean isAdmin,
                                                         @Param("userId") String userId,
                                                         @Param("permission") Integer permission,
                                                         @Param("villageId") String villageId,
                                                         @Param("vProjectId") String vProjectId);

    /**
     * 获取故障电梯列表
     *
     * @param searchElevatorModule 查询参数
     */
    List<Elevator> getFaultElevatorByPage(@Param("searchElevatorModule") SearchElevatorModule searchElevatorModule);

    /**
     * 获取待维保电梯
     *
     * @param searchElevatorModule 查询参数
     */
    List<HashMap<String, Object>> getUnMaintenanceElevatorByPage(
            @Param("searchElevatorModule") SearchElevatorModule searchElevatorModule);

    /**
     * 获取用户授权电梯列表
     *
     * @param userId 用户id
     * @return 电梯列表
     */
    List<Elevator> getUserElevatorList(String userId);
}
