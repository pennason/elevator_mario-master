// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.dto.requests;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 15:06
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DevicePlaybackUrlRequestDTO implements Serializable {
    /**
     * 统⼀设备管理平台中设备通道唯⼀标识
     */
    @Schema(title = "统⼀设备管理平台中设备通道唯⼀标识", description = "统⼀设备管理平台中设备通道唯⼀标识", required = true)
    private String guid;
    /**
     * 访问类型，接⼊⽹络类型 0：内⽹，1：公⽹，2: 其他，默认为1
     */
    @Schema(title = "访问类型", description = "接⼊⽹络类型 0：内⽹，1：公⽹，2: 其他，默认为1", defaultValue = "1")
    private Integer accessType;
    /**
     * 开始时间，格式：yyyyMMddHHmmss
     */
    @Schema(title = "开始时间", description = "开始时间，格式：yyyyMMddHHmmss", required = true)
    private String beginTime;
    /**
     * 结束时间，格式：yyyyMMddHHmmss
     */
    @Schema(title = "结束时间", description = "结束时间，格式：yyyyMMddHHmmss", required = true)
    private String endTime;
    /**
     * 视频协议 1:rtsp 2:hls 3:flv 4:rtmp，不填默认rtsp。 ⼤华：⽀持rtsp、hls 中兴：⽀持rtsp 天翼云眼：⽀持rtsp、hls
     */
    @Schema(title = "视频协议",
            description = "视频协议 1:rtsp 2:hls 3:flv 4:rtmp, 5:mp4, 6:hls(https)，不填默认rtsp。"
                    + " ⼤华：⽀持rtsp、hls 中兴：⽀持rtsp 天翼云眼：⽀持rtsp、hls, rtmp, hls(https)",
            defaultValue = "1", example = "1")
    private Integer proto;
    /**
     * 每⻚条数，不传默认10，最⼤50
     */
    @Schema(title = "每⻚条数", description = "每⻚条数，不传默认10，最⼤50", defaultValue = "10")
    private Integer pageSize;
    /**
     * 当前⻚，默认从1开始
     */
    @Schema(title = "当前⻚", description = "当前⻚，默认从1开始", defaultValue = "1")
    private Integer pageNum;
    /**
     * 0--回看流，1--录像⽂件下载，默认为回看流（⽬前⼤华不⽀持提供录像⽂件的下载流）
     */
    @Schema(title = "是否下载", description = "0--回看流，1--录像⽂件下载，默认为回看流（⽬前⼤华不⽀持提供录像⽂件的下载流）", example = "0",
            defaultValue = "0")
    private Integer isDownload;
    /**
     * 录像⽂件的编号 中兴：录像位置为pl平台的时候，此项为必填 天翼云眼：⽂件地址为云上的时候为必填项
     */
    @Schema(title = "录像⽂件的编号", description = "录像⽂件的编号 中兴：录像位置为pl平台的时候，此项为必填 天翼云眼：⽂件地址为云上的时候为必填项")
    private String fileId;
    /**
     * 码流编号，1:主码流1 （主码流[1]/⼦码流1[2]/⼦码流2[3]）,缺省为主码流
     */
    @Schema(title = "码流编号", description = "码流编号，1:主码流1 （主码流[1]/⼦码流1[2]/⼦码流2[3]）,缺省为主码流", defaultValue = "1")
    private Integer streamId;
    /**
     * 录像⽂件存放的位置，默认为0，云端。 中兴：0-- pl平台 ，1--pu前端 天翼云眼：0--云上，1--平台 ⼤华：0--cloud，1--device
     */
    @Schema(title = "录像⽂件存放的位置",
            description = "录像⽂件存放的位置，默认为0，云端。 中兴：0-- pl平台 ，1--pu前端 天翼云眼：0--云上，1--平台 ⼤华：0--cloud，1--device",
            defaultValue = "1", example = "1")
    private String location;
}
