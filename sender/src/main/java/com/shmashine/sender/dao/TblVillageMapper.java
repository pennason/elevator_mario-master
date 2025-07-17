// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblVillage;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/7/6 18:13
 * @since v1.0
 */

@Mapper
public interface TblVillageMapper {
    /**
     * 根据小区id获取小区信息
     *
     * @param villageId 小区id
     * @return 小区信息
     */
    TblVillage getOneById(@Param("villageId") String villageId);
}
