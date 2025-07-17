package com.shmashine.api.module.system;

import javax.validation.constraints.NotNull;

/**
 * @PackgeName: com.shmashine.api.module.system
 * @ClassName: GetAreaModule
 * @Date: 2020/6/1214:09
 * @Author: LiuLiFu
 * @Description: 省市区编码参数类
 */
public class GetAreaModule {

    @NotNull(message = "地区码，不能为空")
    private String areaId;


    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

}
