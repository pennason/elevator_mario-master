package com.shmashine.api.controller.maiXinMaintenancePlatform.vo;

import lombok.Data;

/**
 * @author  jiangheng
 * @version 2024/1/17 16:33
 * @description: com.shmashine.api.controller.maiXinMaintenancePlatform.vo
 */
@Data
public class MaintenanceItemDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * 序号
     */
    private Integer seqNo;

    /**
     * 设备类别(1:曳引梯 2:液压梯 3:杂物梯)
     */
    private Integer elevatorCategory;
    /**
     * 保养类型(1:半月保 2:季度保养 3:半年保养 4:全年保养)
     */
    private Integer type;
    /**
     * 维保项目内容
     */
    private String item;
    /**
     * 维保基本要求
     */
    private String content;
    /**
     * 检修位置(1:机房 2:轿顶 3:轿厢 4:层门 5:底坑井道)
     */
    private Integer location;
    /**
     * 默认状态(1:启用 2:禁用 3:屏蔽)
     */
    private Integer status;

}
