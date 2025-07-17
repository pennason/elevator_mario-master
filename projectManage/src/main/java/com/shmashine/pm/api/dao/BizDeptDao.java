package com.shmashine.pm.api.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.shmashine.pm.api.entity.TblSysDept;
import com.shmashine.pm.api.module.dept.input.SearchDetpListModule;

public interface BizDeptDao {
    /**
     * 通过部门编号获取子集部门 一层子集
     */
    List<TblSysDept> searchDept(@Param("searchDetpListModule") SearchDetpListModule searchDetpListModule);

    /**
     * 通过部门编号获取子集部门 下集
     */
    List<Map> searchDeptParent(@Param("searchDetpListModule") SearchDetpListModule searchDetpListModule);

    /**
     * 获取部门详情
     */
    HashMap getDeptDetail(@Param("deptId") String deptId);

    Map getDeptInfo(@Param("deptId") String deptId);

    /**
     * 通过部门编号获取子集部门 下集
     */
    List<String> getDeptIdsByParentDeptId(String parentDeptId);

    int existsDeptByName(@Param("deptName") String deptName);
}
