// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/7/26 11:52
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TblCameraCascadePlatformEntity implements Serializable {
    /**
     * ID， 自增
     */
    private Long id;
    /**
     * 云平台序列号，通过该序号获取摄像头视频
     */
    private String cloudNumber;
    /**
     * 上级或下级平台ID
     */
    private String platformId;
    /**
     * 上级或下级平台名称
     */
    private String platformName;
    /**
     * 对接的上级还是下级 1：对接上级平台，2：对接下级平台
     */
    private Integer supOrSub;
    /**
     * 电梯编号
     */
    private String elevatorCode;
    /**
     * 摄像头类型 1：海康萤石，2：雄迈， 4：海康云眸 com.shmashine.common.enums.CameraTypeEnum
     */
    private Integer cameraType;
    /**
     * 通道ID
     */
    private String channelId;
    /**
     * 通道号
     */
    private Integer channelNo;
    /**
     * 通道级联编码
     */
    private String channelCode;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date createTime;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date modifyTime;
}
