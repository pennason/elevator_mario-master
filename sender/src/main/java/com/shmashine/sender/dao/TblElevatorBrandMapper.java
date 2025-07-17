// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblElevatorBrand;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/23 15:36
 * @since v1.0
 */

@Mapper
public interface TblElevatorBrandMapper {
    /**
     * 根据品牌ID获取品牌信息
     *
     * @param brandId 品牌ID
     * @return 品牌信息
     */
    TblElevatorBrand getByBrandId(@Param("brandId") String brandId);
}
