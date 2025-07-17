// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblGroupLeasingElevatorCoefficientEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/17 13:55
 * @since v1.0
 */

@Mapper
public interface TblGroupLeasingElevatorCoefficientMapper {

    /**
     * 根据ID 获取详情
     *
     * @param id id
     * @return 详情
     */
    TblGroupLeasingElevatorCoefficientEntity getById(@Param("id") Long id);

    /**
     * 新增 群租识别 记录
     *
     * @param entity 记录
     * @return 是否成功
     */
    Boolean insert(@Param("entity") TblGroupLeasingElevatorCoefficientEntity entity);

    /**
     * 查询 群租识别记录 列表
     *
     * @param entity 查询条件
     * @return list
     */
    List<TblGroupLeasingElevatorCoefficientEntity> findByEntity(
            @Param("entity") TblGroupLeasingElevatorCoefficientEntity entity);

    /**
     * 查询 最近30天的 群租识别记录 列表
     *
     * @param entity 查询条件
     * @return list
     */
    List<TblGroupLeasingElevatorCoefficientEntity> findByEntityLatestMonth(
            @Param("entity") TblGroupLeasingElevatorCoefficientEntity entity);

    /**
     * 更新 群租识别记录
     *
     * @param entity 更新的内容， 需要含 id
     * @return 受影响的行数
     */
    Integer updateById(@Param("entity") TblGroupLeasingElevatorCoefficientEntity entity);
}
