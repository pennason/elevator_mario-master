// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hikYunMou.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.dto.TblCameraDTO;
import com.shmashine.common.entity.CameraStatusRecordEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/17 14:48
 * @since v1.0
 */

@Mapper
public interface TblCameraMapper {
    /**
     * 修改摄像头当前状态
     *
     * @param entity 摄像头状态
     * @return 结果
     */
    Integer updateCameraStatus(@Param("entity") CameraStatusRecordEntity entity);

    /**
     * 根据电梯编号 获取摄像头信息
     *
     * @param elevatorCode 电梯编号
     * @return 摄像头信息
     */
    TblCameraDTO getCameraInfoByElevatorCode(@Param("elevatorCode") String elevatorCode);

    /**
     * 根据摄像头类型 获取摄像头信息
     *
     * @param cameraType 摄像头类型
     * @return 摄像头信息
     */
    List<TblCameraDTO> listCamerasByCameraType(@Param("cameraType") Integer cameraType);
}
