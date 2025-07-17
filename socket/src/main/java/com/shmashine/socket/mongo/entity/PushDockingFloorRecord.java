package com.shmashine.socket.mongo.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 推送楼层停靠记录缓存
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/12/30 10:09
 * @Since: 1.0.0
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class PushDockingFloorRecord {

    @Id
    private String id;

    /**
     * 电梯编号
     */
    private String elevatorCode;

    /**
     * 当前楼层号
     */
    private Integer nowFloor;

    /**
     * 预停靠楼层
     */
    private Integer dockingFloor;

    /**
     * 是否上客(人数)
     */
    private Integer peopleNumber;

    /**
     * 下次运行到达楼层
     */
    private Integer arrivalFloor;

    /**
     * 记录时间
     */
    private Date recordTime;

}
