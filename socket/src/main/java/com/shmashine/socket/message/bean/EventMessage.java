package com.shmashine.socket.message.bean;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 电梯模式切换消息
 *
 * @author jiangheng
 * @version 1.0 - 2021/4/30 16:33
 */
@Data
public class EventMessage implements Serializable {

    private static final long serialVersionUID = 469685613478676787L;

    /**
     * 电梯模式切换事件id
     */
    private String id;

    /**
     * 电梯code
     */
    private String elevatorCode;

    /**
     * 当前模式
     */
    private Integer eventType;

    /**
     * 当前状态
     */
    private Integer status;

    /**
     * 故障类型
     */
    private Integer faultType;

    /**
     * 故障名
     */
    private String faultName;

    /**
     * 创建时间
     */
    private Date createTime;
}
