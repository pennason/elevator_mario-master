package com.shmashine.pm.api.service.installingTask.impl;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.common.utils.CryptoUtil;
import com.shmashine.pm.api.contants.SystemConstants;
import com.shmashine.pm.api.dao.BizInstallingTaskDao;
import com.shmashine.pm.api.entity.TblInstallingTask;
import com.shmashine.pm.api.entity.base.PageListResultEntity;
import com.shmashine.pm.api.entity.dto.TblInstallingTaskDto;
import com.shmashine.pm.api.module.installingTask.input.SearchInstallingTaskListModule;
import com.shmashine.pm.api.module.installingTask.input.SearchInstallingTaskSelectListModule;
import com.shmashine.pm.api.service.installingTask.BizInstallingTaskService;

@Service
public class BizInstallingTaskServiceImpl implements BizInstallingTaskService {

    @Autowired
    private BizInstallingTaskDao bizInstallingTaskDao;

    @Override
    public List<Map> searchInstallingTaskSelectList(SearchInstallingTaskSelectListModule searchInstallingTaskSelectListModule) {
        return null;
    }

    @Override
    public PageListResultEntity<Map> searchInstallingTaskList(SearchInstallingTaskListModule searchInstallingTaskListModule) {
        Integer pageIndex = searchInstallingTaskListModule.getPageIndex();
        Integer pageSize = searchInstallingTaskListModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);

        PageInfo<Map> pageInfo = new PageInfo<>(bizInstallingTaskDao
                .searchInstallingTaskList(searchInstallingTaskListModule), pageSize);

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
    public List<Map> searchAllInstallingTask(SearchInstallingTaskListModule searchInstallingTaskListModule) {
        return null;
    }

    @Override
    public TblInstallingTask getByInstallingTaskId(String vInstallingTaskId) {
        return null;
    }

    @Override
    public TblInstallingTaskDto getTaskDetail(String vInstallingTaskId) {
        return bizInstallingTaskDao.getTaskDetail(vInstallingTaskId);
    }

    @Override
    public List<Map> getElevatorsInfo(String vInstallingTaskId) {
        return bizInstallingTaskDao.getElevatorsInfo(vInstallingTaskId);
    }

    @Override
    public List<Integer> getAllStatus(SearchInstallingTaskListModule searchInstallingTaskListModule) {
        return bizInstallingTaskDao.getAllStatus(searchInstallingTaskListModule);
    }
}
