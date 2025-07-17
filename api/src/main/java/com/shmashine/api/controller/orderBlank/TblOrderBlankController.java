package com.shmashine.api.controller.orderBlank;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.shmashine.api.config.exception.BizException;
import com.shmashine.api.controller.village.VillageController;
import com.shmashine.api.dao.TblOrderBlankFormDao;
import com.shmashine.api.dao.TblSysFileDao;
import com.shmashine.api.entity.TblOrderBlank;
import com.shmashine.api.entity.TblOrderBlankForm;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.orderBlank.TblOrderBlankModule;
import com.shmashine.api.service.dept.BizDeptService;
import com.shmashine.api.service.orderBlank.impl.TblOrderBlankServiceImpl;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.common.constants.BusinessConstants;
import com.shmashine.common.utils.SnowFlakeUtils;

@RestController
@Controller
@RequestMapping("/orderBlank")
public class TblOrderBlankController extends BaseRequestEntity {

    private static final Integer ISTATUS_ASSIGN_NO = 0;
    private static final Integer ISTATUS_ASSIGN_YES = 1;

    @Autowired
    private TblOrderBlankServiceImpl orderBlankService;
    @Autowired
    private BizUserService bizUserService;
    @Autowired
    private TblSysFileDao tblSysFileDao;
    @Autowired
    private TblOrderBlankFormDao orderBlankFormDao;
    @Autowired
    private BizDeptService bizDeptService;
    @Autowired
    private VillageController villageController;


    /**
     * 查询【请填写功能名称】列表
     */
    @PostMapping("/search")
    @ResponseBody
    public Object list(@RequestBody TblOrderBlankModule tblOrderBlankModule) {
        JSONObject userDept = bizDeptService.getUserDept(super.getUserId());
        String dept_id = userDept.getString("dept_id");
        // 2. 找到当前用户所属部门编号 查询 所有子部门 及 当前部门 list 准备查找项目参数
        List<String> results = Lists.newArrayList();
        results.add(dept_id);
        villageController.recursion(dept_id, results);
        // 4. 查找小区 级联找 看起来是有点lo
        tblOrderBlankModule.setPermissionDeptIds((ArrayList<String>) results);
        tblOrderBlankModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        tblOrderBlankModule.setUserId(getUserId());

        PageListResultEntity menu = orderBlankService.search(tblOrderBlankModule);
        return ResponseResult.successObj(menu);
    }


    /**
     * 新增保存【请填写功能名称】
     */
    @Transactional
    @PostMapping("/add")
    @ResponseBody
    public Object addSave(@RequestBody TblOrderBlank tblOrderBlank) {
        String Id = SnowFlakeUtils.nextStrId();
        tblOrderBlank.setvOrderBlankId(Id);
        tblOrderBlank.setiDelFlag(BusinessConstants.DELETE_FLAG_NO);
        tblOrderBlank.setiStatus(ISTATUS_ASSIGN_NO);
        List<TblOrderBlankForm> formList = tblOrderBlank.getOrderBlankFormList();
        if (formList.size() > 0) {
            Integer pos = 1;
            for (TblOrderBlankForm form : formList) {
                form.setvOrderBlankFormId(SnowFlakeUtils.nextStrId());
                form.setvOrderBlankId(Id);
                form.setiPosition(pos);
                pos += 1;
            }
        }
        int succ = orderBlankService.insert(tblOrderBlank);
        if (succ > 0) {
            orderBlankFormDao.insertBatch(formList);
            return ResponseResult.successObj(tblOrderBlank);
        } else {
            throw new BizException(ResponseResult.error());
        }
    }

    /**
     * 修改功能【请填写功能名称】
     */
    @Transactional
    @PostMapping("/edit")
    @ResponseBody
    public Object update(@RequestBody TblOrderBlank tblOrderBlank) {
        List<TblOrderBlankForm> formList = tblOrderBlank.getOrderBlankFormList();
        orderBlankFormDao.deleteAllByOrderBlankId(tblOrderBlank.getvOrderBlankId());
        if (formList.size() > 0) {
            Integer pos = 1;
            for (TblOrderBlankForm form : formList) {
                form.setvOrderBlankFormId(SnowFlakeUtils.nextStrId());
                form.setvOrderBlankId(tblOrderBlank.getvOrderBlankId());
                form.setiPosition(pos);
                pos += 1;
            }
        }
        int succ = orderBlankService.update(tblOrderBlank);
        if (succ > 0) {
            orderBlankFormDao.insertBatch(formList);
            return ResponseResult.successObj(tblOrderBlank);
        } else {
            throw new BizException(ResponseResult.error());
        }
    }

    /**
     * 详情【请填写功能名称】
     */
    @GetMapping("/detail/{vOrderBlankId}")
    public Object edit(@PathVariable("vOrderBlankId") String vOrderBlankId) {
        TblOrderBlank orderBlank = orderBlankService.detail(vOrderBlankId);
        return ResponseResult.successObj(orderBlank);
    }

    /**
     * 删除【请填写功能名称】
     */
    @PostMapping("/remove")
    @ResponseBody
    public Object remove(@RequestBody TblOrderBlank tblOrderBlank) {
        int succ = orderBlankService.delete(tblOrderBlank);
        if (succ > 0) {
            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error());
        }
    }
}
