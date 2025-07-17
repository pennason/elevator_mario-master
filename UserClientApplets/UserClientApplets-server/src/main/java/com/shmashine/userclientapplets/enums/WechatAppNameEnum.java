package com.shmashine.userclientapplets.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 微信用户appName字段枚举枚举
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/4/15 9:50
 * @Since: 1.0.0
 */
@AllArgsConstructor
public enum WechatAppNameEnum {

    MAN_SEN("mansen", "曼森物业管家"),
    PM("pm", "工程运维"),
    PROPERTY("property", "麦信物业"),
    WEI_BAO("weibao", "麦信维保助手"),
    WU_YE("wuye", "麦信物业管家"),
    GONG_ZHONG_HAO("gongzhonghao", "公众号");

    /**
     * 消息类型 TY
     */
    @Getter
    private final String type;

    /**
     * 消息子类型 ST
     */
    @Getter
    private final String name;
}
