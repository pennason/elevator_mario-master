package com.shmashine.api.service.workOrder;


import java.util.List;
import java.util.Map;

import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.workOrder.ResponseMaintenanceDetailModule;
import com.shmashine.api.module.workOrder.SearchAgencyWorkOrderListModule;
import com.shmashine.common.entity.TblSysSysteminfo;

/**
 * (TblWorkOrder)表服务接口
 *
 * @author little.li
 * @since 2020-06-17 21:36:52
 */
public interface BizWorkOrderService {

    Map<String, Object> getHandlePower(Integer handleStatus);

    /**
     * 获取办工单列表
     */
    PageListResultEntity searchAgencyWorkOrderList(SearchAgencyWorkOrderListModule searchAgencyWorkOrderListModule);

    List<TblSysSysteminfo> getMaintenanceType();

    List<ResponseMaintenanceDetailModule> getMaintenanceDetail(String workOrderId);

    /**
     * 根据电梯注册码获取电梯name
     *
     * @param registerCode
     * @return
     */
    String getEleNameByRegisterCode(String registerCode);

    /**
     * 急修工单删除
     *
     * @param userId      用户id
     * @param eventNumber 事件编号
     * @return
     */
    String delEventOrderById(String userId, String eventNumber);
}