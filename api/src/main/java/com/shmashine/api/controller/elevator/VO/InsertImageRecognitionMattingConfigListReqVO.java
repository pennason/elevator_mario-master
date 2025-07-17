package com.shmashine.api.controller.elevator.VO;

import java.util.List;

import lombok.Data;

/**
 * @author  jiangheng
 * @version 2023/11/8 15:46
 * @description: com.shmashine.api.controller.elevator.VO
 */
@Data
public class InsertImageRecognitionMattingConfigListReqVO {

    private String faultTypes;

    private List<elevator> elevators;

    @Data
    public static class elevator {
        private String elevatorId;
        private String elevatorCode;
    }
}
