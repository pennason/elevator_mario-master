package com.shmashine.socket.nezha.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 设备上下线
 *
 * @author little.li
 */
@Data
public class ElevatorEventRecordDO implements Serializable {

    private static final long serialVersionUID = 1L;

    //
    private Long id;
    //电梯code
    private String elevatorCode;
    //事件类型，1上线，2离线
    private Integer typeId;
    //实际数据
    private String data;
    //发生时间
    private Date startTime;
    //设备类型
    private String sensorType;
}
