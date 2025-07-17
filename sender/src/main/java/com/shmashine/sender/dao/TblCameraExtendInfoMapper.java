// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblCameraExtendInfoEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/9/12 13:30
 * @since v1.0
 */

@Mapper
public interface TblCameraExtendInfoMapper {

    TblCameraExtendInfoEntity getByElevatorCode(@Param("elevatorCode") String elevatorCode);

    TblCameraExtendInfoEntity getByCloudNumberAndPlatformId(@Param("cloudNumber") String cloudNumber,
                                                            @Param("platformId") String platformId);
}
