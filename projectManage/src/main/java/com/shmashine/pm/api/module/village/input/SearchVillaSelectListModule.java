package com.shmashine.pm.api.module.village.input;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SearchVillaSelectListModule implements Serializable {


    /**
     * 项目id集合  在这之前项目是有权限的
     */
    @NotNull(message = "请输入项目Id")
    private String projectId;

    /*public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }*/
}
