package com.shmashine.api.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.module.dept.input.SearchDetpListModule;
import com.shmashine.common.entity.TblSysDept;

/**
 * 部门相关 Dao 层
 */
public interface BizDeptDao {

    /**
     * 通过部门编号获取子集部门 一层子集
     */
    List<TblSysDept> searchDept(@Param("searchDetpListModule") SearchDetpListModule searchDetpListModule);

    /**
     * 通过部门编号获取子集部门 下集
     */
    List<TblSysDept> searchDeptParent(@Param("searchDetpListModule") SearchDetpListModule searchDetpListModule);

    /**
     * 获取部门详情
     */
    HashMap getDeptDetail(@Param("deptId") String deptId);

    Map getDeptInfo(@Param("deptId") String deptId);

    /**
     * 通过部门编号获取子集部门 下集
     */
    List<String> getDeptIdsByParentDeptId(String parentDeptId);

    /**
     * 获取所有部门
     *
     * @return
     */
    List<TblSysDept> getAll(@Param("searchDetpListModule") SearchDetpListModule searchDetpListModule);

}
