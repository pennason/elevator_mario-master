package com.shmashine.api.module.system;

import javax.validation.constraints.NotNull;

/**
 * @PackgeName: com.shmashine.api.module.system
 * @ClassName: GetSelectModule
 * @Date: 2020/6/1013:02
 * @Author: LiuLiFu
 * @Description: 获取系统下拉框数据模型
 */
public class GetSelectModule {
    /**
     * 主ID
     */
    @NotNull(message = "字典主编号不能为空")
    private String mainId;

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }
}
