package com.shmashine.api.controller.dept;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.dept.input.EditDeptLogModule;
import com.shmashine.api.service.dept.BizDeptLogoService;
import com.shmashine.api.util.PojoConvertUtil;
import com.shmashine.common.entity.TblSysDeptLogo;
import com.shmashine.common.utils.OSSUtil;

import lombok.extern.slf4j.Slf4j;


/**
 * 部门Logo信息
 */
@RestController
@Slf4j
@RequestMapping("/deptLogo")
public class DeptLogoController extends BaseRequestEntity {


    private BizDeptLogoService bizDeptLogoService;

    @Autowired
    public DeptLogoController(BizDeptLogoService bizDeptLogoService) {
        this.bizDeptLogoService = bizDeptLogoService;
    }


    /**
     * 通过部门编号获取Logo信息
     *
     * @param deptId 部门编号
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/searchDeptLogo")
    public Object searchDeptLogo(@RequestBody String deptId) {
        Map dept = bizDeptLogoService.getSystemLogInfo(deptId);
        return ResponseResult.successObj(dept);
    }

    /**
     * 修改部门logo信息
     *
     * @param editDeptLogModule
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/editDeptLogo")
    public Object editDeptLog(@RequestBody @Valid EditDeptLogModule editDeptLogModule) {
        TblSysDeptLogo tblSysDeptLogo = PojoConvertUtil.convertPojo(editDeptLogModule, TblSysDeptLogo.class);
        int update = bizDeptLogoService.update(tblSysDeptLogo);
        if (update != 0) {
            return ResponseResult.success();
        }
        return ResponseResult.error();
    }

    /**
     * Log图片上传
     *
     * @param deptId 部门编号
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/upLoadDeptLogoImg/{deptId}")
    public Object upLoadDeptLogImg(@RequestParam("files") MultipartFile[] files, @PathVariable @Valid @NotNull(message = "请输入deptId") String deptId) {

        // 3. 添加OSS文件
        List<Map> maps = OSSUtil.saveDeptLog(files, deptId);
        if (maps.size() == 0 || maps.size() > 1) {
            return ResponseResult.error();
        }
        // 更新文件路径
        Map item = maps.get(0);
        Object absoluterUrl = item.get("url");
        String url = OSSUtil.OSS_URL + absoluterUrl;

        TblSysDeptLogo tblSysDeptLogo = new TblSysDeptLogo();
        tblSysDeptLogo.setVDeptId(deptId);
        tblSysDeptLogo.setVLogFileUrl(url);
        bizDeptLogoService.update(tblSysDeptLogo);
        return ResponseResult.success();
    }
}
