package com.shmashine.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.module.role.input.SearchRoleListModule;
import com.shmashine.common.entity.TblSysRole;

public interface BizRoleDao {

    /**
     * 查询角色列表
     */
    List<TblSysRole> searchRoleList(@Param("searchRoleListModule") SearchRoleListModule searchRoleListModule);

    /**
     * 获取角色详情
     */
    TblSysRole getRoleDetail(@Param("roleId") String roleId);

    List<TblSysRole> searchByRoleIds(@Param("roleIds") List<String> roleIds);
}
