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
public class HikCloudCascadeChannelDTO implements Serializable {
    /**
     * 通道ID
     */
    private String channelId;
    /**
     * 云平台序列号
     */
    private String deviceSerial;
    /**
     * 通道号
     */
    private Integer channelNo;
    /**
     * 通道级联编码
     */
    private String chanelCode;
}
