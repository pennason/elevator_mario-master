// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hikYunMou.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 国标级联 上级国标级联
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/7/26 10:47
 * @since v1.0
 */

@Data
@ToString
public class HikCloudCascadeConfigDTO implements Serializable {
    /**
     * 上级平台id
     */
    private String configId;
    /**
     * 上级平台名称
     */
    private String supPlatformName;
}
