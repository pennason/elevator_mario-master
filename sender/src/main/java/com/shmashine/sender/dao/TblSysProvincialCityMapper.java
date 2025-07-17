// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblSysProvincialCityEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/23 11:13
 * @since v1.0
 */

@Mapper
public interface TblSysProvincialCityMapper {
    /**
     * 根据地区ID获取地区信息
     *
     * @param areaId 地区ID
     * @return 地区信息
     */
    TblSysProvincialCityEntity getByAreaId(@Param("areaId") Integer areaId);
}
