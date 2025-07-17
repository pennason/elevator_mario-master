package com.shmashine.api.module.dataAccount;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shmashine.common.entity.base.PageListParams;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 获取数据账户列表
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SearchDataAccountModule extends PageListParams {

    private String vDataAccountId;

    private String vAccountName;

    private String vAccountCode;

    /**
     * 推送地址
     **/
    private String vPushUrl;

    /**
     * 延迟时间（单位小时）
     **/
    private Integer iDelayDuration;

    private Integer iDelFlag;

    private ArrayList<String> permissionDeptIds;

    public ArrayList<String> getPermissionDeptIds() {
        return permissionDeptIds;
    }

    public void setPermissionDeptIds(ArrayList<String> permissionDeptIds) {
        this.permissionDeptIds = permissionDeptIds;
    }

    @JsonProperty("vDataAccountId")
    public String getvDataAccountId() {
        return vDataAccountId;
    }

    public void setvDataAccountId(String vDataAccountId) {
        this.vDataAccountId = vDataAccountId;
    }

    @JsonProperty("vAccountName")
    public String getvAccountName() {
        return vAccountName;
    }

    public void setvAccountName(String vAccountName) {
        this.vAccountName = vAccountName;
    }

    @JsonProperty("vAccountCode")
    public String getvAccountCode() {
        return vAccountCode;
    }

    public void setvAccountCode(String vAccountCode) {
        this.vAccountCode = vAccountCode;
    }

    @JsonProperty("vPushUrl")
    public String getvPushUrl() {
        return vPushUrl;
    }

    public void setvPushUrl(String vPushUrl) {
        this.vPushUrl = vPushUrl;
    }

    @JsonProperty("iDelayDuration")
    public Integer getiDelayDuration() {
        return iDelayDuration;
    }

    public void setiDelayDuration(Integer iDelayDuration) {
        this.iDelayDuration = iDelayDuration;
    }

    @JsonProperty("iDelFlag")
    public Integer getiDelFlag() {
        return iDelFlag;
    }

    public void setiDelFlag(Integer iDelFlag) {
        this.iDelFlag = iDelFlag;
    }
}
