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
public class ProjectNameEntity {
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 项目名称, 获取项目名称时使用
     */
    private String projectName;
}
