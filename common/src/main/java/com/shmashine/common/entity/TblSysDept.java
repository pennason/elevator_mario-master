package com.shmashine.common.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 部门表(TblSysDept)实体类
 *
 * @author makejava
 * @since 2020-05-20 17:23:33
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TblSysDept implements Serializable {
    @Serial
    private static final long serialVersionUID = -39110412924169592L;
    /**
     * 上级部门ID，一级部门为空
     */
    private String vDeptId;

    @NotNull(message = "父级编码不能为空")
    /**上级部门ID，一级部门为空*/
    private String vParentId;

    @NotNull(message = "部门类型不能为空")
    /**部门类型*/
    private Integer iDeptTypeId;
    /**
     * 部门类型 名称
     */
    private String typeName;

    @Length(min = 1, max = 200, message = "部门名称不能超过200位字符且不能小于1位")
    /**部门名称*/
    private String vDeptName;

    /**
     * 排序
     */
    private Integer iOrderNum;
    /**
     * 0已删除，1正常 默认正常
     */
    private Integer iStatus;
    /**
     * 传真
     */
    private String vFax;
    /**
     * 手机
     */
    private String vMobilephone;
    /**
     * 座机
     */
    private String vTell;
    /**
     * 省市区id
     */
    private Integer vProvinceId;
    /**
     * 部门详细地址
     */
    private String vAddress;
    /**
     * 备注信息
     */
    private String vRemarks;
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

    private String vContacts;

    private Integer iWuyePlatform;

    /**
     * 部门水平 1 客户 2 部门
     */
    private Integer iLevel;

    /**
     * 是否是外协部门
     */
    private Boolean iOutService;

    @JsonProperty("vDeptId")
    public String getVDeptId() {
        return vDeptId;
    }

    public void setVDeptId(String vDeptId) {
        this.vDeptId = vDeptId;
    }

    @JsonProperty("vParentId")
    public String getVParentId() {
        return vParentId;
    }

    public void setVParentId(String vParentId) {
        this.vParentId = vParentId;
    }

    @JsonProperty("iDeptTypeId")
    public Integer getIDeptTypeId() {
        return iDeptTypeId;
    }

    public void setIDeptTypeId(Integer iDeptTypeId) {
        this.iDeptTypeId = iDeptTypeId;
    }

    @JsonProperty("vDeptName")
    public String getVDeptName() {
        return vDeptName;
    }

    public void setVDeptName(String vDeptName) {
        this.vDeptName = vDeptName;
    }

    @JsonProperty("iOrderNum")
    public Integer getIOrderNum() {
        return iOrderNum;
    }

    public void setIOrderNum(Integer iOrderNum) {
        this.iOrderNum = iOrderNum;
    }

    @JsonProperty("iStatus")
    public Integer getIStatus() {
        return iStatus;
    }

    public void setIStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    @JsonProperty("vFax")
    public String getVFax() {
        return vFax;
    }

    public void setVFax(String vFax) {
        this.vFax = vFax;
    }

    @JsonProperty("vMobilephone")
    public String getVMobilephone() {
        return vMobilephone;
    }

    public void setVMobilephone(String vMobilephone) {
        this.vMobilephone = vMobilephone;
    }

    @JsonProperty("vTell")
    public String getVTell() {
        return vTell;
    }

    public void setVTell(String vTell) {
        this.vTell = vTell;
    }

    @JsonProperty("vProvinceId")
    public Integer getVProvinceId() {
        return vProvinceId;
    }

    public void setVProvinceId(Integer vProvinceId) {
        this.vProvinceId = vProvinceId;
    }

    @JsonProperty("vAddress")
    public String getVAddress() {
        return vAddress;
    }

    public void setVAddress(String vAddress) {
        this.vAddress = vAddress;
    }

    @JsonProperty("vRemarks")
    public String getVRemarks() {
        return vRemarks;
    }

    public void setVRemarks(String vRemarks) {
        this.vRemarks = vRemarks;
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

    @JsonProperty("typeName")
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @JsonProperty("vContacts")
    public String getvContacts() {
        return vContacts;
    }

    public void setvContacts(String vContacts) {
        this.vContacts = vContacts;
    }

    @JsonProperty("iWuyePlatform")
    public Integer getiWuyePlatform() {
        return iWuyePlatform;
    }

    public void setiWuyePlatform(Integer iWuyePlatform) {
        this.iWuyePlatform = iWuyePlatform;
    }

    @JsonProperty("iLevel")
    public Integer getiLevel() {
        return iLevel;
    }

    public void setiLevel(Integer iLevel) {
        this.iLevel = iLevel;
    }

    @JsonProperty("iOutService")
    public Boolean getiOutService() {
        return iOutService;
    }

    public void setiOutService(Boolean iOutService) {
        this.iOutService = iOutService;
    }
}