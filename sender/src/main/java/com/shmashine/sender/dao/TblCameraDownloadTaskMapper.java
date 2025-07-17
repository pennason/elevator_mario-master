// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.dao;

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
     * 根据自定义任务ID获取任务信息
     *
     * @param taskCustomId 自定义任务ID
     * @return list
     */
    List<TblCameraDownloadTaskEntity> listByTaskCustomId(@Param("taskCustomId") String taskCustomId);

    /**
     * 根据自定义任务ID获取任务信息， 只返回成功的存储到OSS的记录
     *
     * @param taskCustomId 自定义任务ID
     * @return list
     */
    List<TblCameraDownloadTaskEntity> listSuccessesByTaskCustomId(@Param("taskCustomId") String taskCustomId);
}
