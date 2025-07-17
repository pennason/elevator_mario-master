package com.shmashine.api.controller.elevator.VO;

import java.util.Date;

import lombok.Data;

/**
 * 电梯图像识别抠图区域配置
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/5/27 11:37
 * @Since: 1.0.0
 */
@Data
public class ImageRecognitionMattingRespVO {

    /**
     * 电梯ID-电梯表主键id
     */
    private String elevatorId;

    /**
     * 电梯编号
     */
    private String elevatorCode;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 小区id
     */
    private String villageId;

    /**
     * 小区名
     */
    private String villageName;

    /**
     * 摄像头在线状态
     */
    private Integer cameraOnlineStatus;

    /**
     * 坐标
     */
    private String coordinates;

    /**
     * 抠图更新时间
     */
    private Date updateTime;

    /**
     * 标记图片宽
     */
    private Integer width;

    /**
     * 标记图片高
     */
    private Integer height;

}
