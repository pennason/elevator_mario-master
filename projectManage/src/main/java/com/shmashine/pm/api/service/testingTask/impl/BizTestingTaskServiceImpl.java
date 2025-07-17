package com.shmashine.pm.api.service.testingTask.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.common.utils.CryptoUtil;
import com.shmashine.pm.api.contants.SystemConstants;
import com.shmashine.pm.api.dao.BizTestingTaskDao;
import com.shmashine.pm.api.entity.TblTestingTask;
import com.shmashine.pm.api.entity.base.PageListResultEntity;
import com.shmashine.pm.api.entity.dto.TblTestingTaskDto;
import com.shmashine.pm.api.module.testingTask.input.SearchTestingTaskListModule;
import com.shmashine.pm.api.service.testingTask.BizTestingTaskService;

@Service
public class BizTestingTaskServiceImpl implements BizTestingTaskService {

    @Autowired
    private BizTestingTaskDao bizTestingTaskDao;

    @Override
    public PageListResultEntity<Map> searchTestingTaskList(SearchTestingTaskListModule searchTestingTaskListModule) {
        Integer pageIndex = searchTestingTaskListModule.getPageIndex();
        Integer pageSize = searchTestingTaskListModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Map> pageInfo = new PageInfo<>(bizTestingTaskDao
                .searchTestingTaskList(searchTestingTaskListModule), pageSize);

        List<Map> collect = pageInfo.getList().stream().map(map -> {
            String principalName = (String) map.get("v_principal_name");
            String principalMobile = (String) map.get("v_principal_mobile");
            map.put("v_principal_name", CryptoUtil.decryptAesBase64("vName", String.valueOf(principalName)));
            map.put("v_principal_mobile", CryptoUtil.decryptAesBase64("vMobile", String.valueOf(principalMobile)));
            return map;
        }).collect(Collectors.toList());

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) pageInfo.getTotal(), collect);
    }

    @Override
    public List<Map> searchAllTestingTask(SearchTestingTaskListModule searchTestingTaskListModule) {
        return null;
    }

    @Override
    public TblTestingTask getByTestingTaskId(String vTestingTaskId) {
        return null;
    }

    @Override
    public TblTestingTaskDto getTaskDetail(String vTestingTaskId) {
        return bizTestingTaskDao.getTaskDetail(vTestingTaskId);
    }

    @Override
    public List<Map> getElevatorsInfo(String vTestingTaskId) {
        return bizTestingTaskDao.getElevatorsInfo(vTestingTaskId);
    }
}
