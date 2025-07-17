// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblCameraDownloadTaskEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/9 16:07
 * @since v1.0
 */

@Mapper
public interface TblCameraDownloadTaskMapper {

    /**
     * 根据ID获取详情
     *
     * @param id 任务ID
     * @return 详情
     */
    TblCameraDownloadTaskEntity getById(@Param("id") Long id);

    /**
     * 获取最近7天的夜间守护记录，成功状态的
     *
     * @param elevatorCode 电梯编号
     * @param taskType     任务类型
     * @return 结果
     */
    List<TblCameraDownloadTaskEntity> findByElevatorCodeAndTaskType(@Param("elevatorCode") String elevatorCode,
                                                                    @Param("taskType") Integer taskType);

    /**
     * 根据 指定条件查询
     *
     * @param entity 查询条件
     * @return 列表
     */
    List<TblCameraDownloadTaskEntity> findByEntity(@Param("entity") TblCameraDownloadTaskEntity entity);
}
