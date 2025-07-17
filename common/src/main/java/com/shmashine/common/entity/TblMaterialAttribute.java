package com.shmashine.common.entity;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 物料属性表
 *
 * @author depp.yu
 * @since v1.0
 */

public class TblMaterialAttribute implements Serializable {

    @Serial
    private static final long serialVersionUID = -408572031494925653L;

    private String vMaterialAttributeId;

    private String vMaterialId;

    private String vMaterialAttributeContent;

    @JsonProperty("vMaterialAttributeId")
    public String getvMaterialAttributeId() {
        return vMaterialAttributeId;
    }

    public void setvMaterialAttributeId(String vMaterialAttributeId) {
        this.vMaterialAttributeId = vMaterialAttributeId;
    }

    @JsonProperty("vMaterialId")
    public String getvMaterialId() {
        return vMaterialId;
    }

    public void setvMaterialId(String vMaterialId) {
        this.vMaterialId = vMaterialId;
    }

    @JsonProperty("vMaterialAttributeContent")
    public String getvMaterialAttributeContent() {
        return vMaterialAttributeContent;
    }

    public void setvMaterialAttributeContent(String vMaterialAttributeContent) {
        this.vMaterialAttributeContent = vMaterialAttributeContent;
    }
}
