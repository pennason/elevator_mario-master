package com.shmashine.common.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 电梯配置表(TblElevatorConfig)实体类
 *
 * @author little.li
 * @since 2020-09-10 13:55:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblElevatorConfig implements Serializable {

    @Serial
    private static final long serialVersionUID = -74048324141943564L;

    /**
     * 电梯配置表id
     */
    private String elevatorConfigId;
    /**
     * 电梯id
     */
    private String elevatorId;
    /**
     * 电梯编号
     */
    private String elevatorCode;

    /**
     * 电梯是否开启检测人数
     */
    private Integer isOpenDetectedPeopleNums;

    /**
     * 梯内小屏配置-音量
     */
    private String volume;
    /**
     * 梯内小屏配置-未佩戴口罩是否播放视频 0/1 0:不播放/1:播放
     */
    private String videoMask;
    /**
     * 梯内小屏配置-未佩戴口罩是否播放声音 0/1 0:不播放/1:播放
     */
    private String voiceMask;
    /**
     * 梯内小屏配置-梯内跳动打闹是否播放视频 0/1 0:不播放/1:播放
     */
    private String videoJump;
    /**
     * 梯内小屏配置-梯内跳动打闹是否播放声音 0/1 0:不播放/1:播放
     */
    private String voiceJump;
    /**
     * 梯内小屏配置-关门时受阻是否播放视频 0/1 0:不播放/1:播放
     */
    private String videoDoor;
    /**
     * 梯内小屏配置-关门时受阻是否播放声音 0/1 0:不播放/1:播放
     */
    private String voiceDoor;
    /**
     * 钢丝绳配置-Load(Q) (承重载荷)
     */
    private String loadQ;
    /**
     * 钢丝绳配置-The number of bearing wire ropes(nT) (绳索数量)
     */
    private String ropesNum;
    /**
     * 钢丝绳配置-The acceleration(g) (加速度)
     */
    private String acceleration;
    /**
     * 钢丝绳配置-The load guidance factor (负荷引导系数)
     */
    private String guidanceFactor;
    /**
     * 钢丝绳配置-The rope eﬃciency factor rope efficiency(η)
     */
    private String ropeEfficiency;
    /**
     * 钢丝绳配置-Parallel bearing ropes (平行轴承绳)
     */
    private String bearingRopes;
    /**
     * 钢丝绳配置-acceleration, deceleration (load speed)
     */
    private String loadSpeed;
    /**
     * 钢丝绳配置-The nominal rope diameter => d(mm) (绳索直径)
     */
    private String ropeDiameter;
    /**
     * 钢丝绳配置-The sheave diameter => D(mm) (滑轮直径)
     */
    private String sheaveDiameter;
    /**
     * 钢丝绳配置-The nominal tensile strength in N/mm2(R0) (标称抗拉强度)
     */
    private String tensileStrength;
    /**
     * 钢丝绳配置-Bending length(l>15d) => l (弯曲长度)
     */
    private String bendingLength;
    /**
     * 钢丝绳配置-Rope Lubrication (钢丝绳润滑)
     */
    private String ropeLubrication;
    /**
     * 钢丝绳配置-Rope construction
     */
    private String ropeConstruction;
    /**
     * 钢丝绳配置-Sheave groove - Steel round groove/groove radius
     */
    private String grooveRadius;
    /**
     * 钢丝绳配置-Sheave groove - undercut grooves/undercut angle
     */
    private String undercutAngle;
    /**
     * 钢丝绳配置-Sheave groove - angle
     */
    private String angle;
    /**
     * 钢丝绳配置-Sheave groove - angle of side deflection
     */
    private String sideDeflection;
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
     * 删除标识 0-未删除，1-已删除
     */
    private Integer delFlag;

}