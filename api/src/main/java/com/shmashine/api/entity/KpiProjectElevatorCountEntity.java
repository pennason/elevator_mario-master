// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.entity;

import lombok.Data;

/**
 * 按项目统计的各个状态电梯的数量
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/12 10:44
 * @since v1.0
 */

@Data
public class KpiProjectElevatorCountEntity {
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 符合条件的个数， 统计项目对应数量时使用
     */
    private Integer total;
}
