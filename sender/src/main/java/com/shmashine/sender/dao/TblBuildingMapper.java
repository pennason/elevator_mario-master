// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblBuilding;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/7/6 18:06
 * @since v1.0
 */

@Mapper
public interface TblBuildingMapper {
    /**
     * 根据小区id和楼栋id获取楼栋信息
     *
     * @param villageId  小区id
     * @param buildingId 楼栋id
     * @return 楼栋信息
     */
    TblBuilding getOneByVillageAndBuildingId(@Param("villageId") String villageId,
                                             @Param("buildingId") String buildingId);
}
