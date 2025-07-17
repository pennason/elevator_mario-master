package com.shmashine.api.service.orderBlank.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.api.dao.TblOrderBlankDao;
import com.shmashine.api.entity.TblOrderBlank;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.orderBlank.TblOrderBlankModule;
import com.shmashine.api.service.orderBlank.TblOrderBlankService;
import com.shmashine.common.constants.SystemConstants;

@Service
public class TblOrderBlankServiceImpl implements TblOrderBlankService {

    @Autowired
    private TblOrderBlankDao tblOrderBlankDao;

    @Override
    public List<TblOrderBlank> list() {
        return tblOrderBlankDao.list();
    }

    @Override
    public int insert(@Param("tblOrderBlank") TblOrderBlank tblOrderBlank) {
        return tblOrderBlankDao.insertRecord(tblOrderBlank);
    }

    @Override
    public int update(TblOrderBlank tblOrderBlank) {
        return tblOrderBlankDao.updateRecord(tblOrderBlank);
    }

    @Override
    public int delete(TblOrderBlank tblOrderBlank) {
        return tblOrderBlankDao.deleteRecord(tblOrderBlank);
    }

    @Override
    public TblOrderBlank detail(String tblOrderBlankId) {
        return tblOrderBlankDao.detail(tblOrderBlankId);
    }

    @Override
    public PageListResultEntity search(@Param("tblOrderBlankModule") TblOrderBlankModule tblOrderBlankModule) {
        Integer pageIndex = tblOrderBlankModule.getPageIndex();
        Integer pageSize = tblOrderBlankModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;

        }

        PageHelper.startPage(pageIndex, pageSize);

        PageInfo<Map<String, Object>> hashMapPageInfo = new PageInfo<>(tblOrderBlankDao.search(tblOrderBlankModule), pageSize);

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(), hashMapPageInfo.getList());
    }
}
