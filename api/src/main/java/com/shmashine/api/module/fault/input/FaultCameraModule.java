package com.shmashine.api.module.fault.input;

import java.util.Date;

import lombok.Data;

/**
 * 摄像头服务，下载故障历史视频请求实体
 *
 * @author little.li
 */
@Data
public class FaultCameraModule {

    /**
     * 故障id
     */
    private String id;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

}
