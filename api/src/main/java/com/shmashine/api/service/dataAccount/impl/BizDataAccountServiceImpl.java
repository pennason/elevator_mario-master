package com.shmashine.api.service.dataAccount.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.api.dao.BizDataAccountDao;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.dataAccount.SearchDataAccountModule;
import com.shmashine.api.service.dataAccount.BizDataAccountService;
import com.shmashine.common.constants.SystemConstants;

@Service
public class BizDataAccountServiceImpl implements BizDataAccountService {

    @Autowired
    private BizDataAccountDao bizDataAccountDao;

    @Override
    public PageListResultEntity<Map> search(SearchDataAccountModule searchDataAccountModule) {
        Integer pageIndex = searchDataAccountModule.getPageIndex();
        Integer pageSize = searchDataAccountModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Map> tPageInfo = new PageInfo<>(bizDataAccountDao.search(searchDataAccountModule), pageSize);

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) tPageInfo.getTotal(), tPageInfo.getList());
    }
}
