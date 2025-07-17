// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.service.elevator;

import com.shmashine.common.dto.ElevatorCacheDTO;
import com.shmashine.common.entity.TblElevator;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/7 15:47
 * @since v1.0
 */

public interface ElevatorCacheService {

    /**
     * 根据电梯编号获取 电梯的缓存，含夜间守护，小区群租信息
     *
     * @param elevatorCode 电梯编号
     * @return 缓存
     */
    ElevatorCacheDTO getByElevatorCode(String elevatorCode);

    /**
     * 根据电梯编号 从数据库中获取数据并加入缓存
     *
     * @param elevatorCode 电梯编号
     * @return 缓存
     */
    ElevatorCacheDTO updateCacheByElevatorCodeFromDatabase(String elevatorCode);

    /**
     * 根据小区ID 更新电梯缓存信息
     *
     * @param villageId 小区ID
     * @return 受影响行数
     */
    Integer updateCacheByVillageIdFromDatabase(String villageId);

    /**
     * 批量更新电梯缓存信息
     *
     * @param tblElevator 电梯信息
     * @return 受影响行数
     */
    Integer updateCacheByEntityFromDatabase(TblElevator tblElevator);
}
