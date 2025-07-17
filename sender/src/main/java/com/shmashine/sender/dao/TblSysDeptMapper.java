// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.dao;

import org.apache.ibatis.annotations.Mapper;

import com.shmashine.common.entity.TblSysDept;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/23 15:52
 * @since v1.0
 */

@Mapper
public interface TblSysDeptMapper {

    /**
     * 根据部门ID获取维保公司信息
     *
     * @param deptId 部门ID
     * @return 维保公司信息
     */
    TblSysDept getMaintainCompanyById(String deptId);

    /**
     * 根据部门ID获取物业公司信息
     *
     * @param deptId 部门ID
     * @return 维保公司信息
     */
    TblSysDept getPropertyCompanyById(String deptId);
}
