package com.shmashine.commonbigscreen.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.PathVariable;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shmashine.commonbigscreen.entity.Elevator;
import com.shmashine.commonbigscreen.entity.SearchElevatorModule;
import com.shmashine.commonbigscreen.entity.SearchElevatorStatus;

/**
 * ElevatorDao
 *
 * @author jiangheng
 * @version 1.0 - 2022/3/3 16:14
 */
public interface ElevatorDao extends BaseMapper<Elevator> {

    /**
     * 获取用户电梯统计
     */
    Integer getAllElevatorCount(SearchElevatorModule searchElevatorModule);

    /**
     * 根据小区统计电梯数
     *
     * @param searchElevatorModule 请求体
     */
    List<HashMap<String, Integer>> getElevatorCountByVillage(SearchElevatorModule searchElevatorModule);

    @Select("SELECT v_elevator_brand_name FROM tbl_elevator_brand WHERE v_elevator_brand_id = #{elevatorBrandId}")
    String getElevatorBrandById(String elevatorBrandId);

    /**
     * 获取昨日运行统计
     *
     * @param elevatorCode 电梯code
     */
    @Select("SELECT "
            + "bi_run_count AS lastRunCount,"
            + "bi_door_count AS lastDoorCount,"
            + "bi_bend_count AS lastBendCount,"
            + "bi_run_distance_count AS lastRunDistanceCount "
            + "FROM tbl_elevator_run_count WHERE v_elevator_code = #{elevatorCode} "
            + "AND dt_report_date = DATE_SUB(CURDATE( ), INTERVAL 1 DAY ) LIMIT 1")
    HashMap<String, Integer> getYesterdayRunCount(String elevatorCode);

    /**
     * 获取电梯安全管理员&维保人员
     *
     * @param registerNumber 注册码
     */
    @Select("SELECT "
            + "safety_administrator AS safetyAdministrator,"
            + "safety_admin_tel AS safetyAdminTel,"
            + "first_maintainer_name AS firstMaintainerName,"
            + "first_maintainer_tel AS firstMaintainerTel,"
            + "second_maintainer_name AS secondMaintainerName,"
            + "second_maintainer_tel AS secondMaintainerTel "
            + "FROM tbl_third_party_ruijin_maintenance WHERE register_number = #{registerNumber} LIMIT 1")
    HashMap<String, String> getElevatorSafetyAdministratorAndMaintainer(String registerNumber);

    List<HashMap<String, Object>> queryAllStatus(SearchElevatorStatus searchElevatorStatus);

    List<String> getPeopleTrappedStatus(SearchElevatorStatus searchElevatorStatus);

    List<String> getModeStatus(SearchElevatorStatus searchElevatorStatus);

    List<String> getFaultStatus(SearchElevatorStatus searchElevatorStatus);

    List<String> getIsOnLineStatus(SearchElevatorStatus searchElevatorStatus);

    /**
     * 获取每日平均运行统计
     *
     * @param searchElevatorModule 请求体
     */
    Integer getAVGRunCountByDay(SearchElevatorModule searchElevatorModule);

    /**
     * 获取楼宇id
     *
     * @param userId 用户id
     */
    List<String> getBuildIdByUserId(@PathVariable("isAdminFlag") boolean isAdminFlag,
                                    @PathVariable("userId") String userId);

    /**
     * 电梯运行状态-麦信数据
     *
     * @param searchElevatorStatus 电梯运行状态
     * @return 运行状态
     */
    List<Map> getElevatorsStatusWithMxData(SearchElevatorStatus searchElevatorStatus);


}
