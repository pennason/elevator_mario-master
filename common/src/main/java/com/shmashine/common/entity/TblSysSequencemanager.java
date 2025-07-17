package com.shmashine.common.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 采番表（唯一键生成策略）(TblSysSequencemanager)实体类
 *
 * @author liulifu
 * @since 2020-05-21 14:39:36
 */
public class TblSysSequencemanager implements Serializable {
    private static final long serialVersionUID = -39650356090664826L;
    /**
     * 编号前缀
     */
    private String vSeqId;
    /**
     * 当前最大编号
     */
    private Integer iSeqNo;
    /**
     * 描述
     */
    private String vDescription;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtCreatetime;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtModifytime;
    /**
     * 创建人
     */
    private String vCreateid;
    /**
     * 修改人
     */
    private String vModifyid;

    @JsonProperty("vSeqId")
    public String getVSeqId() {
        return vSeqId;
    }

    public void setVSeqId(String vSeqId) {
        this.vSeqId = vSeqId;
    }

    @JsonProperty("iSeqNo")
    public Integer getISeqNo() {
        return iSeqNo;
    }

    public void setISeqNo(Integer iSeqNo) {
        this.iSeqNo = iSeqNo;
    }

    @JsonProperty("vDescription")
    public String getVDescription() {
        return vDescription;
    }

    public void setVDescription(String vDescription) {
        this.vDescription = vDescription;
    }

    @JsonProperty("dtCreatetime")
    public Date getDtCreatetime() {
        return dtCreatetime;
    }

    public void setDtCreatetime(Date dtCreatetime) {
        this.dtCreatetime = dtCreatetime;
    }

    @JsonProperty("dtModifytime")
    public Date getDtModifytime() {
        return dtModifytime;
    }

    public void setDtModifytime(Date dtModifytime) {
        this.dtModifytime = dtModifytime;
    }

    @JsonProperty("vCreateid")
    public String getVCreateid() {
        return vCreateid;
    }

    public void setVCreateid(String vCreateid) {
        this.vCreateid = vCreateid;
    }

    @JsonProperty("vModifyid")
    public String getVModifyid() {
        return vModifyid;
    }

    public void setVModifyid(String vModifyid) {
        this.vModifyid = vModifyid;
    }

}