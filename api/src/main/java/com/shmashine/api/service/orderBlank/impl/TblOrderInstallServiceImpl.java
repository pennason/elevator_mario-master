package com.shmashine.api.service.orderBlank.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.api.dao.TblOrderInstallDao;
import com.shmashine.api.entity.TblOrderInstall;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.orderBlank.TblOrderInstallModule;
import com.shmashine.api.service.orderBlank.TblOrderInstallService;
import com.shmashine.common.constants.SystemConstants;

@Service
public class TblOrderInstallServiceImpl implements TblOrderInstallService {

    @Autowired
    private TblOrderInstallDao tblOrderInstallDao;

    @Override
    public List<TblOrderInstall> list() {
        return tblOrderInstallDao.list();
    }

    @Override
    public int insert(@Param("tblOrderInstall") TblOrderInstall tblOrderInstall) {
        return tblOrderInstallDao.insertRecord(tblOrderInstall);
    }

    @Override
    public int update(TblOrderInstall tblOrderInstall) {
        return tblOrderInstallDao.updateRecord(tblOrderInstall);
    }

    @Override
    public int delete(TblOrderInstall tblOrderInstall) {
        return tblOrderInstallDao.deleteRecord(tblOrderInstall);
    }

    @Override
    public TblOrderInstall detail(String tblOrderInstallId) {
        return tblOrderInstallDao.detail(tblOrderInstallId);
    }

    @Override
    public PageListResultEntity search(@Param("tblOrderInstallModule") TblOrderInstallModule tblOrderInstallModule) {
        Integer pageIndex = tblOrderInstallModule.getPageIndex();
        Integer pageSize = tblOrderInstallModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;

        }

        PageHelper.startPage(pageIndex, pageSize);

        PageInfo<Map<String, Object>> hashMapPageInfo = new PageInfo<>(tblOrderInstallDao.search(tblOrderInstallModule), pageSize);

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(), hashMapPageInfo.getList());
    }
}
