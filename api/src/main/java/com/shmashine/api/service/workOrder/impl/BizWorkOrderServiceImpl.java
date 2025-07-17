package com.shmashine.api.service.workOrder.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.api.dao.TblElevatorDao;
import com.shmashine.api.dao.TblSysSysteminfoDao;
import com.shmashine.api.dao.TblWorkOrderDao;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.workOrder.ResponseMaintenanceDetailModule;
import com.shmashine.api.module.workOrder.SearchAgencyWorkOrderListModule;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.api.service.workOrder.BizWorkOrderService;
import com.shmashine.api.service.workOrder.TblWorkOrderMaintenanceDetailServiceI;
import com.shmashine.common.constants.SystemConstants;
import com.shmashine.common.entity.TblSysSysteminfo;

/**
 * (TblWorkOrder)工单表服务实现类
 *
 * @author little.li
 * @since 2020-06-17 21:36:53
 */
@Service("tblWorkOrderService")
public class BizWorkOrderServiceImpl implements BizWorkOrderService {

    Logger logger = LoggerFactory.getLogger("WorkOrderLogger");


    @Resource
    private TblWorkOrderDao tblWorkOrderDao;

    @Resource
    private TblWorkOrderMaintenanceDetailServiceI maintenanceDetailService;

    @Resource
    private BizUserService bizUserService;

    @Resource
    private TblElevatorDao elevatorDao;

    @Resource(type = TblSysSysteminfoDao.class)
    private TblSysSysteminfoDao sysSystemInfoDao;

    @Override
    public Map<String, Object> getHandlePower(Integer handleStatus) {

        return tblWorkOrderDao.getWorkOrderHandlePower(handleStatus);
    }

    @Override
    public PageListResultEntity searchAgencyWorkOrderList(SearchAgencyWorkOrderListModule searchAgencyWorkOrderListModule) {
        Integer pageIndex = searchAgencyWorkOrderListModule.getPageIndex();
        Integer pageSize = searchAgencyWorkOrderListModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);

        // 将项目IDS 转成 elevatorIds
        var elevatorIds = searchAgencyWorkOrderListModule.getElevatorIds();
        if (!CollectionUtils.isEmpty(searchAgencyWorkOrderListModule.getProjectIds())) {
            elevatorIds = elevatorDao.getElevatorIdsByProjectIds(searchAgencyWorkOrderListModule.getProjectIds());
            if (!CollectionUtils.isEmpty(elevatorIds)) {
                searchAgencyWorkOrderListModule.setElevatorIds(elevatorIds);
            }
        }

        PageInfo<Map<String, Object>> hashMapPageInfo = new PageInfo(tblWorkOrderDao.searchAgencyWorkOrderList(searchAgencyWorkOrderListModule), pageSize);
        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(), hashMapPageInfo.getList());
    }

    @Override
    public List<TblSysSysteminfo> getMaintenanceType() {
        return sysSystemInfoDao.getMaintenanceType();
    }

    @Override
    public List<ResponseMaintenanceDetailModule> getMaintenanceDetail(String workOrderId) {
        return maintenanceDetailService.getMaintenanceDetail(workOrderId);
    }

    @Override
    public String getEleNameByRegisterCode(String registerCode) {
        return tblWorkOrderDao.getEleNameByRegisterCode(registerCode);
    }

    @Override
    public String delEventOrderById(String userId, String eventNumber) {

        HashMap eventOrder = tblWorkOrderDao.getEventOrderById(eventNumber);

        if (eventOrder == null) {
            logger.info("----------用户{}，删除急修工单：{}----------", userId, "eventNumber:" + eventNumber + " 工单不存在");
            return "工单不存在";
        }

        tblWorkOrderDao.delEventOrderById(eventNumber);
        logger.info("----------用户{}，删除急修工单：{}----------", userId, eventOrder.toString());
        return "删除成功";
    }


}