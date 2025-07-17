package com.shmashine.api.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.api.module.workOrder.SearchAgencyWorkOrderListModule;

/**
 * (TblWorkOrder)表数据库访问层
 *
 * @author little.li
 * @since 2020-06-17 21:36:51
 */
@Mapper
public interface TblWorkOrderDao {

    /**
     * 获取代办工单列表
     *
     * @param searchAgencyWorkOrderListModule
     * @return
     */
    List<Map> searchAgencyWorkOrderList(SearchAgencyWorkOrderListModule searchAgencyWorkOrderListModule);

    /**
     * 获取工单处理权限
     */
    Map<String, Object> getWorkOrderHandlePower(@Param("handleStatus") Integer handleStatus);

    /**
     * 根据电梯注册码获取电梯name
     *
     * @param registerCode
     * @return
     */
    String getEleNameByRegisterCode(String registerCode);

    /**
     * 根据id获取急修工单
     *
     * @param eventNumber
     * @return
     */
    HashMap getEventOrderById(String eventNumber);

    /**
     * 删除急修工单
     *
     * @param eventNumber
     */
    void delEventOrderById(String eventNumber);

}