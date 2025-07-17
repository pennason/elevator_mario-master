package com.shmashine.api.service.mashineService.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.api.dao.MashineServiceBizMaintenanceDao;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.mashineService.MashineServiceBizMaintenanceModule;
import com.shmashine.api.service.mashineService.MashineServiceBizMainteanceService;
import com.shmashine.common.constants.SystemConstants;

@Service
public class MashineServiceBizMaintenanceServiceImpl implements MashineServiceBizMainteanceService {

    @Autowired
    private MashineServiceBizMaintenanceDao mashineServiceBizMaintenanceDao;

    @Override
    public PageListResultEntity list(MashineServiceBizMaintenanceModule mashineServiceBizMaintenanceModule) {
        Integer pageIndex = mashineServiceBizMaintenanceModule.getPageIndex();
        Integer pageSize = mashineServiceBizMaintenanceModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;

        }

        PageHelper.startPage(pageIndex, pageSize);

        PageInfo<Map<String, Object>> hashMapPageInfo = new PageInfo<>(mashineServiceBizMaintenanceDao.list(mashineServiceBizMaintenanceModule), pageSize);

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(), hashMapPageInfo.getList());
    }
}
