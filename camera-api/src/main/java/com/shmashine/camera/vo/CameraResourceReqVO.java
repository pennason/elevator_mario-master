package com.shmashine.camera.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author  jiangheng
 * @version 2023/3/28 13:56
 * @description: com.shmashine.camera.vo
 */
@Data
public class CameraResourceReqVO {

    /**
     * 摄像头序列号
     */
    @Schema(description = "摄像头序列号")
    private String cameraSerial;

    /**
     * 摄像头类型 1：海康萤石平台，2：雄迈平台，3：海尔平台，4：海康云眸, 5：天翼云眼，6:中兴，
     */
    @Schema(description = "摄像头类型 1：海康萤石平台，2：雄迈平台，3：海尔平台，4：海康云眸, 5：天翼云眼，6:中兴，", defaultValue = "1", example = "1")
    private Integer cameraType;

    /**
     * 电梯编号
     */
    @Schema(description = "电梯编号")
    private String elevatorCode;

    /**
     * 故障类型
     */
    @Schema(description = "故障类型")
    private String faultType;

    /**
     * 故障id
     */
    @Schema(description = "故障id")
    private String faultId;

    /**
     * 流播放协议，hls、rtmp、flv
     */
    @Schema(description = "流播放协议，hls、rtmp、flv", defaultValue = "hls", example = "hls")
    private String protocol;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String stopTime;

    /**
     * 故障发生时间 yyyy-MM-dd HH:mm:ss
     */
    @Schema(description = "故障发生时间 yyyy-MM-dd HH:mm:ss")
    private String occurTime;

    /**
     * 视频清晰度，1-高清，2-标清
     */
    @Schema(description = "视频清晰度，1-高清，2-标清", defaultValue = "1", example = "1")
    private Integer quality;

    /**
     * 过期时间
     */
    private Long expireTime;
}