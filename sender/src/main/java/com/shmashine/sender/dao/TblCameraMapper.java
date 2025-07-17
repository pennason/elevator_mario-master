// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.dto.TblCameraDTO;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/7/12 11:47
 * @since v1.0
 */

@Mapper
public interface TblCameraMapper {
    /**
     * 根据电梯编号查询摄像头信息
     *
     * @param elevatorCode 电梯编号
     * @return 摄像头信息
     */
    TblCameraDTO getCameraByElevatorCode(@Param("elevatorCode") String elevatorCode);
}
