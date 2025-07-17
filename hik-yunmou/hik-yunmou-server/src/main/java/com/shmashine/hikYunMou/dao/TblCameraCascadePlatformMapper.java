// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hikYunMou.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblCameraCascadePlatformEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/7/26 13:45
 * @since v1.0
 */

@Mapper
public interface TblCameraCascadePlatformMapper {
    /**
     * 新增下载记录
     *
     * @param entity 下载任务
     * @return 是否成功
     */
    Boolean insert(@Param("entity") TblCameraCascadePlatformEntity entity);
}
