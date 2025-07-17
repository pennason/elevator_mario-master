// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/17 13:51
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CameraStatusRecordEntity implements Serializable {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 消息唯一ID
     */
    private String messageId;
    /**
     * 设备序列号
     */
    private String serialNumber;
    /**
     * 发生时间
     */
    private String occurTime;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 状态 1-在线 0-离线
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;

}
