// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblGroupLeasingStatisticsEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/3/3 14:10
 * @since v1.0
 */

@Mapper
public interface TblGroupLeasingStatisticsMapper {

    /**
     * 根据ID 获取详情
     *
     * @param id id
     * @return 详情
     */
    TblGroupLeasingStatisticsEntity getById(@Param("id") Long id);

    /**
     * 查询 群租识别记录 列表
     *
     * @param entity 查询条件
     * @return list
     */
    TblGroupLeasingStatisticsEntity getByUnique(@Param("entity") TblGroupLeasingStatisticsEntity entity);

    /**
     * 新增 群租识别 记录
     *
     * @param entity 记录
     * @return 是否成功
     */
    Boolean insert(@Param("entity") TblGroupLeasingStatisticsEntity entity);

    /**
     * 查询 群租识别记录 列表
     *
     * @param entity 查询条件
     * @return list
     */
    List<TblGroupLeasingStatisticsEntity> findByEntity(
            @Param("entity") TblGroupLeasingStatisticsEntity entity);

    /**
     * 查询 群租识别记录 列表
     *
     * @param elevatorCodes  电梯编号
     * @param statisticsType 统计类型， elevator, floor
     * @param level          群租系数等级， >= level
     * @return list
     */
    List<TblGroupLeasingStatisticsEntity> listByElevatorCodes(@Param("elevatorCodes") List<String> elevatorCodes,
                                                              @Param("statisticsType") String statisticsType,
                                                              @Param("level") Integer level);

    /**
     * 更新 群租识别记录
     *
     * @param entity 更新的内容， 需要含 id
     * @return 受影响的行数
     */
    Integer updateById(@Param("entity") TblGroupLeasingStatisticsEntity entity);

    Boolean save(@Param("entity") TblGroupLeasingStatisticsEntity entity);
}
