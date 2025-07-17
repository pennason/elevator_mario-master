// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hikYunMou.dto.requests;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/6/9 14:44
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HikCloudRecordVideoRequestDTO implements Serializable {
    /**
     * 项目ID 必须
     */
    private String projectId;
    /**
     * 文件ID 可选
     */
    private String fileId;
    /**
     * 通道号 必须
     */
    private Integer channelNo;
    /**
     * 设备序列号 必须
     */
    private String deviceSerial;
    /**
     * 开始时间：yyyyMMddHHmmss 必须
     */
    private String startTime;
    /**
     * 结束时间：yyyyMMddHHmmss， 录制时长要求在5秒以上且在24小时以内 必须
     */
    private String endTime;
    /**
     * 录像类型：local-本地录像，cloud-云存储 必须
     */
    private String recType;
    /**
     * 录像解密密钥，若设备加密则必须填写，否则视频无法录制成功 可选
     */
    private String validateCode;
    /**
     * 码流类型，可以选择 1：高清（主码流） 2：标清（子码流） 可选
     */
    private Integer streamType;
    /**
     * 录像文件切片间隔时长，单位分钟，范围：30分钟-210分钟，默认30分钟；小于30分钟按30分钟处理 可选
     */
    private Integer sliceDuration;
}
