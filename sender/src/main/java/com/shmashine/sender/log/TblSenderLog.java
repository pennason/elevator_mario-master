package com.shmashine.sender.log;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * tbl_sender_log
 *
 * @author
 */
@Data
public class TblSenderLog implements Serializable {
    private Integer id;

    /**
     * 电梯编号
     */
    private String elevatorCode;

    /**
     * 注册码
     */
    private String registerNumber;

    /**
     * 地址
     */
    private String adder;

    /**
     * 推送次数
     */
    private Integer monitorCount;

    /**
     * 故障推送次数
     */
    private Integer faultCount;

    /**
     * 推送日期
     */
    private Date sendDate;

    private static final long serialVersionUID = 1L;
}