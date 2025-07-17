package com.shmashine.api.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 配货单物料表单实体类
 *
 * @author depp
 */
public class TblOrderBlankForm implements Serializable {

    private static final long serialVersionUID = 3052021548957244689L;

    private String vOrderBlankFormId;

    private String vOrderBlankId;

    private String vMaterialName;

    private String vMaterialAttributeContent;

    private Integer iMaterialNum;

    private int iDelFlag;

    private int iPosition;

    private String vMaterialId;

    private String vMaterialAttributeId;

    @JsonProperty("vOrderBlankFormId")
    public String getvOrderBlankFormId() {
        return vOrderBlankFormId;
    }

    public void setvOrderBlankFormId(String vOrderBlankFormId) {
        this.vOrderBlankFormId = vOrderBlankFormId;
    }

    @JsonProperty("vOrderBlankId")
    public String getvOrderBlankId() {
        return vOrderBlankId;
    }

    public void setvOrderBlankId(String vOrderBlankId) {
        this.vOrderBlankId = vOrderBlankId;
    }

    @JsonProperty("vMaterialName")
    public String getvMaterialName() {
        return vMaterialName;
    }

    public void setvMaterialName(String vMaterialName) {
        this.vMaterialName = vMaterialName;
    }

    @JsonProperty("vMaterialAttributeContent")
    public String getvMaterialAttributeContent() {
        return vMaterialAttributeContent;
    }

    public void setvMaterialAttributeContent(String vMaterialAttributeContent) {
        this.vMaterialAttributeContent = vMaterialAttributeContent;
    }

    @JsonProperty("iMaterialNum")
    public Integer getiMaterialNum() {
        return iMaterialNum;
    }

    public void setiMaterialNum(Integer iMaterialNum) {
        this.iMaterialNum = iMaterialNum;
    }

    @JsonProperty("iDelFlag")
    public int getiDelFlag() {
        return iDelFlag;
    }

    public void setiDelFlag(int iDelFlag) {
        this.iDelFlag = iDelFlag;
    }

    @JsonProperty("iPosition")
    public int getiPosition() {
        return iPosition;
    }

    public void setiPosition(int iPosition) {
        this.iPosition = iPosition;
    }

    @JsonProperty("vMaterialId")
    public String getvMaterialId() {
        return vMaterialId;
    }

    public void setvMaterialId(String vMaterialId) {
        this.vMaterialId = vMaterialId;
    }

    @JsonProperty("vMaterialAttributeId")
    public String getvMaterialAttributeId() {
        return vMaterialAttributeId;
    }

    public void setvMaterialAttributeId(String vMaterialAttributeId) {
        this.vMaterialAttributeId = vMaterialAttributeId;
    }
}
