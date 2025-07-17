package com.shmashine.common.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图像识别匹配配置
 *
 * @author jiangheng
 * @since 2023/11/8 11:23
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ImageRecognitionMattingConfigEntity {

    private Long id;

    private Long elevatorId;

    private String elevatorCode;

    private String faultTypes;

    private Integer width;

    private Integer height;

    //标注坐标
    private String coordinates;

    //真实坐标
    private String realCoordinates;

    private String creator;

    private Date create_time;

    private Date update_time;

}
