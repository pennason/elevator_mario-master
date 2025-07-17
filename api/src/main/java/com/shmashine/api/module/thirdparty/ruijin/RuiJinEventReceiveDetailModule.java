package com.shmashine.api.module.thirdparty.ruijin;

import java.util.Date;

import lombok.Data;

/**
 * @PackgeName: com.shmashine.api.module.thirdparty.ruijin
 * @ClassName: RuiJinEventReceiveDetailModule
 * @Date: 2020/7/2214:08
 * @Author: LiuLiFu
 * @Description: 瑞金医院 事件明细
 */
@Data
public class RuiJinEventReceiveDetailModule {
    /**
     * 事件编号
     */
    private String eventNumber;
    /**
     * 发生时间
     */
    private Date statusTime;
    /**
     * 状态
     */
    private String status;
    /**
     * 备注
     */
    private String comment;
    /**
     * 处理人
     */
    private String handler;
    /**
     * 处理人电话
     */
    private String handlerTel;
    /**
     * 救援单位名称
     */
    private String rescueCompanyName;

}
