package com.shmashine.api.module.workOrder;

import java.util.Date;

import lombok.Data;

/**
 * 工单详情response实体类
 *
 * @author little.li
 */
@Data
public class ResponseWorkOrderDetailModule {

    /**
     * 工单详情id
     */
    private String workOrderDetailId;


    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 系统备注
     */
    private String systemRemarks;


    /**
     * 用户备注
     */
    private String remarks;


    /**
     * 图片url列表
     */
    private String imageList;


}
