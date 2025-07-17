// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hikYunMou.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblCameraHikCloudProjectEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/6/9 11:05
 * @since v1.0
 */

@Mapper
public interface TblCameraHikCloudProjectMapper {
    /**
     * 根据项目编号获取项目信息
     *
     * @param projectCode 项目编号
     * @return 项目信息
     */
    TblCameraHikCloudProjectEntity getByProjectCode(@Param("projectCode") String projectCode);

    /**
     * 根据指定信息查询
     *
     * @param entity 查询条件
     * @return 项目信息
     */
    TblCameraHikCloudProjectEntity findByEntity(@Param("entity") TblCameraHikCloudProjectEntity entity);

    /**
     * 新增
     *
     * @param entity 项目信息
     * @return 是否成功
     */
    Boolean insert(@Param("entity") TblCameraHikCloudProjectEntity entity);

    /**
     * 根据ID删除记录
     *
     * @param id 主键ID
     * @return 是否成功
     */
    Integer deleteById(@Param("id") Long id);

    /**
     * 根据主键更新记录
     *
     * @param id     主键ID
     * @param entity 变更的字段
     * @return 受影响的行数
     */
    Integer updateById(@Param("id") Long id, @Param("entity") TblCameraHikCloudProjectEntity entity);

    /**
     * 根据唯一组件更新记录
     *
     * @param entity 变更的字段
     * @return 受影响的行数
     */
    Integer updateByUnique(@Param("entity") TblCameraHikCloudProjectEntity entity);
}
