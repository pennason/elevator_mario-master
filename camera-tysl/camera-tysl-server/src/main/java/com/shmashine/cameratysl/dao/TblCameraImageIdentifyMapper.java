// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblCameraImageIdentifyEntity;


/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/10/13 15:25
 * @since v1.0
 */

@Mapper
public interface TblCameraImageIdentifyMapper {

    /**
     * 更新记录
     *
     * @param entity 记录
     * @return 影响的行数
     */
    Integer update(@Param("entity") TblCameraImageIdentifyEntity entity);
}
