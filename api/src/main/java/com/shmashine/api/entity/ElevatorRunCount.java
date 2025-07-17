package com.shmashine.api.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/8/12 17:26
 */
@Data
public class ElevatorRunCount implements Serializable {

    private static final long serialVersionUID = 65667368742832535L;

    //主键id
    private String id;

    //电梯code
    private String vElevatorCode;

    //电梯项目id
    private String vProjectId;

    //运行次数
    private Long biRunCount;

    //上一次记录运行次数
    private Long lastRunCount;

    //开关门次数
    private Long biDoorCount;

    //上一次记录开关门次数
    private Long lastDoorCount;

    //钢丝绳折弯次数
    private Long biBendCount;

    //上一次钢丝绳折弯次数
    private Long lastBendCount;

    //平层触发次数
    private Long biLevelTriggerCount;

    //上一次平层触发次数
    private Long lastLevelTriggerCount;

    //累计运行距离（米）
    private Long biRunDistanceCount;

    //上一次累计运行距离（米）
    private Long lastRunDistanceCount;

    //往返次数
    private Long backAndForthCount;

    //累计往返次数
    private Long lastBackAndForthCount;

    //客流量
    private Integer passengerFlow;

    //记录日期
    private Date dReportDate;

    //创建时间
    private Date dtCreateTime;

    //更新时间
    private Date updateTime;
}
