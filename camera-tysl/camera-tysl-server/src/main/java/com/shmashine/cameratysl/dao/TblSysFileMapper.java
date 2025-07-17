// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.dao;

import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblSysFile;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/9/7 18:28
 * @since v1.0
 */

@Mapper
public interface TblSysFileMapper {
    /**
     * 根据fileId 获取文件信息
     *
     * @param fileId ID
     * @return 故障信息
     */
    TblSysFile getById(String fileId);

    Integer insertOrUpdate(@Param("entity") TblSysFile entity);

    int insert(@NotNull TblSysFile tblSysFile);
}
