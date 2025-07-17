// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 缓存电梯相关信息， 夜间守护， 群租问题使用
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/7 15:58
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ElevatorCacheDTO implements Serializable {

    /**
     * 电梯唯一ID
     */
    private String elevatorId;
    /**
     * 电梯编号
     */
    private String elevatorCode;
    /**
     * 电梯类型
     */
    private Integer elevatorType;

    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 小区ID
     */
    private String villageId;
    /**
     * 设备安装状态 0：未安装，1：已安装
     */
    private Integer installStatus;
    /**
     * 最高楼层
     */
    private Integer maxFloor;
    /**
     * 最低楼层
     */
    private Integer minFloor;
    /**
     * 楼层详细信息
     */
    private String floorDetail;
    /**
     * 额定速度
     */
    private Double ratedSpeed;
    /**
     * 出场编号
     */
    private String leaveFactoryNumber;
    /**
     * 是否已删除
     */
    private Integer delFlag;
    /**
     * 夜间守护模式 1:开启
     */
    private Integer nightWatchStatus;
    /**
     * 守护开启时间
     */
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private String nightWatchStartTime;
    /**
     * 守护结束时间
     */
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private String nightWatchEndTime;

    // 小区信息

    /**
     * 小区名称
     */
    private String villageName;

    /**
     * 群租识别开启 0：不开启 1：开启中
     */
    private Integer groupLeasingStatus;
    /**
     * 群租 时间系数 0-23  2,2,2,2,1.5,1.5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1.5,1.5
     */
    private String groupLeasingTimeCoefficient;
    /**
     * 群租 阈值 50, 100, 150, 200,
     */
    private String groupLeasingStepRange;
    /**
     * 群租 开始取证日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date groupLeasingStartDate;
    /**
     * 群租 结束取证日期 默认30天结束
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date groupLeasingEndDate;
    /**
     * 群租 每日取证开始时间
     */
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private String groupLeasingStartTime;
    /**
     * 群租 每日取证结束时间
     */
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private String groupLeasingEndTime;
    /**
     * 群租状态确认 0：未确认 1：取证中 2：已确认 参考 @com.shmashine.common.enums.GroupLeasingResultEnum
     */
    private Integer groupLeasingResult;
    /**
     * 街道
     */
    private String street;

}
