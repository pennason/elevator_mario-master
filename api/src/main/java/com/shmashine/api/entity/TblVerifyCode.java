package com.shmashine.api.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shmashine.common.constants.ServiceConstants;

public class TblVerifyCode implements Serializable {

    public static Integer LoginType = 1;
    public static Integer unused = 0;
    public static Integer used = 1;

    private static final long serialVersionUID = 1436128249747348668L;
    /**
     * code ID
     */
    private Long iCid;
    /**
     * 用户
     */
    private String vUserId;
    /**
     * 手机号
     */
    private String vMobile;
    /**
     * 状态
     */
    private Integer iStatus;
    /**
     * 类型
     */
    private Integer iType;
    /**
     * 过期时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date DtExpiredTime;
    /**
     * 新建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date DtCreateTime;
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date DtModifyTime;
    /**
     * 删除标记
     */
    private Integer iDelFlag;
    /**
     * code内容
     */
    private String vCode;

    public Long getiCid() {
        return iCid;
    }

    public void setiCid(Long iCid) {
        this.iCid = iCid;
    }

    public String getvUserId() {
        return vUserId;
    }

    public void setvUserId(String vUserId) {
        this.vUserId = vUserId;
    }

    public String getvMobile() {
        return vMobile;
    }

    public void setvMobile(String vMobile) {
        this.vMobile = vMobile;
    }

    public Integer getiStatus() {
        return iStatus;
    }

    public void setiStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    public Integer getiType() {
        return iType;
    }

    public void setiType(Integer iType) {
        this.iType = iType;
    }

    public Date getDtExpiredTime() {
        return DtExpiredTime;
    }

    public void setDtExpiredTime(Date dtExpiredTime) {
        DtExpiredTime = dtExpiredTime;
    }

    public Date getDtCreateTime() {
        return DtCreateTime;
    }

    public void setDtCreateTime(Date dtCreateTime) {
        DtCreateTime = dtCreateTime;
    }

    public Date getDtModifyTime() {
        return DtModifyTime;
    }

    public void setDtModifyTime(Date dtModifyTime) {
        DtModifyTime = dtModifyTime;
    }

    public Integer getiDelFlag() {
        return iDelFlag;
    }

    public void setiDelFlag(Integer iDelFlag) {
        this.iDelFlag = iDelFlag;
    }

    public String getvCode() {
        return vCode;
    }

    public void setvCode(String vCode) {
        this.vCode = vCode;
    }

    @Override
    public String toString() {
        return "TblVerifyCode{" +
                "iCid=" + iCid +
                ", vUserId='" + vUserId + '\'' +
                ", vMobile='" + vMobile + '\'' +
                ", iStatus=" + iStatus +
                ", iType=" + iType +
                ", DtExpiredTime=" + DtExpiredTime +
                ", DtCreateTime=" + DtCreateTime +
                ", DtModifyTime=" + DtModifyTime +
                ", iDelFlag=" + iDelFlag +
                ", vCode='" + vCode + '\'' +
                '}';
    }

    public TblVerifyCode() {
    }

    public TblVerifyCode(String vUserId, String vMobile, Integer iStatus, Integer iType, Date dtExpiredTime, Date dtCreateTime, Date dtModifyTime, Integer iDelFlag, String vCode) {
        this.vUserId = vUserId;
        this.vMobile = vMobile;
        this.iStatus = iStatus;
        this.iType = iType;
        this.DtExpiredTime = dtExpiredTime;
        this.DtCreateTime = dtCreateTime;
        this.DtModifyTime = dtModifyTime;
        this.iDelFlag = iDelFlag;
        this.vCode = vCode;
    }

    /**
     * 判度是否过期
     *
     * @return
     */
    public boolean isExpired() {
        Date now = new Date();

        if (this.getDtExpiredTime() != null) {
            return this.getDtExpiredTime().getTime() < now.getTime();
        } else {
            return (now.getTime() - ServiceConstants.LOGIN_VERIFY_CODE_EXPIRED_PEROID) > this.getDtCreateTime().getTime();
        }
    }

    /**
     * 是否有效
     *
     * @return
     */
    public boolean isUseful() {
        return this.getiStatus() == TblVerifyCode.unused;
    }
}
