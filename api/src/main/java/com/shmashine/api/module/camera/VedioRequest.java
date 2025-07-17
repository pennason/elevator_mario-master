package com.shmashine.api.module.camera;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by Administrator on 2019/3/30.
 */
@Data
public class VedioRequest implements Serializable {

    private static final long serialVersionUID = -1L;

    // 电梯注册码
    private String registerNumber;

    // 告警编号 （对应平台 故障表的 failureNo 字段）
    private String alarmCode;

    // 视频开始时间
    private Integer beginTime;

    // 视频结束时间
    private Integer endTime;

}
