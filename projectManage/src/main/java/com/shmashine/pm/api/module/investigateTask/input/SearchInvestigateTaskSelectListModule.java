package com.shmashine.pm.api.module.investigateTask.input;

import javax.validation.constraints.NotNull;

public class SearchInvestigateTaskSelectListModule {

    /**
     * 项目id集合  在这之前项目是有权限的
     */
    @NotNull(message = "请输入任务Id")
    private String investtigateTaskId;

    public String getInvesttigateTaskId() {
        return investtigateTaskId;
    }

    public void setInvesttigateTaskId(String investtigateTaskId) {
        this.investtigateTaskId = investtigateTaskId;
    }
}
