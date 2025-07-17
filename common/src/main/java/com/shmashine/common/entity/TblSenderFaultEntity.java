// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/7/12 16:13
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TblSenderFaultEntity implements Serializable {
    /**
     * 自增ID
     */
    private Long id;
    /**
     * 告警ID
     */
    private String faultId;
    /**
     * 电梯编号
     */
    private String elevatorCode;
    /**
     * 需要推送的平台
     */
    private String pushGovern;
    /**
     * 是否困人， 0：不是，1：是困人
     */
    private Integer entrap;
    /**
     * 原告警消息 FaultMessage的信息
     */
    private String faultMessage;
    /**
     * 是否需要取证照片，1：需要，0：不需要
     */
    private Integer needPhoto;
    /**
     * 是否需要取证视频，1：需要，0：不需要
     */
    private Integer needVideo;
    /**
     * 照片地址
     */
    private String urlPhoto;
    /**
     * 视频地址
     */
    private String urlVideo;
    /**
     * 是否已经完成推送，0：未完成，1：已完成
     */
    private Integer finished;

    /**
     * 记录生成时间
     */
    private LocalDateTime createTime;
    /**
     * 记录修改时间
     */
    private LocalDateTime modifyTime;
}