package com.shmashine.fault.fault.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;


/**
 * 故障定义表(TblFaultDefinition)实体类
 *
 * @author little.li
 * @since 2020-09-02 11:04:31
 */
@Data
public class TblFaultDefinition0902 {

    /**
     * 故障定义唯一ID
     */
    private String faultDefinitionId;
    /**
     * 故障类型
     */
    private String faultType;
    /**
     * 故障名称
     */
    private String faultName;
    /**
     * 故障子类型
     */
    private String faultSecondType;
    /**
     * 故障子类型名称
     */
    private String faultSecondName;
    /**
     * 故障解决方式
     */
    private String resolve;
    /**
     * 备注
     */
    private String remark;
    /**
     * 故障等级
     */
    private Integer level;
    /**
     * 故障等级名称
     */
    private String levelName;
    /**
     * 是否保存故障视频 0：不保存，1：保存
     */
    private Integer saveVideo;
    /**
     * 是否保存故障图片 0：不保存，1：保存
     */
    private Integer saveImage;
    /**
     * 是否拦截确认 0：不拦截-直接生成故障，1：拦截-人工确认故障， 2：拦截-延迟1分钟自动确认故障
     */
    private Integer filterFlag;
    /**
     * 不文明行为标识，0：故障，1：不文明行为
     */
    private Integer uncivilizedBehaviorFlag;
    /**
     * 平台名称
     */
    private String platformType;
    /**
     * 电梯类型 1 直梯; 2 扶梯
     */
    private Integer elevatorType;
    /**
     * 设备类型 1 后装设备; 2 前装设备
     */
    private Integer sensorType;

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
    /**
     * 创建记录用户
     */
    private String createUserId;
    /**
     * 修改记录用户
     */
    private String modifyUserId;

    /**
     * 是否发送短信
     */
    private Integer sendSMS;

    /**
     * 删除标识 0-未删除，1-已删除
     */
    private Integer delFlag;


}