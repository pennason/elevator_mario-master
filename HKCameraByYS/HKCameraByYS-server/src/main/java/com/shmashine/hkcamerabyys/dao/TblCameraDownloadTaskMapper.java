// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hkcamerabyys.dao;

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
     * 新增下载记录
     *
     * @param entity 下载任务
     * @return 是否成功
     */
    Boolean insert(@Param("entity") TblCameraDownloadTaskEntity entity);

    /**
     * 根据ID更新字段
     *
     * @param id     主键ID
     * @param entity 变更的字段
     * @return 影响的行数
     */
    Integer updateById(@Param("id") Long id, @Param("entity") TblCameraDownloadTaskEntity entity);

    /**
     * 根据唯一组件更新字段
     *
     * @param entity 变更的字段
     * @return 受影响的行数
     */
    Integer updateByUnique(@Param("entity") TblCameraDownloadTaskEntity entity);

    Integer updateFileStatusById(@Param("id") Long id, @Param("fileStatus") Integer fileStatus);

    /**
     * 自增 RequestFailedCount 字段
     *
     * @param id 主键ID
     * @return 受影响的行数
     */
    Integer increaseRequestFailedCount(@Param("id") Long id);

    /**
     * 自增 UploadFailedCount 字段
     *
     * @param id 主键ID
     * @return 受影响的行数
     */
    Integer increaseUploadFailedCount(@Param("id") Long id);

    /**
     * 获取 file_status 状态
     *
     * @param id 主键ID
     * @return file_status 状态
     */
    Integer getFileStatusById(@Param("id") Long id);

    /**
     * 根据任务状态和失败次数获取任务列表
     *
     * @param fileStatusList 执行状态
     * @return 列表
     */
    List<TblCameraDownloadTaskEntity> getByFileStatusAndFailedCount(
            @Param("fileStatusList") List<Integer> fileStatusList, @Param("start") int start, @Param("end") int end);

    /**
     * 根据任务状态获取任务列表
     *
     * @param fileStatusList 执行状态
     * @return 列表
     */
    List<TblCameraDownloadTaskEntity> getByFileStatus(@Param("fileStatusList") List<Integer> fileStatusList);

    /**
     * 获取夜间守护模式过期的记录
     *
     * @param taskType   任务类型，如守夜模式
     * @param expireDate 过期时间， 获取在这之前的记录
     * @return 记录
     */
    List<TblCameraDownloadTaskEntity> listTaskTypeExpiredRecords(@Param("taskType") Integer taskType,
                                                                 @Param("expireDate") String expireDate);

    /**
     * 获取下载记录列表
     *
     * @param entity 查询条件
     * @return 记录
     */
    List<TblCameraDownloadTaskEntity> findByEntity(@Param("entity") TblCameraDownloadTaskEntity entity);

    /**
     * 根据ID获取详情
     *
     * @param id 任务ID
     * @return 详情
     */
    TblCameraDownloadTaskEntity getById(@Param("id") Long id);

    /**
     * 根据ID 删除相关记录
     *
     * @param id 任务ID
     * @return 影响的行数
     */
    Integer deleteById(@Param("id") Long id);

    /**
     * 根据taskID获取任务记录
     *
     * @param taskId 任务id
     */
    TblCameraDownloadTaskEntity getByByTaskId(String taskId);
}
