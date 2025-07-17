package com.shmashine.api.mongo.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 楼层停靠统计
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/12/16 14:52
 * @Since: 1.0.0
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class FloorStopCountInfo {

    @Id
    private String id;

    /**
     * 电梯编号
     */
    private String elevatorCode;

    /**
     * 楼层号
     */
    private Integer floor;

    /**
     * 楼层数
     */
    private Integer floorCount;

    /**
     * 上次楼层统计记录数
     */
    private Integer lastFloorCount;

    /**
     * 统计时间  yyyy-MM-dd HH:mm:ss
     */
    private Date countTime;
}
