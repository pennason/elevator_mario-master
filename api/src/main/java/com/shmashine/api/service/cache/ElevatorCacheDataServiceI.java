// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.service.cache;

import com.shmashine.common.model.Result;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/11/28 14:37
 * @since v1.0
 */

public interface ElevatorCacheDataServiceI {

    /**
     * 缓存总获取 运行数据
     *
     * @param elevatorCode 电梯编号
     * @return 结果
     */
    Result getRunningDataFromCache(String elevatorCode);

    /**
     * 获取最近的故障列表
     *
     * @param elevatorCode 电梯编号
     * @return 结果
     */
    Result getFaultListDataFromCache(String elevatorCode);
}
