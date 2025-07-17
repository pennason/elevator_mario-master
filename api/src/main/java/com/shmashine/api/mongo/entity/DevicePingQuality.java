package com.shmashine.api.mongo.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  jiangheng
 * @version 2023/6/28 16:18
 * @description: 设备通讯质量
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class DevicePingQuality {

    @Id
    private String id;

    /**
     * 电梯编号
     */
    private String elevatorCode;

    /**
     * 最大时延
     */
    private Integer pingMax;

    /**
     * 最小时延
     */
    private Integer pingMin;

    /**
     * 平均时延
     */
    private Integer pingAvg;

    /**
     * 丢包率
     */
    private Double pingLoss;

    /**
     * 今日最大时延
     */
    private Integer todayPingMax;

    /**
     * 今日最小时延
     */
    private Integer todayPingMin;

    /**
     * 今日平均时延
     */
    private Double todayPingAvg;

    /**
     * 今日丢包率
     */
    private Double todayPingLoss;

    /**
     * 统计时间  yyyy-MM-dd HH:mm:ss
     */
    private Date updateTime;

    /**
     * 统计日期 yyyy-MM-dd
     */
    private String updateDate;
}
