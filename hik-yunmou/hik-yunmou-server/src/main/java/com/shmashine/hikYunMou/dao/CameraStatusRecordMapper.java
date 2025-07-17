// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hikYunMou.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.CameraStatusRecordEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/17 14:05
 * @since v1.0
 */

@Mapper
public interface CameraStatusRecordMapper {

    /**
     * 根据设备序列号查询最新状态
     *
     * @param serialNumber 设备序列号
     * @return 结果
     */
    CameraStatusRecordEntity getBySerialNumber(@Param("serialNumber") String serialNumber);

    /**
     * 根据消息ID查询记录
     *
     * @param messageId 消息ID
     * @return 结果
     */
    CameraStatusRecordEntity getByMessageId(@Param("messageId") String messageId);

    /**
     * 新增
     *
     * @param entity 实体
     * @return 是否成功
     */
    Boolean insert(@Param("entity") CameraStatusRecordEntity entity);

    /**
     * 按主键删除
     *
     * @param id 主键
     * @return 影响行数
     */
    Integer deleteById(@Param("id") Long id);

}
