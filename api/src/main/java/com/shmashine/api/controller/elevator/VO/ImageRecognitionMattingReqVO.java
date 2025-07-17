package com.shmashine.api.controller.elevator.VO;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 获取摄像头抠图配置列表请求参数
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/5/27 11:18
 * @Since: 1.0.0
 */
@Data
public class ImageRecognitionMattingReqVO {

    @NotNull(message = "页码不能为空")
    private Integer pageNo;

    @NotNull(message = "每页条数不能为空")
    private Integer pageSize;

    /**
     * 电梯编号
     */
    private String elevatorCode;

    /**
     * 小区id
     */
    private String villageId;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 摄像头在线状态
     */
    private Integer cameraOnlineStatus;

    /**
     * 是否抠图 0：未抠图 1：已抠图
     */
    private Integer imageMatting;
}
