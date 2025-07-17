// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblSysFile;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/7/13 10:00
 * @since v1.0
 */

@Mapper
public interface TblSysFileMapper {

    /**
     * 根据故障ID获取文件列表
     *
     * @param faultId 故障ID
     * @return 文件列表
     */
    List<TblSysFile> getFileListByFaultId(@Param("faultId") String faultId);
}
