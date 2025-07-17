package com.shmashine.pm.api.service.investigateTask.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.common.utils.CryptoUtil;
import com.shmashine.pm.api.contants.SystemConstants;
import com.shmashine.pm.api.dao.BizInvestigateTaskDao;
import com.shmashine.pm.api.entity.TblInvestigateTask;
import com.shmashine.pm.api.entity.base.PageListResultEntity;
import com.shmashine.pm.api.module.investigateTask.input.SearchInvestigateTaskListModule;
import com.shmashine.pm.api.module.investigateTask.input.SearchInvestigateTaskSelectListModule;
import com.shmashine.pm.api.service.investigateTask.BizInvestigateTaskService;

@Service
public class BizInvestigateTaskServiceImpl implements BizInvestigateTaskService {


    @Autowired
    BizInvestigateTaskDao bizInvestigateTaskDao;

    /**
     * 任务列表
     *
     * @param searchInvestigateTaskListModule
     * @return
     */
    @Override
    public PageListResultEntity<Map> searchInvestigateTaskList(SearchInvestigateTaskListModule searchInvestigateTaskListModule) {
        Integer pageIndex = searchInvestigateTaskListModule.getPageIndex();
        Integer pageSize = searchInvestigateTaskListModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Map> pageInfo = new PageInfo<>(bizInvestigateTaskDao
                .searchInvestigateTaskList(searchInvestigateTaskListModule), pageSize);

        List<Map> collect = pageInfo.getList().stream().map(map -> {
            String principalName = (String) map.get("v_principal_name");
            map.put("v_principal_name", CryptoUtil.decryptAesBase64("vName", String.valueOf(principalName)));
            return map;
        }).collect(Collectors.toList());

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) pageInfo.getTotal(), collect);
    }

    /**
     * 获取所有任务结果
     */
    @Override
    public List<Map> searchAllInvestigateTask(SearchInvestigateTaskListModule searchInvestigateTaskListModule) {

        return bizInvestigateTaskDao.searchInvestigateTaskList(searchInvestigateTaskListModule);

    }


    /**
     * 任务下拉框
     */
    @Override
    public List<Map> searchInvestigateTaskSelectList(SearchInvestigateTaskSelectListModule
                                                             searchInvestigateTaskSelectListModule) {
        return bizInvestigateTaskDao.searchInvestigateTaskSelectList(searchInvestigateTaskSelectListModule);
    }

    @Override
    public TblInvestigateTask getByVillageId(@Param("vVillageId") String vVillage) {
        return bizInvestigateTaskDao.getByVillageId(vVillage);
    }

    @Override
    public HashMap getRelativeInfo(@Param("vInvestigateTaskId") String vInvestigateTaskId) {
        return bizInvestigateTaskDao.getRelativeInfo(vInvestigateTaskId);
    }

    @Override
    public Map getBizVillageInfo(String vInvestigateTaskId) {
        return bizInvestigateTaskDao.getBizVillageInfo(vInvestigateTaskId);
    }

    @Override
    public List<Integer> getAllStatus(SearchInvestigateTaskListModule searchInvestigateTaskListModule) {
        return bizInvestigateTaskDao.getAllStatus(searchInvestigateTaskListModule);
    }


}
