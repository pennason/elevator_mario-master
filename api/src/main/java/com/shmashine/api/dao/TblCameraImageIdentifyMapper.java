// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.api.service.elevator.DO.ImageRecognitionMattingConfigDO;
import com.shmashine.common.entity.TblCameraImageIdentifyEntity;


/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/10/13 15:25
 * @since v1.0
 */

@Mapper
public interface TblCameraImageIdentifyMapper {
    /**
     * 根据ID获取详情
     *
     * @param id 任务ID
     * @return 详情
     */
    TblCameraImageIdentifyEntity getById(@Param("id") Long id);

    /**
     * 获取最近一周待识别的记录
     *
     * @param date 日期 yyyy-MM-dd
     * @return 结果
     */
    List<TblCameraImageIdentifyEntity> listRecentUnIdentify(@Param("date") String date);

    /**
     * 获取初始化状态的记录
     *
     * @return 结果
     */
    List<TblCameraImageIdentifyEntity> listInitRecordHoursAgo(@Param("date") String date);

    /**
     * 更新记录
     *
     * @param entity 记录
     * @return 影响的行数
     */
    Integer update(@Param("entity") TblCameraImageIdentifyEntity entity);

    /**
     * 更新记录 识别中
     *
     * @param ids 记录id
     * @return 影响的行数
     */
    Integer updateStatusToIdentifying(@Param("ids") List<Long> ids);

    /**
     * 保存记录
     *
     * @param entity 记录
     */
    void save(@Param("entity") TblCameraImageIdentifyEntity entity);

    /**
     * 根据自定义id获取记录
     *
     * @param customId
     * @return
     */
    TblCameraImageIdentifyEntity getByCustomId(String customId);

    /**
     * 根据故障类型获取图片抠图配置
     *
     * @param elevatorCode
     * @param faultType
     * @return
     */
    ImageRecognitionMattingConfigDO getImageMattingConfigByFaultType(@Param("elevatorCode") String elevatorCode, @Param("faultType") String faultType);

    /**
     * 更新配置实际坐标点
     *
     * @param realCoordinates
     * @param id
     */
    void updateImageMattingConfig(@Param("realCoordinates") String realCoordinates, @Param("id") Long id);
}
