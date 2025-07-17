// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.dto.mongo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/11/27 16:38
 * @since v1.0
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class ElevatorFaultCacheDTO implements Serializable {
    @Id
    private String id;

    /**
     * 故障ID
     */
    private String faultId;

    /**
     * 电梯编号
     */
    private String elevatorCode;

    /**
     * 创建时间  yyyy-MM-dd HH:mm:ss
     */
    private Date createAt;
    /**
     * 消息体
     */
    private String message;


}
