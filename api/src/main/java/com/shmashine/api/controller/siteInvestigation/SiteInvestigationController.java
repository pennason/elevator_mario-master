package com.shmashine.api.controller.siteInvestigation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.shmashine.api.dao.TblSiteInvestigationDao;
import com.shmashine.api.dao.TblSysFileDao;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.siteInvestigation.SearchSiteInvestigationModule;
import com.shmashine.api.service.dept.BizDeptService;
import com.shmashine.api.service.siteInvestigation.ITblSiteInvestigationService;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.common.constants.BusinessConstants;
import com.shmashine.common.entity.TblSiteInvestigation;
import com.shmashine.common.entity.TblSysFile;
import com.shmashine.common.utils.OSSUtil;
import com.shmashine.common.utils.SnowFlakeUtils;

/**
 * 【请填写功能名称】Controller
 *
 * @author depp
 * @date 2021-07-06
 */
@RestController
@RequestMapping("/investigation")
public class SiteInvestigationController extends BaseRequestEntity {

    private static final String prefix = "/investigation";
    private static final int SiteInvestigationFileType = 5;

    @Autowired
    private TblSiteInvestigationDao tblSiteInvestigationDao;
    @Autowired
    private ITblSiteInvestigationService tblSiteInvestigationService;
    @Autowired
    private BizUserService bizUserService;
    @Autowired
    private TblSysFileDao tblSysFileDao;
    @Autowired
    private BizDeptService bizDeptService;
    @Autowired
    private VillageController villageController;

    /**
     * 查询【请填写功能名称】列表
     */
    @PostMapping("/search")
    @ResponseBody
    public Object list(@RequestBody SearchSiteInvestigationModule searchSiteInvestigationModule) {
        JSONObject userDept = bizDeptService.getUserDept(super.getUserId());
        String dept_id = userDept.getString("dept_id");
        // 2. 找到当前用户所属部门编号 查询 所有子部门 及 当前部门 list 准备查找项目参数
        List<String> results = Lists.newArrayList();
        results.add(dept_id);
        villageController.recursion(dept_id, results);
        // 4. 查找小区 级联找 看起来是有点lo
        searchSiteInvestigationModule.setPermissionDeptIds((ArrayList<String>) results);
        searchSiteInvestigationModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchSiteInvestigationModule.setUserId(getUserId());

        PageListResultEntity menu = tblSiteInvestigationService.search(searchSiteInvestigationModule);
        return ResponseResult.successObj(menu);
    }


    /**
     * 新增【请填写功能名称】
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存【请填写功能名称】
     */
    @PostMapping("/add")
    @ResponseBody
    public Object addSave(@RequestBody TblSiteInvestigation tblSiteInvestigation) {
        String siteInvestigationId = SnowFlakeUtils.nextStrId();
        tblSiteInvestigation.setvSiteInvestigationId(siteInvestigationId);
        tblSiteInvestigation.setiDelFlag(BusinessConstants.DELETE_FLAG_NO);
        int succ = tblSiteInvestigationDao.insertTblSiteInvestigation(tblSiteInvestigation);
        if (succ > 0) {
            tblSiteInvestigation.setvSiteInvestigationId(siteInvestigationId);
            return ResponseResult.successObj(tblSiteInvestigation);
        } else {
            throw new BizException(ResponseResult.error());
        }
    }


    /**
     * 修改【请填写功能名称】
     */
    @GetMapping("/detail/{vSiteInvestigationId}")
    public Object edit(@PathVariable("vSiteInvestigationId") String vSiteInvestigationId) {
        TblSiteInvestigation tblSiteInvestigation = tblSiteInvestigationDao.selectTblSiteInvestigationById(vSiteInvestigationId);
        return ResponseResult.successObj(tblSiteInvestigation);
    }

    /**
     * 修改保存【请填写功能名称】
     */
    @PostMapping("/edit")
    @ResponseBody
    public Object editSave(@RequestBody TblSiteInvestigation tblSiteInvestigation) {
        int succ = tblSiteInvestigationDao.updateTblSiteInvestigation(tblSiteInvestigation);
        if (succ > 0) {
            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error());
        }
    }

    /**
     * 删除【请填写功能名称】
     */
    @PostMapping("/remove")
    @ResponseBody
    public Object remove(@RequestBody TblSiteInvestigation tblSiteInvestigation) {
        int succ = tblSiteInvestigationDao.deleteTblSiteInvestigationById(tblSiteInvestigation);
        if (succ > 0) {
            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error());
        }
    }

    /**
     * 现勘图片上传
     *
     * @param vSiteInvestigationId 部门编号
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/uploadImg/{vSiteInvestigationId}")
    public Object uploadImg(@RequestParam("files") MultipartFile[] files, @PathVariable @Valid @NotNull(message = "请输入现勘id") String vSiteInvestigationId) {
        // 添加OSS文件
        List<Map> maps = OSSUtil.saveSiteInvestigationImg(files, vSiteInvestigationId);
        List<TblSysFile> fileList = new ArrayList<>();

        for (Map item : maps) {
            TblSysFile file = new TblSysFile();
            String vFileId = SnowFlakeUtils.nextStrId();

            file.setVFileId(vFileId);
            file.setVFileType("0");
            file.setIBusinessType(SiteInvestigationFileType);

            Object absoluterUrl = item.get("url");
            String fileName = (String) item.get("fileName");
            String url = OSSUtil.OSS_URL + absoluterUrl;
            file.setVBusinessId(vSiteInvestigationId);
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
     * @param vSiteInvestigationId
     * @return
     */
    @GetMapping("/images/{vSiteInvestigationId}")
    public Object findAllImagesByVSiteInvestigationId(@PathVariable @Valid @NotNull(message = "请输入现勘id") String vSiteInvestigationId) {
        String businessId = vSiteInvestigationId;
        List<TblSysFile> fileList = tblSysFileDao.getAllByBusinessIdAndBusinessType(businessId, SiteInvestigationFileType);
        return ResponseResult.successObj(fileList);
    }
}
