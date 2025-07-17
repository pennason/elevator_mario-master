// Copyright (C) 2024 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.dao;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblSysProvincialCityEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/1/24 18:32
 * @since v1.0
 */

public interface TblSysProvincialCityMapper {

    TblSysProvincialCityEntity getByNameAndLevel(@Param("areaName") String areaName, @Param("level") Integer level, @Param("cityCode") String cityCode);

    TblSysProvincialCityEntity getProvinceByAreaCode(@Param("areaCode") String areaCode);
}
