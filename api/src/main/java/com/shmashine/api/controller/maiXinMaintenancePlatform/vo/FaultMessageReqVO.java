package com.shmashine.api.controller.maiXinMaintenancePlatform.vo;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  jiangheng
 * @version 2024/3/6 18:07
 * @description: com.shmashine.fault.entity.maiXinMaintenancePlatform
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FaultMessageReqVO implements Serializable {


    /**
     * 平台代码
     */
    private String platformCode;

    /**
     * 电梯编号
     */
    private String elevatorCode;

    /**
     * 设备出厂编码
     */
    private String leaveFactoryNumber;

    /**
     * 电梯统一注册码
     */
    private String registerNumber;

    /**
     * 电梯特种设备代码
     */
    private String equCode;

    /**
     * 告警唯一标识
     */
    @NotBlank(message = "告警唯一标识不能为空")
    private String alarmId;

    /**
     * 故障状态 0：新增，1：消除
     */
    @NotNull(message = "故障状态不能为空")
    private Integer faultStatus;

    /**
     * 事件来源: 110电话：S01，物业：S02,维保：S03，物联网：S04，小程序：S05
     */
    private String alarmChannel;

    /**
     * 故障类型
     */
    @NotNull(message = "故障类型不能为空")
    private GovernFaultTypeEnum faultTypeEnum;

    /**
     * 故障描述-故障原因
     */
    @NotBlank(message = "故障描述不能为空")
    private String faultDescription;

    /**
     * 故障上报地点
     */
    private String address;

    /**
     * 故障取证图片
     */
    private String faultImageUrl;

    /**
     * 故障取证视频
     */
    private String faultVideoUrl;

    /**
     * 故障楼层
     */
    private String floor;

    /**
     * 故障时间
     */
    @NotNull(message = "故障时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date occurTime;

    /**
     * 上报时间: 平台上报上来的时间，与 故障产生时间不同
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date currentTime;

    /**
     * 实时数据内容
     */
    @NotNull(message = "实时数据内容不能为空")
    private RealMessageData data;

}
