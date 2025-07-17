// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/10/13 14:55
 * @since v1.0
 */

@Data
@ToString
@Accessors(chain = true)
public class TblCameraImageIdentifyEntity implements Serializable {
    /**
     * 自定义唯一ID，如故障ID， 图片根据此ID获取
     */
    private final String taskCustomId;

    /**
     * 自增ID
     */
    private Long id;

    /**
     * 电梯ID
     */
    private String elevatorId;
    /**
     * 电梯编号
     */
    private String elevatorCode;
    /**
     * 照片采集时间
     */
    private String collectTime;
    /**
     * 所在楼层
     */
    private String floor;
    /**
     * 识别图片地址
     */
    private String ossUrl;
    /**
     * 记录执行状态 0:初始状态，1：待识别，2：识别中， 3：已识别
     */
    private Integer status;
    /**
     * 业务类型 1：自研电动车识别， 2：自研乘梯人员（数量，年龄等），3：自研姿态识别 4:人数统计识别
     */
    private Integer identifyType;
    /**
     * 识别状态 0:无结果，1：成功，2：失败
     */
    private Integer identifyStatus;
    /**
     * 识别结果
     */
    private String identifyResult;


    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date createTime;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date modifyTime;
}
