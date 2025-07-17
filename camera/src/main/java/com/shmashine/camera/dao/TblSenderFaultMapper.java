// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.camera.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblSenderFaultEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/7/12 17:10
 * @since v1.0
 */

@Mapper
public interface TblSenderFaultMapper {

    /**
     * 根据ID获取需要发送的故障信息
     *
     * @param faultId 故障ID
     * @return 故障信息
     */
    TblSenderFaultEntity getByFaultId(@Param("faultId") String faultId);

    /**
     * 根据ID更新故障信息
     *
     * @param entity 故障信息
     * @return 更新结果
     */
    Integer update(@Param("entity") TblSenderFaultEntity entity);

    /**
     * 插入故障信息
     *
     * @param entity 故障信息
     * @return 插入结果
     */
    Integer insert(@Param("entity") TblSenderFaultEntity entity);
}
