package com.shmashine.api.controller.elevator.VO;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.ToString;

/**
 * @author  jiangheng
 * @version 2023/11/8 10:49
 * @description: 图像识别抠图配置请求参数
 */
@Data
@ToString
public class ImageRecognitionMattingConfigReqVO implements Serializable {

    private String elevatorId;

    private String elevatorCode;

    private String faultTypes;

    private Integer width;

    private Integer height;

    private List<Coordinate> coordinates;

    @Data
    public static class Coordinate {

        private int[] x;
        private int[] y;
    }
}
