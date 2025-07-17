// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.dto;

import lombok.Data;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/8 18:13
 * @since v1.0
 */

@Data
public class TblCameraDTO {
    /**
     * 摄像头ID
     */
    private String cameraId;
    /**
     * 所属电梯ID
     */
    private String elevatorId;
    /**
     * 电梯编号
     */
    private String elevatorCode;
    /**
     * 摄像头类型 1：海康萤石平台，2：雄迈平台，3：海尔平台，4：海康云眸, 5:天翼云眼， 6：中兴
     */
    private Integer cameraType;
    /**
     * 摄像头序列号
     */
    private String serialNumber;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 云平台序列号
     */
    private String cloudNumber;
    private String hlsUrl;
    private String rtmpUrl;
    private String privateUrl;

    /**
     * 云眼国标号
     */
    private String deviceCode;
    /**
     * 是否激活（0：未激活，1：已激活）
     */
    private Integer activate;
    /**
     * 在线状态 0-离线，1-在线
     */
    private Integer onlineStatus;
    /**
     * 删除标识 0-未删除，1-已删除
     */
    private Integer delFlag;
}
