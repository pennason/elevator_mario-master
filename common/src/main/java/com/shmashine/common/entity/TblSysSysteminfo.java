package com.shmashine.common.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 系统字典表（包括系统所有枚举值）(TblSysSysteminfo)实体类
 *
 * @author liulifu
 * @since 2020-05-21 14:39:36
 */
public class TblSysSysteminfo implements Serializable {
    private static final long serialVersionUID = 398239088849572700L;
    /**
     * 唯一编号
     */
    private Integer vSysid;
    /**
     * 子编号
     */
    private Integer vSyssubid;
    /**
     * 值1
     */
    private String vSysvalue1;
    /**
     * 值2
     */
    private String vSysvalue2;
    /**
     * 值3
     */
    private String vSysvalue3;
    /**
     * 值4
     */
    private String vSysvalue4;
    /**
     * 值5
     */
    private String vSysvalue5;
    /**
     * 中文名字
     */
    private String vNameZhCn;
    /**
     * 排序号
     */
    private Integer iSortindex;
    /**
     * 是否是修复的值
     */
    private String vIsfixvalue;
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

    @JsonProperty("vSysid")
    public Integer getVSysid() {
        return vSysid;
    }

    public void setVSysid(Integer vSysid) {
        this.vSysid = vSysid;
    }

    @JsonProperty("vSyssubid")
    public Integer getVSyssubid() {
        return vSyssubid;
    }

    public void setVSyssubid(Integer vSyssubid) {
        this.vSyssubid = vSyssubid;
    }

    @JsonProperty("vSysvalue1")
    public String getVSysvalue1() {
        return vSysvalue1;
    }

    public void setVSysvalue1(String vSysvalue1) {
        this.vSysvalue1 = vSysvalue1;
    }

    @JsonProperty("vSysvalue2")
    public String getVSysvalue2() {
        return vSysvalue2;
    }

    public void setVSysvalue2(String vSysvalue2) {
        this.vSysvalue2 = vSysvalue2;
    }

    @JsonProperty("vSysvalue3")
    public String getVSysvalue3() {
        return vSysvalue3;
    }

    public void setVSysvalue3(String vSysvalue3) {
        this.vSysvalue3 = vSysvalue3;
    }

    @JsonProperty("vSysvalue4")
    public String getVSysvalue4() {
        return vSysvalue4;
    }

    public void setVSysvalue4(String vSysvalue4) {
        this.vSysvalue4 = vSysvalue4;
    }

    @JsonProperty("vSysvalue5")
    public String getVSysvalue5() {
        return vSysvalue5;
    }

    public void setVSysvalue5(String vSysvalue5) {
        this.vSysvalue5 = vSysvalue5;
    }

    @JsonProperty("vNameZhCn")
    public String getVNameZhCn() {
        return vNameZhCn;
    }

    public void setVNameZhCn(String vNameZhCn) {
        this.vNameZhCn = vNameZhCn;
    }

    @JsonProperty("iSortindex")
    public Integer getISortindex() {
        return iSortindex;
    }

    public void setISortindex(Integer iSortindex) {
        this.iSortindex = iSortindex;
    }

    @JsonProperty("vIsfixvalue")
    public String getVIsfixvalue() {
        return vIsfixvalue;
    }

    public void setVIsfixvalue(String vIsfixvalue) {
        this.vIsfixvalue = vIsfixvalue;
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