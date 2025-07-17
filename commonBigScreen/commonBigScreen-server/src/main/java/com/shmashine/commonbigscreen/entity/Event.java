package com.shmashine.commonbigscreen.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 仪电故障工单
 *
 * @author jiangheng
 * @version 1.0 -  2022/3/7 11:36
 */
@TableName("tbl_third_party_ruijin_envent")
@Data
public class Event {

    private String elevatorId;

    /**
     * 电梯名称
     */
    private String elevatorName;

    /**
     * 电梯编号
     */
    private String elevatorCode;

    /**
     * 电梯地址
     */
    private String address;

    /**
     * 上报时间
     */
    private String reportTime;

    /**
     * 处理人
     */
    private String handlerPeople;

    /**
     * 状态
     */
    private String status;

    /**
     * 事件id
     */
    private String eventId;

    /**
     * 事件唯一编号
     */
    private String eventNumber;

    /**
     * 故障id
     */
    private String faultId;

    /**
     * 事件名
     */
    private String eventName;
}
