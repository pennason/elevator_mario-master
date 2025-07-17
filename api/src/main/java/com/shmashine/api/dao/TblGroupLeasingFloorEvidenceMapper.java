// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblGroupLeasingFloorEvidenceEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/3/13 13:55
 * @since v1.0
 */

@Mapper
public interface TblGroupLeasingFloorEvidenceMapper {

    /**
     * 根据ID 获取详情
     *
     * @param id id
     * @return 详情
     */
    TblGroupLeasingFloorEvidenceEntity getById(@Param("id") Long id);

    /**
     * 新增 群租取证 记录
     *
     * @param entity 记录
     * @return 是否成功
     */
    Boolean insert(@Param("entity") TblGroupLeasingFloorEvidenceEntity entity);

    /**
     * 查询 群租取证 列表
     *
     * @param entity 查询条件
     * @return list
     */
    List<TblGroupLeasingFloorEvidenceEntity> findByEntity(@Param("entity") TblGroupLeasingFloorEvidenceEntity entity);

    /**
     * 查询 群租取证 列表
     *
     * @param elevatorCode 电梯编号
     * @param floor        楼层
     * @return info
     */
    TblGroupLeasingFloorEvidenceEntity getByUnique(@Param("elevatorCode") String elevatorCode,
                                                   @Param("floor") String floor);

    /**
     * 更新 群租识别记录
     *
     * @param entity 更新的内容， 需要含 id
     * @return 受影响的行数
     */
    Integer updateById(@Param("entity") TblGroupLeasingFloorEvidenceEntity entity);

    /**
     * 存储相关记录， 如果冲突就更新
     *
     * @param entity 实体
     * @return 结果
     */
    Boolean save(@Param("entity") TblGroupLeasingFloorEvidenceEntity entity);
}
