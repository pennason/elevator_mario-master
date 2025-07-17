package com.shmashine.hkcamerabyys.client.entity;

import java.util.Date;

import lombok.Data;

/**
 * 下载文件请求体
 *
 * @author jiangheng
 * @version v1.0.0 - 2021/11/5 15:22
 */
@Data
public class DownLoadByHKYSRequestBody {

    /**
     * 电梯code
     */
    private String elevatorCode;

    /**
     * 上报类型：add/disappear
     */
    private String sType;

    /**
     * 故障id
     */
    private String faultId;

    /**
     * 故障类型
     */
    private String faultType;

    /**
     * 故障发生时间
     */
    private Date occurTime;

    /**
     * 设备序列号
     */
    private String deviceSerial;


}
