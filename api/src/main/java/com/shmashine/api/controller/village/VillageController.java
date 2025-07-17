package com.shmashine.api.controller.village;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.shmashine.api.config.exception.BizException;
import com.shmashine.api.controller.village.vo.VillagesAndPermissionStatusReqVO;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.village.input.SearchVillaListModule;
import com.shmashine.api.module.village.input.SearchVillaSelectListModule;
import com.shmashine.api.service.dept.BizDeptService;
import com.shmashine.api.service.elevatorproject.BizProjectService;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.api.service.village.BizVillageService;
import com.shmashine.api.service.village.TblVillageServiceI;
import com.shmashine.common.entity.TblVillage;
import com.shmashine.common.utils.SnowFlakeUtils;

/**
 * 小区接口
 */
@RequestMapping("village")
@RestController
public class VillageController extends BaseRequestEntity {


    private BizVillageService bizVillageService;
    private BizUserService bizUserService;
    private BizDeptService bizDeptService;
    private BizProjectService bizProjectService;
    private TblVillageServiceI tblVillageServiceI;

    @Autowired
    public VillageController(BizVillageService bizVillageService, BizUserService bizUserService, BizDeptService bizDeptService, BizProjectService bizProjectService, TblVillageServiceI tblVillageServiceI) {
        this.bizVillageService = bizVillageService;
        this.bizUserService = bizUserService;
        this.bizDeptService = bizDeptService;
        this.bizProjectService = bizProjectService;
        this.tblVillageServiceI = tblVillageServiceI;
    }

    /**
     * 获取小区列表
     *
     * @return
     */
    @PostMapping("/searchVillageList")
    public Object searchVillageList(@RequestBody SearchVillaListModule searchVillaSelectListModule) {
        //1. 查找有权查看的项目
        // 1. 递归 查找 部门id list
        // 1.1 递归找到有权查看的的部门编号
        JSONObject userDept = bizDeptService.getUserDept(super.getUserId());
        String dept_id = userDept.getString("dept_id");
        // 2. 找到当前用户所属部门编号 查询 所有子部门 及 当前部门 list 准备查找项目参数
        List<String> results = Lists.newArrayList();
        results.add(dept_id);
        recursion(dept_id, results);
        // 4. 查找小区 级联找
        searchVillaSelectListModule.setPermissionDeptIds((ArrayList<String>) results);
        return ResponseResult.successObj(bizVillageService.searchVillageList(searchVillaSelectListModule));
    }

    /**
     * 根据项目获取小区下拉框
     *
     * @return
     */
    @PostMapping("/searchVillageSelectList")
    public Object searchVillageSelectList(@RequestBody @Valid SearchVillaSelectListModule searchVillaSelectListModule) {
        searchVillaSelectListModule.setUserId(super.getUserId());
        searchVillaSelectListModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        return ResponseResult.successObj(bizVillageService.searchVillageSelectList(searchVillaSelectListModule));
    }

    /**
     * 根据项目获取小区下拉框并返回授权状态-电梯授权页面
     *
     * @return
     */
    @PostMapping("/searchVillagesAndPermissionStatusByProject")
    public Object searchVillagesAndPermissionStatusByProject(@RequestBody @Valid VillagesAndPermissionStatusReqVO villagesReqVO) {
        villagesReqVO.setUserId(super.getUserId());
        villagesReqVO.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        return ResponseResult.successObj(bizVillageService.searchVillagesByProject(villagesReqVO));
    }

    /**
     * 获取所有小区结果
     *
     * @return
     */
    @PostMapping("searchAllVillage")
    public ResponseResult searchAllVillage() {
        //1. 查找有权查看的项目
        // 1. 递归 查找 部门id list
        // 1.1 递归找到有权查看的的部门编号
        JSONObject userDept = bizDeptService.getUserDept(super.getUserId());
        String dept_id = userDept.getString("dept_id");
        // 2. 找到当前用户所属部门编号 查询 所有子部门 及 当前部门 list 准备查找项目参数
        List<String> results = Lists.newArrayList();
        results.add(dept_id);
        recursion(dept_id, results);
        // 4. 查找小区 级联找
        SearchVillaListModule searchVillaListModule = new SearchVillaListModule();
        searchVillaListModule.setPermissionDeptIds((ArrayList<String>) results);
        searchVillaListModule.setIDelFlag(0);
        return ResponseResult.successObj(bizVillageService.searchAllVillage(searchVillaListModule));
    }


    /**
     * 递归查询 下级部门的编号
     *
     * @param dept_id
     * @param strings
     */
    public void recursion(String dept_id, List<String> strings) {

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

    /**
     * 查看小区
     *
     * @return
     */
    @PostMapping("/getVillageInfo")
    public Object getVillageInfo(@RequestBody @Valid @NotNull(message = "请输入小区Id") String villageId) {
        return ResponseResult.successObj(tblVillageServiceI.getById(villageId));
    }

    /**
     * 添加小区
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/insertVillageInfo")
    public Object insertVillageInfo(@RequestBody TblVillage tblVillage) {
        String villageId = SnowFlakeUtils.nextStrId();
        tblVillage.setVVillageId(villageId);
        tblVillage.setIDelFlag(0);

        int insert = tblVillageServiceI.insert(tblVillage);
        if (insert > 0) {
            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error());
        }
    }

    /**
     * 编辑小区
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/editVillageInfo")
    public Object editVillageInfo(@RequestBody TblVillage tblVillage) {
        int update = tblVillageServiceI.update(tblVillage);
        if (update > 0) {
            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error());
        }
    }

    /**
     * 删除小区
     *
     * @return
     */
    @PostMapping("/deleteVillageInfo")
    public Object deleteVillageInfo(@RequestBody @Valid @NotNull(message = "请输入小区Id") String villageId) {
        TblVillage tblVillage = new TblVillage();
        tblVillage.setIDelFlag(1);
        tblVillage.setVVillageId(villageId);
        int update = tblVillageServiceI.update(tblVillage);
        if (update > 0) {
            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error());
        }
    }

}
