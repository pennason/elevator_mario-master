// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.client.dto.callback;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/22 10:02
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "设备1400图⽚通知", description = "设备1400图⽚通知")
public class CallbackDeviceImage1400NotifyDTO implements Serializable {
    /**
     * 抓拍类型，1:⼈脸2:⻋牌
     */
    @Schema(title = "抓拍类型", description = "抓拍类型，1:⼈脸2:⻋牌", example = "1", required = true)
    private Integer shotType;
    /**
     * 设备码
     */
    @Schema(title = "设备码", description = "设备码", required = true)
    private String uid;
    /**
     * 国标id
     */
    @Schema(title = "国标id", description = "国标id", required = true)
    private String gbId;
    /**
     * 图⽚组Id (⼈脸或者机动⻋Id)
     */
    @Schema(title = "图⽚组Id", description = "图⽚组Id (⼈脸或者机动⻋Id)", required = true)
    private String imageGroupId;
    /**
     * 设备拥有者⼿机号
     */
    @Schema(title = "设备拥有者⼿机号", description = "设备拥有者⼿机号", required = true)
    private String phone;
    /**
     * 特征信息, 如⻋牌号(⽬前只传⻋牌号, ⼈脸时为空, 后续有多个值时⽤逗号分隔)
     */
    @Schema(title = "特征信息", description = "特征信息, 如⻋牌号(⽬前只传⻋牌号, ⼈脸时为空, 后续有多个值时⽤逗号分隔)")
    private String features;
    /**
     * 图像对象列表
     */
    @Schema(title = "图像对象列表", description = "图像对象列表", required = true)
    private List<ImageInfo> imageInfoList;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ImageInfo implements Serializable {
        /**
         * 图像信息标识id（必需唯⼀）
         */
        @Schema(title = "图像信息标识id", description = "图像信息标识id（必需唯⼀）", required = true)
        private Integer imageId;
        /**
         * 图⽚宽度
         */
        @Schema(title = "图⽚宽度", description = "图⽚宽度")
        private String width;
        /**
         * 图⽚⾼度
         */
        @Schema(title = "图⽚⾼度", description = "图⽚⾼度")
        private Integer height;
        /**
         * 时间（yyyyMMddHHmmss）
         */
        @Schema(title = "时间", description = "时间（yyyyMMddHHmmss）", required = true)
        private String shortTime;
        /**
         * ⽂件类型
         */
        @Schema(title = "⽂件类型", description = "⽂件类型")
        private String fileFormat;
        /**
         * 图像类型，11 ⼈脸特征图，14 背景图
         */
        @Schema(title = "图像类型", description = "图像类型，11 ⼈脸特征图，14 背景图", required = true)
        private String type;
        /**
         * 图⽚路径
         */
        @Schema(title = "图⽚路径", description = "图⽚路径")
        private String imageUrl;


        /**
         * 容器Id
         */
        @Schema(title = "容器Id", description = "容器Id")
        private Integer containerId;
        /**
         * 对象Id
         */
        @Schema(title = "对象Id", description = "对象Id")
        private String objectId;
    }
}
