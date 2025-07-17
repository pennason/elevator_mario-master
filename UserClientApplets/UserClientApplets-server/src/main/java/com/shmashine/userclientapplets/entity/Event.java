package com.shmashine.userclientapplets.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 维修记录表
 *
 * @author jiangheng
 * @version V1.0 - 2022/2/15 14:25
 */
@TableName("tbl_third_party_ruijin_envent")
@Data
@EqualsAndHashCode(callSuper = false)
public class Event extends BaseEntity {

    /**
     * 故障id
     */
    private String faultId;

    /**
     * 事件编号
     */
    private String eventNumber;

    /**
     * 事件来源
     */
    private String eventChannel;

    /**
     * 电梯注册代码
     */
    private String registerNumber;

    /**
     * 电梯名
     */
    private String elevatorName;

    /**
     * 当前状态（1：渠道上报  2：新建工单 3：已接单、4：已签到 5：已完成 6：已确认 7： 误报 8：事故）
     */
    private Integer currentStatus;

    /**
     * 事件内容
     */
    private String eventDesc;

    /**
     * 处理人
     */
    private String handler;

    /**
     * 处理人电话
     */
    private String handlerTel;

    /**
     * 报警人
     */
    private String reporter;

    /**
     * 取证视频地址
     */
    private String videoUrl;

    /**
     * 取证图片地址
     */
    private String imageUrl;

    /**
     * 服务模式
     */
    private String modeStatusName;

    /**
     * 地址
     */
    private String address;

    /**
     * 故障状态
     */
    private String statusName;

    /**
     * 上报时间
     */
    private String reportTime;

    /**
     * 结束时间
     */
    private String endTime;
}
