package com.shmashine.api.service.role;

import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.role.input.SearchRoleListModule;
import com.shmashine.common.entity.TblSysRole;

public interface RoleService {
    /**
     * 查询角色数据
     */
    PageListResultEntity<TblSysRole> searchRoleList(SearchRoleListModule searchRoleListModule);

    /**
     * 获取角色详情
     */
    TblSysRole getRoleDetail(String roleId);

    PageListResultEntity<TblSysRole> searchRoleByvRoleId(SearchRoleListModule searchRoleListModule);
}
