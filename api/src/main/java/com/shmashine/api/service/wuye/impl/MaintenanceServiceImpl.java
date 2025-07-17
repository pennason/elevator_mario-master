package com.shmashine.api.service.wuye.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.api.dao.BizElevatorDao;
import com.shmashine.api.dao.BizRuiJinDao;
import com.shmashine.api.dao.WuyeMaintenanceDao;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.elevator.SearchElevatorModule;
import com.shmashine.api.module.fault.input.SearchFaultModule;
import com.shmashine.api.service.village.TblVillageServiceI;
import com.shmashine.api.service.wuye.MaintenanceService;
import com.shmashine.common.constants.SystemConstants;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    @Autowired
    private WuyeMaintenanceDao baseMapper;
    @Autowired
    private BizRuiJinDao bizRuiJinDao;
    @Autowired
    private BizElevatorDao bizElevatorDao;
    @Autowired
    private TblVillageServiceI tblVillageServiceI;

    @Override
    public HashMap<String, Object> getMaintenanceOverdueElevator(SearchElevatorModule searchElevatorModule) {
        return null;
    }

    @Override
    public HashMap<String, Object> getYearCheckOverdueElevator(SearchElevatorModule searchElevatorModule) {
        return null;
    }

    @Override
    public Integer getMaintenanceCount(SearchFaultModule searchFaultModule) {
        return baseMapper.getMaintenanceCount(searchFaultModule);
    }

    @Override
    public Integer getOverdueMaintenanceCount(SearchFaultModule searchFaultModule) {
        return baseMapper.getOverdueMaintenanceCount(searchFaultModule);
    }

    @Override
    public List<Map> getOverdueOrders(String elevatorId) {
        return baseMapper.getOverdueOrders(elevatorId);
    }

    /**
     * 获取维保记录列表
     *
     * @param searchFaultModule
     * @return
     */
    @Override
    public PageListResultEntity searchMaintenanceList(SearchFaultModule searchFaultModule) {

        Integer pageIndex = searchFaultModule.getPageIndex();
        Integer pageSize = searchFaultModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }

        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Map> mapPageInfo = new PageInfo<>(bizRuiJinDao.queryMaintenanceWorkOrdersList(searchFaultModule), pageSize);

        // 扩展小区信息
        tblVillageServiceI.extendVillageInfo(mapPageInfo.getList());

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) mapPageInfo.getTotal(), mapPageInfo.getList());

    }
}
