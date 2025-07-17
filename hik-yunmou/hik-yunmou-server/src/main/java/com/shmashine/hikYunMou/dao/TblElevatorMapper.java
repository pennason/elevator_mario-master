// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hikYunMou.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

}
