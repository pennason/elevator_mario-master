package com.shmashine.fault.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 故障消息配置
 *
 * @author jiangheng
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class FaultMessageConfirm {

    //CHECKSTYLE:OFF

    @Id
    private String id;

    private String TY;

    private String ST;

    private String D;

    private String elevatorCode;

    private String faultType;

    private String fault_stype;

    private String sensorType;

    private String time;

    //CHECKSTYLE:ON

}
