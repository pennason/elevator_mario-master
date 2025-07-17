// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblCameraExtendInfoEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/24 9:13
 * @since v1.0
 */

@Mapper
public interface TblCameraExtendInfoMapper {
    /**
     * 根据电梯编号获取摄像头扩展信息
     *
     * @param elevatorCode 电梯编号
     * @return 摄像头扩展信息
     */
    TblCameraExtendInfoEntity getCameraExtendInfoByElevatorCode(@Param("elevatorCode") String elevatorCode,
                                                                @Param("cameraType") Integer cameraType);

    TblCameraExtendInfoEntity getByCloudNumberAndPlatformId(@Param("cloudNumber") String cloudNumber,
                                                            @Param("platformId") String platformId);

    /**
     * 保存摄像头扩展信息
     *
     * @param entity 摄像头扩展信息
     * @return 结果
     */
    Integer saveCameraExtendInfo(TblCameraExtendInfoEntity entity);

    /**
     * 更新摄像头扩展信息状态
     *
     * @param entity 摄像头扩展信息
     * @return 结果
     */
    Integer updateCameraStatus(@Param("entity") TblCameraExtendInfoEntity entity);

    /**
     * 根据guid更新摄像头扩展信息状态
     *
     * @param entity 摄像头扩展信息
     * @return 结果
     */
    Integer updateCameraStatusByGuid(@Param("entity") TblCameraExtendInfoEntity entity);

}
