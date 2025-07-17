// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hkcamerabyys.dao;

import org.apache.ibatis.annotations.Mapper;

import com.shmashine.common.entity.TblFault;
import com.shmashine.hkcamerabyys.entity.UncivilizedBehavior37Fault;

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

    /**
     * 新增电动车入梯故障记录临时表
     *
     * @param fault 故障记录
     */
    int insertUncivilizedBehavior37Fault(UncivilizedBehavior37Fault fault);

    String getElevatorAddress(String elevatorCode);

}
