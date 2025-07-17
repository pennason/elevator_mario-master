package com.shmashine.sender.log;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import lombok.Data;

/**
 * 推送信息计数器 统计
 */
@Data
public class SenderCounter implements Serializable {

    /**
     * 电梯推送分组
     */
    private String groupId;
    /**
     * 电梯注册码
     */
    private String registerNumber;

    /**
     * 电梯注册码
     */
    private String elevatorCode;

    /**
     * 电梯名称
     */
    private String elevatorName;

    /**
     * 位置
     */
    private String adder;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 总共推送
     */
    private AtomicLong totalCount = new AtomicLong(0);

    /**
     * 单日监控推送
     */
    private AtomicLong monitorCount = new AtomicLong(0);

    /**
     * 单日故障推送
     */
    private AtomicLong faultCount = new AtomicLong(0);
    private AtomicLong staticCount = new AtomicLong(1);

    /**
     * 上一次推送时间
     */
    private Date lastTime = new Date();

    private boolean online = true;

}

