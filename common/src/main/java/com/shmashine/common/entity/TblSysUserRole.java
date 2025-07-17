package com.shmashine.common.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 用户与角色对应表(TblSysUserRole)实体类
 *
 * @author makejava
 * @since 2020-05-20 14:25:53
 */
public class TblSysUserRole implements Serializable {
    private static final long serialVersionUID = -80003001414238786L;
    /**
     * 用户ID
     */
    private String vUserId;
    /**
     * 角色ID
     */
    private String vRoleId;
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

    @JsonProperty("vUserId")
    public String getVUserId() {
        return vUserId;
    }

    public void setVUserId(String vUserId) {
        this.vUserId = vUserId;
    }

    @JsonProperty("vRoleId")
    public String getVRoleId() {
        return vRoleId;
    }

    public void setVRoleId(String vRoleId) {
        this.vRoleId = vRoleId;
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