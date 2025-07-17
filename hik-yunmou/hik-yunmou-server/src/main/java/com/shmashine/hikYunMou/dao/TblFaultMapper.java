// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hikYunMou.dao;

import org.apache.ibatis.annotations.Mapper;

import com.shmashine.common.entity.TblFault;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/9 15:57
 * @since v1.0
 */

@Mapper
public interface TblFaultMapper {
    /**
     * 根据故障ID获取故障信息
     *
     * @param faultId 故障ID
     * @return 故障信息
     */
    TblFault getById(String faultId);
}
