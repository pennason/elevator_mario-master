package com.shmashine.api.controller.elevator.VO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author  jiangheng
 * @version 2023/11/8 14:25
 * @description: com.shmashine.api.controller.elevator.VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ImageRecognitionMattingConfigRespVO extends ImageRecognitionMattingConfigReqVO {

    /**
     * 主键
     */
    private String id;

    /**
     * 图片地址
     */
    private String picUrl;
}
