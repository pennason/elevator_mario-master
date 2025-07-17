// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hikYunMou.dto.requests;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/6/9 13:33
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HikCloudProjectCreateRequestDTO implements Serializable {
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 项目文件过期时间 天数
     */
    private Integer expireDays;
    /**
     * 流量限制 -1 不限制
     */
    private Long flowLimit;
}
