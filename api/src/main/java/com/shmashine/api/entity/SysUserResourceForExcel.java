package com.shmashine.api.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;

/**
 * @AUTHOR jiangheng
 * @DATA 2021/3/4 - 16:12
 * excel批量导入电梯表资源授权实体类
 */
@Builder
public class SysUserResourceForExcel implements Serializable {

    private static final long serialVersionUID = 210045872510769373L;
    /**
     * 用户id
     */
    private String vUserId;
    /**
     * 资源id
     */
    private String vResourceId;
    /**
     * 资源类型
     */
    private Integer iResourceType;
    /**
     * 外键code（电梯code）
     */
    private String vResourceCode;
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
     * 创建人
     */
    private String vCreateUserId;
    /**
     * 修改人
     */
    private String vModifyUserId;
    /**
     * 删除标识 0-未删除，1-已删除
     */
    private Integer iDelFlag;
}
