// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.dto.KpiProjectNorthPushDTO;
import com.shmashine.common.entity.KpiProjectNorthPushEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/12 11:49
 * @since v1.0
 */

public interface KpiProjectNorthPushMapper {
    /**
     * 按项目统计电梯安装总数
     *
     * @param projectId 项目ID
     * @param day       日期 yyyy-MM-dd
     * @return 结果
     */
    KpiProjectNorthPushEntity getKpiProjectNorthPushEntityByProjectIdAndDate(@Param("projectId") String projectId,
                                                                             @Param("day") String day);

    /**
     * 按项目统计电梯安装总数历史记录
     *
     * @param projectId 项目ID
     * @return 列表
     */
    List<KpiProjectNorthPushEntity> listKpiProjectNorthPushEntityByProjectId(@Param("projectId") String projectId);

    /**
     * 按日期统计电梯安装总数
     *
     * @param day 日期 yyyy-MM-dd
     * @return 列表
     */
    List<KpiProjectNorthPushEntity> listKpiProjectNorthPushEntityByDate(@Param("day") String day);


    /**
     * 获取指定日期和项目的统计信息
     *
     * @param projectIds 项目IDs
     * @param day        日期 yyyy-MM-dd
     * @return 结果
     */
    List<KpiProjectNorthPushEntity> listKpiProjectNorthPushEntityByProjectIdsAndDay(
            @Param("projectIds") List<String> projectIds, @Param("day") String day);

    /**
     * 按项目和日期范围统计电梯的在线，告警，摄像头状态，按项目分组
     *
     * @param projectIds 项目IDs
     * @param startDate  开始日期 yyyy-MM-dd
     * @param endDate    结束日期 yyyy-MM-dd
     * @return 结果
     */
    List<KpiProjectNorthPushDTO> getKpiProjectNorthPushByDateRange(@Param("projectIds") List<String> projectIds,
                                                                   @Param("startDate") String startDate,
                                                                   @Param("endDate") String endDate);


    /**
     * 按项目和日期范围获取电梯的在线，告警，摄像头状态明细
     *
     * @param projectIds 项目IDs
     * @param startDate  开始日期 yyyy-MM-dd
     * @param endDate    结束日期 yyyy-MM-dd
     * @return 结果
     */
    List<KpiProjectNorthPushEntity> listKpiProjectNorthPushDetailByDateRange(@Param("projectIds") List<String> projectIds,
                                                                             @Param("startDate") String startDate,
                                                                             @Param("endDate") String endDate);

    /**
     * 新增项目日期统计信息
     *
     * @param entity 信息
     * @return 是否成功
     */
    Boolean insert(@Param("entity") KpiProjectNorthPushEntity entity);

    /**
     * 按主键删除
     *
     * @param id 主键
     * @return 影响行数
     */
    Integer deleteById(@Param("id") Long id);

    /**
     * 按主键更新
     *
     * @param entity 信息
     * @return 影响行数
     */
    Integer updateById(@Param("entity") KpiProjectNorthPushEntity entity);
}
