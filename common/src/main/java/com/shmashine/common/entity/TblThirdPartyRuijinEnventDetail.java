package com.shmashine.common.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 瑞景医院，故障事件明细表(TblThirdPartyRuijinEnventDetail)实体类
 *
 * @author makejava
 * @since 2020-09-28 16:53:55
 */
@Data
public class TblThirdPartyRuijinEnventDetail implements Serializable {
    private static final long serialVersionUID = -22614966566727217L;
    private String eventId;
    private String eventDetailId;
    /**
     * 事件编号
     */
    private String eventNumber;
    /**
     * 发生时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date statusTime;
    /**
     * 状态
     */
    private String status;
    /**
     * 备注
     */
    private String comment;
    /**
     * 处理人
     */
    private String handler;
    /**
     * 处理人电话
     */
    private String handlerTel;
    /**
     * 救援单位名称
     */
    private String rescueCompanyName;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtCreateTime;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtModifyTime;
    /**
     * 创建记录用户
     */
    private String vCreateUserId;
    /**
     * 修改记录用户
     */
    private String vModifyUserId;

}