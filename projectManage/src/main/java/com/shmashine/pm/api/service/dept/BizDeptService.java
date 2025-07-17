package com.shmashine.pm.api.service.dept;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.pm.api.entity.TblSysDept;
import com.shmashine.pm.api.entity.base.PageListResultEntity;
import com.shmashine.pm.api.module.dept.input.SearchDetpListModule;

public interface BizDeptService {
    /**
     * 查询部门列表
     */
    List<TblSysDept> searchDeptList(SearchDetpListModule searchDetpListModule);

    /**
     * 查询部门列表下级
     */
    PageListResultEntity<Map> searchDeptParent(SearchDetpListModule searchDetpListModule);

    /**
     * 获取用户信息
     */
    Map getDeptInfo(String deptId);

    /**
     * 获取用户所在部门
     */
    JSONObject getUserDept(String user_id);

    /**
     * 设置用户部门及部门的上级部门
     */
    TblSysDept settingDeptUserAndParent(TblSysDept tblSysDept);


    /**
     * 获取部门详情
     */
    Object getDeptDetail(String deptId);

    /**
     * 根据部门id获取所有子部门及自己
     *
     * @param deptId
     * @return
     */
    List<String> getAllSubsByDeptId(String deptId);

    /**
     * 判断是否处在小区名
     *
     * @param deptName
     * @return
     */
    int existsDeptByName(String deptName);
}
