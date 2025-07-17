package com.shmashine.socket.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 故障消息确认
 *
 * @author jiangheng
 * @version 2023/3/20 18:02
 */
@SuppressWarnings("checkstyle:MemberName")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class FaultMessageConfirm {

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

}
