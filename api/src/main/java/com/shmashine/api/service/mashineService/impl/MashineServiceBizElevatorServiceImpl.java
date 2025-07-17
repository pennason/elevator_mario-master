package com.shmashine.api.service.mashineService.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.api.dao.MashineServiceBizElevatorDao;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.mashineService.MashineServiceBizElevatorModule;
import com.shmashine.api.service.mashineService.MashineServiceBizElevatorService;
import com.shmashine.common.constants.SystemConstants;

@Service
public class MashineServiceBizElevatorServiceImpl implements MashineServiceBizElevatorService {

    @Autowired
    private MashineServiceBizElevatorDao mashineServiceBizElevatorDao;

    @Override
    public PageListResultEntity list(MashineServiceBizElevatorModule mashineServiceBizElevatorModule) {
        Integer pageIndex = mashineServiceBizElevatorModule.getPageIndex();
        Integer pageSize = mashineServiceBizElevatorModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;

        }

        PageHelper.startPage(pageIndex, pageSize);

        PageInfo<Map<String, Object>> hashMapPageInfo = new PageInfo<>(mashineServiceBizElevatorDao.list(mashineServiceBizElevatorModule), pageSize);

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(), hashMapPageInfo.getList());
    }
}
