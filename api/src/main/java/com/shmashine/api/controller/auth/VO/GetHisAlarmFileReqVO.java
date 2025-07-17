package com.shmashine.api.controller.auth.VO;

import java.util.Date;

import lombok.Data;

/**
 * 获取历史告警文件请求体
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2025/2/7 14:14
 * @Since: 1.0.0
 */
@Data
public class GetHisAlarmFileReqVO {

    /**
     * 告警编号 电梯远程监测系统告警上传时的alarmId
     */
    private String alarmCode;
    /**
     * 电梯注册码
     */
    private String registerNumber;
    /**
     * 告警发生时间 yyyy-MM-dd  HH:mm:ss 格式字符串
     */
    private Date occurTime;
}
