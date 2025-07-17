package com.shmashine.api.controller.orderInstall;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.shmashine.api.config.exception.BizException;
import com.shmashine.api.controller.village.VillageController;
import com.shmashine.api.dao.TblOrderBlankFormDao;
import com.shmashine.api.dao.TblSysFileDao;
import com.shmashine.api.entity.TblOrderInstall;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.orderBlank.TblOrderInstallModule;
import com.shmashine.api.service.dept.BizDeptService;
import com.shmashine.api.service.orderBlank.impl.TblOrderInstallServiceImpl;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.common.constants.BusinessConstants;
import com.shmashine.common.entity.TblSysFile;
import com.shmashine.common.utils.OSSUtil;
import com.shmashine.common.utils.SnowFlakeUtils;

@RestController
@Controller
@RequestMapping("/orderInstallation")
public class TblOrderInstallController extends BaseRequestEntity {

    private static final Integer ISTATUS_INSTALL_NO = 0;
    private static final Integer ISTATUS_INSTALL_YES = 1;

    private static final int OrderInstallFileType = 6;

    @Autowired
    private TblOrderInstallServiceImpl orderInstallService;
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
    public Object list(@RequestBody TblOrderInstallModule tblOrderInstallModule) {
        JSONObject userDept = bizDeptService.getUserDept(super.getUserId());
        String dept_id = userDept.getString("dept_id");
        // 2. 找到当前用户所属部门编号 查询 所有子部门 及 当前部门 list 准备查找项目参数
        List<String> results = Lists.newArrayList();
        results.add(dept_id);
        villageController.recursion(dept_id, results);
        // 4. 查找小区 级联找 看起来是有点lo
        tblOrderInstallModule.setPermissionDeptIds((ArrayList<String>) results);
        tblOrderInstallModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        tblOrderInstallModule.setUserId(getUserId());

        PageListResultEntity menu = orderInstallService.search(tblOrderInstallModule);
        return ResponseResult.successObj(menu);
    }


    /**
     * 新增保存【请填写功能名称】
     */
    @Transactional
    @PostMapping("/add")
    @ResponseBody
    public Object addSave(@RequestBody TblOrderInstall tblOrderInstall) {
        String Id = SnowFlakeUtils.nextStrId();
        tblOrderInstall.setvOrderInstallId(Id);
        tblOrderInstall.setiDelFlag(BusinessConstants.DELETE_FLAG_NO);
        tblOrderInstall.setiStatus(ISTATUS_INSTALL_NO);
        int succ = orderInstallService.insert(tblOrderInstall);
        if (succ > 0) {
            return ResponseResult.successObj(tblOrderInstall);
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
    public Object update(@RequestBody TblOrderInstall tblOrderInstall) {
        int succ = orderInstallService.update(tblOrderInstall);
        if (succ > 0) {
            return ResponseResult.successObj(tblOrderInstall);
        } else {
            throw new BizException(ResponseResult.error());
        }
    }

    /**
     * 详情【请填写功能名称】
     */
    @GetMapping("/detail/{vOrderInstallId}")
    public Object edit(@PathVariable("vOrderInstallId") String vOrderInstallId) {
        TblOrderInstall orderInstall = orderInstallService.detail(vOrderInstallId);
        return ResponseResult.successObj(orderInstall);
    }

    /**
     * 删除【请填写功能名称】
     */
    @PostMapping("/remove")
    @ResponseBody
    public Object remove(@RequestBody TblOrderInstall tblOrderInstall) {
        int succ = orderInstallService.delete(tblOrderInstall);
        if (succ > 0) {
            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error());
        }
    }

    /**
     * 安装单图片上传
     *
     * @param vOrderInstallId 部门编号
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/uploadImg/{vOrderInstallId}")
    public Object uploadImg(@RequestParam("files") MultipartFile[] files, @PathVariable @Valid @NotNull(message = "请输入安装单id") String vOrderInstallId) {
        // 添加OSS文件
        List<Map> maps = OSSUtil.saveSiteInvestigationImg(files, vOrderInstallId);
        List<TblSysFile> fileList = new ArrayList<>();

        for (Map item : maps) {
            TblSysFile file = new TblSysFile();
            String vFileId = SnowFlakeUtils.nextStrId();

            file.setVFileId(vFileId);
            file.setVFileType("0");
            file.setIBusinessType(OrderInstallFileType);

            Object absoluterUrl = item.get("url");
            String fileName = (String) item.get("fileName");
            String url = OSSUtil.OSS_URL + absoluterUrl;
            file.setVBusinessId(vOrderInstallId);
            file.setVUrl(url);
            fileList.add(file);
        }
        int succ = tblSysFileDao.insertBatch(fileList);

        if (succ > 0) {
            return ResponseResult.successObj(fileList);
        } else {
            throw new BizException(ResponseResult.error());
        }
    }

    /**
     * 删除现勘图片
     *
     * @param vFileId
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/deleteImg/{vFileId}")
    public Object deleteImg(@PathVariable @Valid @NotNull(message = "请输入图片id") String vFileId) {
        int succ = tblSysFileDao.deleteById(vFileId);
        if (succ > 0) {
            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error());
        }
    }

    /**
     * 得到所有图片
     *
     * @param vOrderInstallId
     * @return
     */
    @GetMapping("/images/{vOrderInstallId}")
    public Object findAllImagesByVOrderInstallId(@PathVariable @Valid @NotNull(message = "请输入安装id") String vOrderInstallId) {
        String businessId = vOrderInstallId;
        List<TblSysFile> fileList = tblSysFileDao.getAllByBusinessIdAndBusinessType(businessId, OrderInstallFileType);
        return ResponseResult.successObj(fileList);
    }
}
