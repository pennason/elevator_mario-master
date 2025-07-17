package com.shmashine.common.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 用户主档资源对应表(TblSysUserResource)实体类
 *
 * @author makejava
 * @since 2020-06-19 16:58:50
 */
public class TblSysUserResource implements Serializable {
    private static final long serialVersionUID = 210063520510769373L;
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


    @JsonProperty("vUserId")
    public String getVUserId() {
        return vUserId;
    }

    public void setVUserId(String vUserId) {
        this.vUserId = vUserId;
    }


    @JsonProperty("vResourceId")
    public String getVResourceId() {
        return vResourceId;
    }

    public void setVResourceId(String vResourceId) {
        this.vResourceId = vResourceId;
    }

    @JsonProperty("iResourceType")
    public Integer getIResourceType() {
        return iResourceType;
    }

    public void setIResourceType(Integer iResourceType) {
        this.iResourceType = iResourceType;
    }

    @JsonProperty("vResourceCode")
    public String getVResourceCode() {
        return vResourceCode;
    }

    public void setVResourceCode(String vResourceCode) {
        this.vResourceCode = vResourceCode;
    }

    @JsonProperty("dtCreateTime")
    public Date getDtCreateTime() {
        return dtCreateTime;
    }

    public void setDtCreateTime(Date dtCreateTime) {
        this.dtCreateTime = dtCreateTime;
    }

    @JsonProperty("dtModifyTime")
    public Date getDtModifyTime() {
        return dtModifyTime;
    }

    public void setDtModifyTime(Date dtModifyTime) {
        this.dtModifyTime = dtModifyTime;
    }

    @JsonProperty("vCreateUserId")
    public String getVCreateUserId() {
        return vCreateUserId;
    }

    public void setVCreateUserId(String vCreateUserId) {
        this.vCreateUserId = vCreateUserId;
    }

    @JsonProperty("vModifyUserId")
    public String getVModifyUserId() {
        return vModifyUserId;
    }

    public void setVModifyUserId(String vModifyUserId) {
        this.vModifyUserId = vModifyUserId;
    }

    @JsonProperty("iDelFlag")
    public Integer getIDelFlag() {
        return iDelFlag;
    }

    public void setIDelFlag(Integer iDelFlag) {
        this.iDelFlag = iDelFlag;
    }

}