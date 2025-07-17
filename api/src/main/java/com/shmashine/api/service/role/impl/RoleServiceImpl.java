package com.shmashine.api.service.role.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.api.dao.BizRoleDao;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.role.input.SearchRoleListModule;
import com.shmashine.api.service.role.RoleService;
import com.shmashine.common.constants.SystemConstants;
import com.shmashine.common.entity.TblSysRole;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private BizRoleDao bizRoleDao;

    @Override
    public PageListResultEntity<TblSysRole> searchRoleList(SearchRoleListModule searchRoleListModule) {
        Integer pageIndex = searchRoleListModule.getPageIndex();
        Integer pageSize = searchRoleListModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数;
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<TblSysRole> pageInfo = new PageInfo<>(bizRoleDao.searchRoleList(searchRoleListModule), pageSize);
        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) pageInfo.getTotal(), pageInfo.getList());

    }

    @Override
    public TblSysRole getRoleDetail(String roleId) {
        return bizRoleDao.getRoleDetail(roleId);
    }

    @Override
    public PageListResultEntity<TblSysRole> searchRoleByvRoleId(SearchRoleListModule searchRoleListModule) {

        Integer pageIndex = searchRoleListModule.getPageIndex();
        Integer pageSize = searchRoleListModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数;
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<TblSysRole> pageInfo = null;

        if ("ROLE0000000000000001".equals(searchRoleListModule.getvRoleId())) {
            pageInfo = new PageInfo<>(bizRoleDao.searchRoleList(new SearchRoleListModule()), pageSize);
        } else {  //普通用户只为本身角色和普通用户，维保工
            List<String> roleIds = new ArrayList<>();
            //本身角色
            roleIds.add(searchRoleListModule.getvRoleId());
            //物业普通使用者
            roleIds.add("8121974556536406016");
            //物业电梯维保人员
            roleIds.add("8121975029326741504");
            pageInfo = new PageInfo<>(bizRoleDao.searchByRoleIds(roleIds), pageSize);
        }


        return new PageListResultEntity<>(pageIndex, pageSize, (int) pageInfo.getTotal(), pageInfo.getList());
    }
}
