package com.shmashine.api.module.thirdparty.ruijin;

import javax.validation.constraints.NotNull;

/**
 * @PackgeName: com.shmashine.api.module.thirdparty.ruijin
 * @ClassName: RuiJinEventModule
 * @Date: 2020/7/2315:28
 * @Author: LiuLiFu
 * @Description: 特殊 接受类
 */
public class RuiJinEventModule {

    @NotNull(message = "请输入救援信息")
    private RuiJinEventReceiveModule value;

    public RuiJinEventReceiveModule getValue() {
        return value;
    }

    public void setValue(RuiJinEventReceiveModule value) {
        this.value = value;
    }
}
