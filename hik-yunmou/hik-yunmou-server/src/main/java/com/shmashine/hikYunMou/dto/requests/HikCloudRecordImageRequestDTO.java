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
public class HikCloudRecordImageRequestDTO implements Serializable {
    /**
     * 项目ID 必须
     */
    private String projectId;
    /**
     * 设备序列号 必须
     */
    private String deviceSerial;
    /**
     * 通道号 必须
     */
    private Integer channelNo;
    /**
     * 录像解密密钥，若设备加密则必须填写，否则视频无法录制成功 可选
     */
    private String validateCode;
    /**
     * 录像类型：local-本地录像，cloud-云存储 必须
     */
    private String recType;
    /**
     * 协议若不传，则标识为萤石协议；若传：gb28181，标识为国标设备
     */
    private String devProto;
    /**
     * 抽帧模式，0：普通模式，1：错峰抽帧模式，不填默认:0
     */
    private String frameModel;
    /**
     * 码流类型，可以选择 1：高清（主码流） 2：标清（子码流） 可选，不填默认为1
     */
    private Integer streamType;
    /**
     * 抽帧时间点列表，格式: yyyyMMddHHmmss,yyyyMMddHHmmss，时间点以英文逗号分隔
     */
    private String timingPoints;
}
