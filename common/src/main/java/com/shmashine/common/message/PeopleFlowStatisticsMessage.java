package com.shmashine.common.message;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author  jiangheng
 * @version 2024/1/8 16:28
 * @description: 人流量统计消息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PeopleFlowStatisticsMessage implements Serializable {

    private static final long serialVersionUID = 20240108162810000L;

    /**
     * id
     */
    private Long id;

    /**
     * 电梯编号
     */
    private String elevatorCode;

    /**
     * 触发时间
     */
    private Date triggerTime;

    /**
     * 所在楼层
     */
    private Integer floor;

    /**
     * 轿厢运行方向 0：折返，1：上行，-1：下行
     */
    private Integer direction;

    /**
     * 识别结果人数
     */
    private Integer identificationNumber;

    /**
     * 人流量(吞吐量)
     */
    private Integer throughput;

    /**
     * 图片地址
     */
    private String picUrl;
}
