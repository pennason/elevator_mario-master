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
 * @version v1.0  -  2023/8/23 18:17
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TblCameraExtendInfoEntity implements Serializable {
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
     * 本系统设备通道唯⼀标识
     */
    private String guid;
    /**
     * 国标id
     */
    private String gbid;
    /**
     * 设备CTEI码
     */
    private String ctei;
    /**
     * 摄像头类型 1：海康萤石，2：雄迈， 4：海康云眸, 5天翼云眼，6中兴 com.shmashine.common.enums.CameraTypeEnum
     */
    private Integer cameraType;
    /**
     * 通道序号
     */
    private Integer channelSeq;
    /**
     * 设备编号（国标级联编号）
     */
    private String deviceCode;
    /**
     * 设备所属平台编号。⼤华:QQY、中兴:TYBD、天翼云眼TYYY
     */
    private String vendorCode;
    /**
     * 0:离线 1:在线
     */
    private Integer onlineState;
    /**
     * 状态：-1:删除 0:离线 1:在线 2:故障 3:新增
     */
    private Integer status;
    /**
     * 事件发生事件
     */
    private String changeTime;

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
