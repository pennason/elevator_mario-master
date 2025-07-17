package com.shmashine.api.module.fault.output;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 故障统计实体类
 *
 * @author little.li
 * @since 2020-06-17 17:45:48
 */
@Data
public class FaultResponseModule implements Serializable {


    /**
     * 电梯编号
     */
    private String elevatorCode;


    /**
     * 故障上报时间
     */
    private Date createTime;


    /**
     * 故障类型
     */
    private String faultType;


    /**
     * 故障名称
     */
    private String faultName;


    /**
     * 故障次数
     */
    private Integer faultNumCount;

    /**
     * 终端类型
     */
    private String eType;

    public String geteType() {
        return eType;
    }

    public void seteType(String eType) {
        this.eType = eType;
    }
}