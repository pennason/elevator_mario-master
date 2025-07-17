// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.service.elevator;

import java.util.List;

import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.elevator.ElevatorGroupLeasingHotMapModule;
import com.shmashine.api.module.elevator.GroupLeasingFloorEvidenceModule;
import com.shmashine.common.entity.TblGroupLeasingFloorEvidenceEntity;
import com.shmashine.common.entity.TblGroupLeasingStatisticsEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/3/10 14:09
 * @since v1.0
 */

public interface ElevatorGroupLeasingServiceI {
    /**
     * 根据电梯编号 获取群租楼层统计信息, 并扩展取证信息
     *
     * @param elevatorCode 电梯
     * @return 结果
     */
    List<ElevatorGroupLeasingHotMapModule> getFloorWithEvidenceByElevatorCode(String elevatorCode);

    /**
     * 日期转换  yyyyMMddHHmmss -> yyyy-MM-dd HH:mm:ss
     *
     * @param date yyyyMMddHHmmss
     * @return yyyy-MM-dd HH:mm:ss
     */
    String dateShortToNormal(String date);

    /**
     * 根据电梯编号 获取 群租取证配置信息
     *
     * @param elevatorCode 电梯编号
     * @return 结果
     */
    List<TblGroupLeasingFloorEvidenceEntity> getFloorEvidenceConfigByElevatorCode(String elevatorCode);

    /**
     * 根据电梯编号 获取可疑漏乘
     *
     * @param elevatorCode 电梯编号
     * @return 可疑漏乘
     */
    List<TblGroupLeasingStatisticsEntity> getFloorSuspiciousByElevatorCode(String elevatorCode);

    /**
     * 存储楼层取证配置信息
     *
     * @param module 配置信息
     * @return 受影响行数
     */
    ResponseResult saveFloorEvidences(GroupLeasingFloorEvidenceModule module);
}
