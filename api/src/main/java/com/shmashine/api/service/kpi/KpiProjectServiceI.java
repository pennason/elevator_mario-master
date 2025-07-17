// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.service.kpi;

import java.util.List;
import java.util.Map;

import com.shmashine.api.entity.KpiProjectElevatorCountEntity;
import com.shmashine.common.dto.KpiProjectIotDTO;
import com.shmashine.common.dto.KpiProjectIotNotifyDTO;
import com.shmashine.common.dto.KpiProjectIotStatisticsDTO;
import com.shmashine.common.dto.KpiProjectNorthPushDTO;
import com.shmashine.common.dto.KpiProjectNorthPushNotifyDTO;
import com.shmashine.common.dto.KpiProjectNorthPushStatisticsDTO;
import com.shmashine.common.dto.KpiProjectStatisticsDTO;
import com.shmashine.common.entity.KpiProjectIotEntity;
import com.shmashine.common.entity.KpiProjectNorthPushEntity;
import com.shmashine.common.entity.TblSysUserWecomEntity;

/**
 * 项目KPI与北向推送KPI
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/12 10:17
 * @since v1.0
 */

public interface KpiProjectServiceI {

    // 逻辑查询

    // 项目KPI

    /**
     * 获取实时的项目KPI信息
     *
     * @return 项目KPI信息
     */
    List<KpiProjectIotEntity> listKpiProjectIotEntity();

    /**
     * 按日期获取当日的项目KPI记录, key 为项目ID
     *
     * @param day 日期 yyyy-MM-dd
     * @return 项目KPI记录
     */
    Map<String, KpiProjectIotEntity> mapKpiProjectIotEntityByDate(String day);

    /**
     * 保存项目KPI记录
     *
     * @param kpiProjectIotEntity 项目KPI记录
     */
    void saveKpiProjectIotEntity(KpiProjectIotEntity kpiProjectIotEntity);

    /**
     * 构建项目KPI通知信息
     *
     * @param kpiProjectIotEntity 项目KPI记录
     * @return 通知信息
     */
    KpiProjectIotNotifyDTO buildProjectNotifyInfo(KpiProjectIotEntity kpiProjectIotEntity);

    /**
     * 根据项目ID获取，日，周，月统计信息， 项目KPI, 建议使用 listStatisticsByProjectIdsForIot
     *
     * @param projectIds 项目ID列表
     * @return 项目KPI统计信息
     */
    @Deprecated
    List<KpiProjectIotStatisticsDTO> listIotStatisticsByProjectIds(List<String> projectIds);

    /**
     * 根据项目ID获取，日，周，月统计信息， 项目KPI
     *
     * @param projectIds 项目ID列表
     * @return 项目KPI统计信息
     */
    List<KpiProjectStatisticsDTO> listStatisticsByProjectIdsForIot(List<String> projectIds);

    /**
     * 获取项目KPI指定某天的统计信息
     *
     * @param projectIds 项目ID列表
     * @param day        日期 yyyy-MM-dd
     * @return 项目KPI记录
     */
    List<KpiProjectIotEntity> listKpiProjectIotEntityByProjectIdsAndDay(List<String> projectIds, String day);

    /**
     * 获取项目KPI本周的统计信息
     *
     * @param projectIds 项目ID列表
     * @return 项目KPI统计-本周
     */
    List<KpiProjectIotDTO> listKpiProjectIotEntityByWeek(List<String> projectIds);

    /**
     * 获取项目KPI上周统计信息
     *
     * @param projectIds 项目ID列表
     * @return 项目KPI统计-上周
     */
    List<KpiProjectIotDTO> listKpiProjectIotEntityByLastWeek(List<String> projectIds);

    /**
     * 获取项目KPI本月的统计信息
     *
     * @param projectIds 项目ID列表
     * @return 项目KPI统计-本月
     */
    List<KpiProjectIotDTO> listKpiProjectIotEntityByMonth(List<String> projectIds);

    /**
     * 获取项目KPI上月的统计信息
     *
     * @param projectIds 项目ID列表
     * @return 项目KPI统计-上月
     */
    List<KpiProjectIotDTO> listKpiProjectIotEntityByLastMonth(List<String> projectIds);

    /**
     * 获取项目KPI最新x天的统计信息
     *
     * @param projectIds 项目ID列表
     * @param days       天数
     * @return 项目KPI统计-最新x天
     */
    List<KpiProjectIotDTO> listKpiProjectIotEntityByDay(List<String> projectIds, Integer days);

    /**
     * 获取项目KPI指定项目和日期范围的明细信息
     *
     * @param projectIds 项目ID列表
     * @param startDate  开始日期 yyyy-MM-dd
     * @param endDate    结束日期 yyyy-MM-dd
     * @return 项目KPI明细信息
     */
    List<KpiProjectIotEntity> listKpiProjectIotDetailByProjectIdsAndDateRange(List<String> projectIds,
                                                                              String startDate, String endDate);

    // 北向推送项目KPI

    /**
     * 获取实时北向推送项目KPI信息
     *
     * @return 列表
     */
    List<KpiProjectNorthPushEntity> listKpiProjectNorthPushEntity();

    /**
     * 保存北向推送项目KPI信息
     *
     * @param kpiProjectNorthPushEntity 项目KPI信息
     */
    void saveKpiProjectNorthPushEntity(KpiProjectNorthPushEntity kpiProjectNorthPushEntity);

    /**
     * 构建北向推送项目KPI通知信息
     *
     * @param kpiProjectNorthPushEntity 项目KPI信息
     * @return 北向推送通知信息
     */
    KpiProjectNorthPushNotifyDTO buildProjectNorthPushNotifyInfo(KpiProjectNorthPushEntity kpiProjectNorthPushEntity);


    /**
     * 根据项目ID获取，日，周，月统计信息 北向推送, 建议使用 listStatisticsByProjectIdsForNorthPush
     *
     * @param projectIds 项目ID列表
     * @return 北向推送KPI统计信息
     */
    @Deprecated
    List<KpiProjectNorthPushStatisticsDTO> listNorthPushStatisticsByProjectIds(List<String> projectIds);


    /**
     * 根据项目ID获取，日，周，月统计信息 北向推送
     *
     * @param projectIds 项目ID列表
     * @return 北向推送KPI统计信息
     */
    List<KpiProjectStatisticsDTO> listStatisticsByProjectIdsForNorthPush(List<String> projectIds);

    /**
     * 获取北向推送项目KPI指定某天的统计信息
     *
     * @param projectIds 项目ID列表
     * @param day        日期 yyyy-MM-dd
     * @return 北向推送KPI记录
     */
    List<KpiProjectNorthPushEntity> listKpiProjectNorthPushEntityByProjectIdsAndDay(List<String> projectIds, String day);

    /**
     * 获取北向推送项目KPI本周的统计信息
     *
     * @param projectIds 项目ID列表
     * @return 北向推送KPI统计-本周
     */
    List<KpiProjectNorthPushDTO> listKpiProjectNorthPushEntityByWeek(List<String> projectIds);

    /**
     * 获取北向推送项目KPI上周统计信息
     *
     * @param projectIds 项目ID列表
     * @return 北向推送KPI统计-上周
     */
    List<KpiProjectNorthPushDTO> listKpiProjectNorthPushEntityByLastWeek(List<String> projectIds);

    /**
     * 获取北向推送项目KPI本月的统计信息
     *
     * @param projectIds 项目ID列表
     * @return 北向推送KPI统计-本月
     */
    List<KpiProjectNorthPushDTO> listKpiProjectNorthPushEntityByMonth(List<String> projectIds);

    /**
     * 获取北向推送项目KPI上月的统计信息
     *
     * @param projectIds 项目ID列表
     * @return 北向推送KPI统计-上月
     */
    List<KpiProjectNorthPushDTO> listKpiProjectNorthPushEntityByLastMonth(List<String> projectIds);

    /**
     * 获取北向推送项目KPI最新x天的统计信息
     *
     * @param projectIds 项目ID列表
     * @param days       天数
     * @return 北向推送KPI统计-最新x天
     */
    List<KpiProjectNorthPushDTO> listKpiProjectNorthPushEntityByDay(List<String> projectIds, int days);

    /**
     * 获取项目KPI指定项目和日期范围的明细信息
     *
     * @param projectIds 项目ID列表
     * @param startDate  开始日期 yyyy-MM-dd
     * @param endDate    结束日期 yyyy-MM-dd
     * @return 项目KPI明细信息
     */
    List<KpiProjectNorthPushEntity> listKpiProjectNorthPushDetailByProjectIdsAndDateRange(List<String> projectIds,
                                                                                          String startDate,
                                                                                          String endDate);


    // 单步查询

    /**
     * 按项目统计电梯安装总数
     */
    List<KpiProjectElevatorCountEntity> countInstalledElevatorGroupByProject();

    /**
     * 按项目统计电梯离线总数
     */
    List<KpiProjectElevatorCountEntity> countOfflineElevatorGroupByProject();

    /**
     * 按项目统计电梯故障总数
     */
    List<KpiProjectElevatorCountEntity> countFaultElevatorGroupByProject();

    /**
     * 按项目统计摄像头总数
     */
    List<KpiProjectElevatorCountEntity> countCameraGroupByProject();

    /**
     * 按项目统计摄像头离线总数
     */
    List<KpiProjectElevatorCountEntity> countCameraOfflineGroupByProject();

    /**
     * 按项目和日期获取对应的项目KPI记录
     *
     * @param projectId 项目ID
     * @param day       日期 yyyy-MM-dd
     * @return 项目KPI记录
     */
    KpiProjectIotEntity getKpiProjectIotEntityByProjectIdAndDate(String projectId, String day);

    /**
     * 按项目和日期获取对应的北向推送项目KPI记录
     *
     * @param projectId 项目ID
     * @param day       日期 yyyy-MM-dd
     * @return 项目KPI记录
     */
    KpiProjectNorthPushEntity getKpiProjectNorthPushEntityByProjectIdAndDate(String projectId, String day);

    // 用户相关

    /**
     * 根据项目ID获取项目的企业微信用户信息
     *
     * @param projectId 项目ID
     * @return 企业微信用户信息
     */
    List<TblSysUserWecomEntity> listWecomUserByProjectId(String projectId);

    /**
     * 获取开发人员的企业微信用户信息
     *
     * @return 企业微信用户信息
     */
    List<TblSysUserWecomEntity> listWecomUserAdmin();

    /**
     * 发送通知给企业微信用户
     *
     * @param wecomUsers 企业微信用户信息
     * @param notifyDTO  通知信息
     */
    void notifyWecomUsers(List<TblSysUserWecomEntity> wecomUsers, KpiProjectIotNotifyDTO notifyDTO);

    /**
     * 发送通知给企业微信用户
     *
     * @param wecomUsers 企业微信用户信息
     * @param notifyDTO  通知信息
     */
    void notifyWecomUsers(List<TblSysUserWecomEntity> wecomUsers, KpiProjectNorthPushNotifyDTO notifyDTO);
}
