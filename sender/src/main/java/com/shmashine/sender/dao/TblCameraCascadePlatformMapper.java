// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblCameraCascadePlatformEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/7/26 13:45
 * @since v1.0
 */

@Mapper
public interface TblCameraCascadePlatformMapper {

    /**
     * 根据电梯编号 获取 摄像头 国标级联信息
     *
     * @param elevatorCode 电梯编号
     * @return 摄像头 国标级联信息
     */
    TblCameraCascadePlatformEntity getByElevatorCode(@Param("elevatorCode") String elevatorCode);

    /**
     * 根据云号和平台id获取摄像头国标级联信息
     *
     * @param cloudNumber 云号
     * @param platformId  平台id
     * @return 摄像头 国标级联信息
     */
    TblCameraCascadePlatformEntity getByCloudNumberAndPlatformId(@Param("cloudNumber") String cloudNumber,
                                                                 @Param("platformId") String platformId);
}
