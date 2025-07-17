package com.shmashine.api.controller.building;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.building.SearchBuildingModule;
import com.shmashine.api.service.building.BizBuildingService;
import com.shmashine.api.service.dept.BizDeptService;
import com.shmashine.api.service.user.BizUserService;

import lombok.extern.slf4j.Slf4j;

/**
 * 楼宇信息接口
 */
@RequestMapping("building")
@RestController
@Slf4j
public class BuildingController extends BaseRequestEntity {

    private BizDeptService bizDeptService;

    private BizUserService bizUserService;

    private BizBuildingService bizBuildingService;

    @Autowired
    public BuildingController(BizDeptService bizDeptService, BizBuildingService bizBuildingService, BizUserService bizUserService) {
        this.bizDeptService = bizDeptService;
        this.bizBuildingService = bizBuildingService;
        this.bizUserService = bizUserService;
    }

    /**
     * 查找楼宇列表
     *
     * @param searchBuildingModule
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("search")
    public Object search(@RequestBody SearchBuildingModule searchBuildingModule) {
        //1. 查找有权查看的项目
        // 1. 递归 查找 部门id list
        // 1.1 递归找到有权查看的的部门编号
        JSONObject userDept = bizDeptService.getUserDept(super.getUserId());
        String dept_id = userDept.getString("dept_id");
        // 2. 找到当前用户所属部门编号 查询 所有子部门 及 当前部门 list 准备查找项目参数
        List<String> results = Lists.newArrayList();
        results.add(dept_id);
        recursion(dept_id, results);
        // 4. 查找小区 级联找 看起来是有点lo
        searchBuildingModule.setPermissionDeptIds((ArrayList<String>) results);
        List<Map> search = bizBuildingService.search(searchBuildingModule);
        return ResponseResult.successObj(search);
    }

    /**
     * 递归查询 下级部门的编号
     *
     * @param dept_id
     * @param strings
     */
    private void recursion(String dept_id, List<String> strings) {

        if (null != dept_id) {
            List<String> userDeptIds = bizUserService.getUserDeptIds(dept_id);
            if (null != userDeptIds && userDeptIds.size() > 0) {
                userDeptIds.forEach(id -> {
                    recursion(id, strings);
                });
            }
            strings.addAll(userDeptIds);
        }
    }

}
