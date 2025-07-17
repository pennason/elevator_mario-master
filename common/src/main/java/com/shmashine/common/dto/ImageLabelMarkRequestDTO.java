// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 图像标注-暂未标注的记录信息
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/11/9 17:02
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ImageLabelMarkRequestDTO implements Serializable {
    /**
     * 故障唯一id
     */
    @Schema(description = "故障唯一id", required = true)
    private String faultId;
    /**
     * 文件地址, 上传至自己阿里云平台的地址
     */
    private String imagePath;
    /**
     * 标注类型， 37：助动车
     */
    @Schema(description = "标注类型， 37：助动车", required = true)
    private Integer markType;
    /**
     * 标注图片时的图片宽度
     */
    @Schema(description = "标注图片时的图片宽度", required = true)
    private Integer width;
    /**
     * 标注图片时的图片高度
     */
    @Schema(description = "标注图片时的图片高度", required = true)
    private Integer height;
    /**
     * 标注信息， 空表示不需要标注
     */
    private List<ImageLabel> labels;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class ImageLabel implements Serializable {
        @Schema(description = "标注框左上角x坐标", required = true)
        private Integer x1;
        @Schema(description = "标注框左上角y坐标", required = true)
        private Integer y1;
        @Schema(description = "标注框右下角x坐标", required = true)
        private Integer x2;
        @Schema(description = "标注框右下角y坐标", required = true)
        private Integer y2;
        @Schema(description = "标注框标签", required = true)
        private String label;
        @Schema(description = "背景色，只用于前端展示")
        private String background;
        @Schema(description = "框颜色，只用于前端展示")
        private String color;

    }

}