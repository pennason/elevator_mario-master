package com.shmashine.pm.api.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TblSysDeptUser implements Serializable {

    private static final long serialVersionUID = 8968764546789038061L;

    private String vDeptId;
    private String vUserId;
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

    @JsonProperty("vDeptId")
    public String getVDeptId() {
        return vDeptId;
    }

    public void setVDeptId(String vDeptId) {
        this.vDeptId = vDeptId;
    }

    @JsonProperty("vUserId")
    public String getVUserId() {
        return vUserId;
    }

    public void setVUserId(String vUserId) {
        this.vUserId = vUserId;
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
