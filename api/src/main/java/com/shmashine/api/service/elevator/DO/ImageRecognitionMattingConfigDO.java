package com.shmashine.api.service.elevator.DO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  jiangheng
 * @version 2023/11/8 11:23
 * @description: com.shmashine.api.service.elevator.DO
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ImageRecognitionMattingConfigDO {

    private Long id;

    private String elevatorId;

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
