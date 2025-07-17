// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.ImageRecognitionMattingConfigEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/9 14:41
 * @since v1.0
 */

@Mapper
public interface TblElevatorMapper {
    /**
     * 是否下载视频  电梯状态未安装不下载
     *
     * @param elevatorCode 电梯编号
     * @return bool
     */
    Boolean checkCanDownMedia(@Param("elevatorCode") String elevatorCode);

    /**
     * 根据故障类型获取图片抠图配置
     *
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     * @return 配置
     */
    ImageRecognitionMattingConfigEntity getImageMattingConfigByFaultType(@Param("elevatorCode") String elevatorCode,
                                                                         @Param("faultType") String faultType);

    void updateImageMattingConfig(@Param("realCoordinates") String realCoordinates, @Param("id") Long id);

}
