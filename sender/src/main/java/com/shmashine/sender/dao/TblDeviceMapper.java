// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblDevice;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/23 15:28
 * @since v1.0
 */

@Mapper
public interface TblDeviceMapper {
    /**
     * 根据电梯编号获取设备信息，只获取一条
     *
     * @param elevatorCode 电梯编号
     * @return 电梯信息
     */
    TblDevice getOneByElevatorCode(@Param("elevatorCode") String elevatorCode);
}
