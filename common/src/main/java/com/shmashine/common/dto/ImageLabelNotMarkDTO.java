// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

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
public class ImageLabelNotMarkDTO implements Serializable {
    /**
     * 故障唯一id
     */
    private String faultId;
    /**
     * 电梯编号
     */
    private String elevatorCode;
    /**
     * 记录时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date collectTime;
    /**
     * 文件地址, 上传至自己阿里云平台的地址
     */
    private String fileOssUrl;
    /**
     * 标注类型， 37：助动车
     */
    private Integer markType;
}
