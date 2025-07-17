package com.shmashine.api.controller.dept;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.shmashine.api.config.exception.BizException;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.dept.input.SearchDetpListModule;
import com.shmashine.api.redis.utils.RedisUtils;
import com.shmashine.api.service.dept.BizDeptService;
import com.shmashine.api.service.dept.TblSysDeptLogoServiceI;
import com.shmashine.api.service.system.TblSysDeptServiceI;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.api.util.ExceExport2;
import com.shmashine.api.util.JSONUtils;
import com.shmashine.common.constants.BusinessConstants;
import com.shmashine.common.constants.RedisConstants;
import com.shmashine.common.constants.SystemConstants;
import com.shmashine.common.entity.TblSysDept;
import com.shmashine.common.entity.TblSysDeptLogo;
import com.shmashine.common.utils.SnowFlakeUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 部门信息接口
 */
@RestController
@Slf4j
@RequestMapping("/dept")
public class DeptController extends BaseRequestEntity {

    private BizDeptService bizDeptService;

    private TblSysDeptServiceI tblSysDeptServiceI;

    private TblSysDeptLogoServiceI sysDeptLogoServiceI;

    private BizUserService bizUserService;

    private RedisUtils redisUtils;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    public DeptController(BizDeptService bizDeptService,
                          TblSysDeptServiceI tblSysDeptServiceI, TblSysDeptLogoServiceI sysDeptLogoServiceI, BizUserService bizUserService, RedisUtils redisUtils) {
        this.bizDeptService = bizDeptService;
        this.tblSysDeptServiceI = tblSysDeptServiceI;
        this.sysDeptLogoServiceI = sysDeptLogoServiceI;
        this.bizUserService = bizUserService;
        this.redisUtils = redisUtils;
    }

    /**
     * 获取用户当前所在部门
     *
     * @param
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("getUserDept")
    public Object getUserDept() {
        return ResponseResult.successObj(bizDeptService.getUserDept(getUserId()));
    }

    /**
     * 获取部门列表
     *
     * @param searchDetpListModule
     * @return #type:com.shmashine.api.entity.base.ResponseResult,com.shmashine.common.entity.TblSysDept#
     */
    @PostMapping("SearchDept")
    public Object SearchUser(@RequestBody @Valid SearchDetpListModule searchDetpListModule) {
        if (searchDetpListModule.getvParentId() == null) {
            JSONObject userDept = bizDeptService.getUserDept(getUserId());
            searchDetpListModule.setvParentId(userDept.getString("dept_id"));
        }
        return ResponseResult.successObj(bizDeptService.searchDeptList(searchDetpListModule));
    }

    /**
     * 获取下级部门
     *
     * @param searchDetpListModule
     * @return #type:com.shmashine.api.entity.base.ResponseResult,com.shmashine.common.entity.TblSysDept#
     */
    @PostMapping("searchDeptParent")
    public Object searchDeptParent(@RequestBody @Valid SearchDetpListModule searchDetpListModule) {

        //超管返回所有部门
        if (bizUserService.isAdmin(getUserId())) {
            List<TblSysDept> tblSysDepts = bizDeptService.getAll(searchDetpListModule);
            return ResponseResult.successObj(tblSysDepts);
        }

        if (searchDetpListModule.getvParentId() == null) {
            JSONObject userDept = bizDeptService.getUserDept(getUserId());
            searchDetpListModule.setvParentId(userDept.getString("dept_id"));
        }

        searchDetpListModule.setAdminFlag(bizUserService.isAdmin(getUserId()));

        JSONObject userDept = bizDeptService.getUserDept(super.getUserId());
        String dept_id = userDept.getString("dept_id");
        // 2. 找到当前用户所属部门编号 查询 所有子部门 及 当前部门 list 准备查找项目参数
        //获取所有部门缓存
        String key = RedisConstants.USER_DEPT_INFO + dept_id;
        List<String> results = (List<String>) redisTemplate.opsForValue().get(key);

        if (results == null) {
            results = new ArrayList<>();
            recursion(dept_id, results);
            if (!results.contains(dept_id))
                results.add(dept_id);
            redisTemplate.opsForValue().set(key, results);

        }
        searchDetpListModule.setPermissionDeptIds((ArrayList<String>) results);

        return ResponseResult.successObj(bizDeptService.searchDeptParent(searchDetpListModule));
    }

    /**
     * 添加部门
     *
     * @param tblSysDept
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("addDept")
    public Object addDept(@RequestBody @Valid TblSysDept tblSysDept) {
        String v_user_id = super.getUserId();
        // 采番 部门编号
        String dept = SnowFlakeUtils.nextStrId();


        // 创建默认部门Log
        TblSysDeptLogo tblSysDeptLog = new TblSysDeptLogo();
        tblSysDeptLog.setVLogoId(SnowFlakeUtils.nextStrId());
        tblSysDeptLog.setVDeptId(dept);
        tblSysDeptLog.setVSystemTitle(BusinessConstants.DEFAULT_SYSTEM_NAME);
        tblSysDeptLog.setVLogFileUrl(BusinessConstants.DEFAULT_SYSTEM_LOG_FILE_URL);
        sysDeptLogoServiceI.insert(tblSysDeptLog);

        // 设置唯一标识，创建人,修改人，创建时间，修改时间
        tblSysDept.setVDeptId(dept);
        tblSysDept.setVModifyid(v_user_id);
        tblSysDept.setVCreateid(v_user_id);
        TblSysDept tbsSaysDeptResult = bizDeptService.settingDeptUserAndParent(tblSysDept);

        if (tblSysDeptServiceI.insert(tbsSaysDeptResult) > 0) {
            redisUtils.deleteByPrex("dept");

            //删除缓存
            Set<String> keys = redisTemplate.keys(RedisConstants.USER_DEPT_INFO + "*");
            redisTemplate.delete(keys);

            return ResponseResult.success();
        } else {
            return new ResponseResult(ResponseResult.CODE_ERROR, "msg3_01");
        }

    }

    /**
     * 编辑部门
     *
     * @param tblSysDept
     * @return #type:com.shmashine.api.entity.base.ResponseResult,com.shmashine.common.entity.TblSysDept#
     */
    @PostMapping("editDept")
    public Object editDept(@RequestBody @Valid TblSysDept tblSysDept) {
        // 设置更新人
        tblSysDept.setVModifyid(getUserId());
        if (tblSysDeptServiceI.update(tblSysDept) > 0) {
            redisUtils.deleteByPrex("dept");
            return ResponseResult.success();
        }
        return new ResponseResult(ResponseResult.CODE_ERROR, "msg3_02");
    }

    /**
     * 删除部门
     *
     * @param params 部门编号
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("deleteDept")
    public Object deleteDept(@RequestBody String params) {
        Map<String, Object> prams = JSONUtils.jsonToMap(params);
        TblSysDept tblSysDept = new TblSysDept();
        tblSysDept.setVDeptId((String) prams.get("vDeptId"));
        tblSysDept.setIStatus(SystemConstants.DATA_ABNORMAL);
        if (tblSysDeptServiceI.update(tblSysDept) > 0) {
            redisUtils.deleteByPrex("dept");

            //删除缓存
            Set<String> keys = redisTemplate.keys(RedisConstants.USER_DEPT_INFO + "*");
            redisTemplate.delete(keys);

            return ResponseResult.success();
        }
        return new ResponseResult(ResponseResult.CODE_ERROR, "msg3_03");
    }

    /**
     * 批量删除部门
     *
     * @param arr 部门编号List
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("batchDeleteDept")
    public Object batchDeleteDept(@RequestBody @Valid @NotBlank List<String> arr) {
        // 逻辑删除
        int num = 0;
        for (int i = 0; i < arr.size(); i++) {
            TblSysDept tblSysDept = new TblSysDept();
            tblSysDept.setVDeptId(arr.get(i));
            tblSysDept.setIStatus(SystemConstants.DATA_ABNORMAL);
            if (tblSysDeptServiceI.update(tblSysDept) > 0) {
                num++;
            }
            ;
        }
        if (num == arr.size()) {
            return ResponseResult.success();
        }
        return new ResponseResult(ResponseResult.CODE_ERROR, "msg3_04");
    }

    /**
     * 获取部门详情
     *
     * @param params 部门编号
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("getDeptDetail")
    public Object getDeptDetail(@RequestBody String params) {
        Map<String, Object> prams = JSONUtils.jsonToMap(params);
        return ResponseResult.successObj(bizDeptService.getDeptDetail((String) prams.get("vDeptId")));
    }


    /**
     * 过时接口，可用api/dept/searchDeptParent替代，删除前需与前端确认
     * 获取部门下拉框(权限限制)
     *
     * @param deptType 部门类型
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @Deprecated
    @GetMapping("getDeptDetailSelectList/{deptType}")
    public Object getDeptDetailSelectList(@PathVariable("deptType") String deptType) {
        // 递归找到有权查看的的部门编号
        JSONObject userDept = bizDeptService.getUserDept(super.getUserId());
        String dept_id = userDept.getString("dept_id");
        // 1. 找到当前用户所属部门编号 查询 所有子部门 及 当前部门 list
        List<Map> results = Lists.newArrayList();
        results.add(bizDeptService.getDeptInfo(dept_id));
        recursion(dept_id, results, deptType);
        return ResponseResult.successObj(results);
    }

    /**
     * 获取所有部门id
     *
     * @return
     */
    @GetMapping("/getDeptIdList")
    public List<String> getDeptIdList() {

        // 递归找到有权查看的的部门编号
        JSONObject userDept = bizDeptService.getUserDept(super.getUserId());
        String dept_id = userDept.getString("dept_id");
        // 1. 找到当前用户所属部门编号 查询 所有子部门

        //获取所有部门缓存
        String key = RedisConstants.USER_DEPT_INFO + dept_id;

        List<String> results = (List<String>) redisTemplate.opsForValue().get(key);

        if (results == null) {
            results = new ArrayList<>();
            recursion(dept_id, results);
            if (!results.contains(dept_id))
                results.add(dept_id);
            redisTemplate.opsForValue().set(key, results);

        }

        results.add(dept_id);

        return results;
    }

    /**
     * 下载外协模版
     *
     * @param response
     * @param request
     * @throws IOException
     */
    @GetMapping("/downloadOutServiceTemplate")
    public Object downloadOutServiceTemplate(HttpServletResponse response, HttpServletRequest request) throws IOException {

        String filePath = "https://oss-mashine.oss-cn-qingdao.aliyuncs.com/web/static/iot/demo-excel/%E5%A4%96%E5%8D%8F%E4%BA%BA%E5%91%98%E4%BF%A1%E6%81%AF%E8%A1%A8.xlsx";

        return ResponseResult.successObj(filePath);

        //        String  filePath = getClass().getResource("/templates/外协人员信息表.xlsx" ).getPath();
//        InputStream bis = new BufferedInputStream(new FileInputStream(new File(filePath)));
//
//        String filename = "外协人员信息表.xlsx";
//
//        filename = URLEncoder.encode(filename,"UTF-8");
//        response.addHeader("Content-Disposition", "attachment;filename=" + filename);
//        response.setContentType("multipart/form-data");
//
//        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
//        int len = 0;
//        while((len = bis.read()) != -1){
//            out.write(len);
//            out.flush();
//        }
//        out.close();
    }

    /**
     * 外协导入
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/uploadOutServiceXlsx")
    public Object uploadOutServiceXlsx(@RequestParam("file") MultipartFile file) throws Exception {
        if (file == null) {
            throw new BizException(ResponseResult.error("上传文件不能为空"));
        }
        List<List<String>> data = ExceExport2.readXlsxData(file, 2);

        List<List<String>> validData = data.stream().filter(item -> item.stream().filter(it -> !it.equals("")).collect(Collectors.toList()).size() >= 10).collect(Collectors.toList());

        if (validData.size() == 0) {
            throw new BizException(ResponseResult.error("外协人员信息填写不完整！"));
        }

        Boolean res;
        try {
            res = bizDeptService.geneOutServiceData(validData);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BizException(ResponseResult.error(ex.getMessage()));
        }

        return ResponseResult.successObj(res);
    }

    /**
     * 递归查询 下级部门的用户
     *
     * @param dept_id
     * @param strings
     */
    public void recursion(String dept_id, List<Map> strings, String deptType) {

        if (null != dept_id) {
            List<Map> userDeptSelectList = bizUserService.getUserDeptSelectList(dept_id, deptType);
            if (null != userDeptSelectList && userDeptSelectList.size() > 0) {
                userDeptSelectList.forEach(id -> {
                    recursion((String) id.get("v_dept_id"), strings, deptType);
                });
            }
            strings.addAll(userDeptSelectList);
        }
    }


    /**
     * 递归查询 下级部门的用户
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

}
