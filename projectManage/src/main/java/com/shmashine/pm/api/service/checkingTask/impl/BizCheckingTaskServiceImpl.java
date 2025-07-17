package com.shmashine.pm.api.service.checkingTask.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.common.utils.CryptoUtil;
import com.shmashine.pm.api.contants.SystemConstants;
import com.shmashine.pm.api.dao.BizCheckingTaskDao;
import com.shmashine.pm.api.entity.TblCheckingTask;
import com.shmashine.pm.api.entity.base.PageListResultEntity;
import com.shmashine.pm.api.entity.dto.TblCheckingTaskDto;
import com.shmashine.pm.api.module.checkingTask.input.SearchCheckingTaskListModule;
import com.shmashine.pm.api.module.checkingTask.input.SearchCheckingTaskSelectListModule;
import com.shmashine.pm.api.service.checkingTask.BizCheckingTaskService;

@Service
public class BizCheckingTaskServiceImpl implements BizCheckingTaskService {

    @Autowired
    private BizCheckingTaskDao bizCheckingTaskDao;

    @Override
    public List<Map> searchCheckingTaskSelectList(SearchCheckingTaskSelectListModule searchCheckingTaskSelectListModule) {
        return null;
    }

    @Override
    public PageListResultEntity<Map> searchCheckingTaskList(SearchCheckingTaskListModule searchCheckingTaskListModule) {
        Integer pageIndex = searchCheckingTaskListModule.getPageIndex();
        Integer pageSize = searchCheckingTaskListModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Map> pageInfo = new PageInfo<>(bizCheckingTaskDao.searchCheckingTaskList(searchCheckingTaskListModule), pageSize);

        List<Map> collect = pageInfo.getList().stream().map(map -> {
            String principalName = (String) map.get("v_principal_name");
            map.put("v_principal_name", CryptoUtil.decryptAesBase64("vName", String.valueOf(principalName)));
            return map;
        }).collect(Collectors.toList());

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) pageInfo.getTotal(), collect);
    }

    @Override
    public List<Map> searchAllCheckingTask(SearchCheckingTaskListModule searchCheckingTaskListModule) {
        return null;
    }

    @Override
    public TblCheckingTask getByCheckingTaskId(String vCheckingTaskId) {
        return null;
    }

    @Override
    public TblCheckingTaskDto getTaskDetail(String vCheckingTaskId) {
        return bizCheckingTaskDao.getTaskDetail(vCheckingTaskId);
    }
}
