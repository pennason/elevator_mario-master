package com.shmashine.pm.api.service.distributionTask.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.pm.api.contants.SystemConstants;
import com.shmashine.pm.api.dao.BizDistributionTaskDao;
import com.shmashine.pm.api.entity.TblDistributionTask;
import com.shmashine.pm.api.entity.base.PageListResultEntity;
import com.shmashine.pm.api.module.distributionTask.input.SearchDistributionTaskListModule;
import com.shmashine.pm.api.service.distributionTask.BizDistributionTaskService;

@Service
public class BizDistributionTaskServiceImpl implements BizDistributionTaskService {

    @Autowired
    BizDistributionTaskDao bizDistributionTaskDao;

    @Override
    public PageListResultEntity<Map> searchDistributionTaskList(SearchDistributionTaskListModule
                                                                        searchDistributionTaskListModule) {

        Integer pageIndex = searchDistributionTaskListModule.getPageIndex();
        Integer pageSize = searchDistributionTaskListModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Map> pageInfo = new PageInfo<>(bizDistributionTaskDao
                .searchDistributionTaskList(searchDistributionTaskListModule), pageSize);

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public TblDistributionTask getByVillageId(String vVillageId) {
        return bizDistributionTaskDao.getByVillageId(vVillageId);
    }

    @Override
    public List<HashMap> getRelativeInfo(@Param("vDistributionTaskId") String vDistributionTaskId) {
        return bizDistributionTaskDao.getRelativeInfo(vDistributionTaskId);
    }

    @Override
    public Map getBizInfoById(@Param("vDistributionTaskId") String vDistributionTaskId) {
        return bizDistributionTaskDao.getBizInfoById(vDistributionTaskId);
    }

    @Override
    public Integer existsElevatorCode(@Param("elevatorCodes") List<String> elevatorCodes) {
        return bizDistributionTaskDao.existsElevatorCode(elevatorCodes);
    }
}
