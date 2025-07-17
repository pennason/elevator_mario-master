package com.shmashine.pm.api.service.dept.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.shmashine.pm.api.contants.SystemConstants;
import com.shmashine.pm.api.dao.BizDeptDao;
import com.shmashine.pm.api.dao.TblSysDeptDao;
import com.shmashine.pm.api.dao.TblSysDeptUserDao;
import com.shmashine.pm.api.entity.TblSysDept;
import com.shmashine.pm.api.entity.TblSysDeptUser;
import com.shmashine.pm.api.entity.base.PageListResultEntity;
import com.shmashine.pm.api.module.dept.input.SearchDetpListModule;
import com.shmashine.pm.api.service.dept.BizDeptService;

@Service
public class BizDeptServiceImpl implements BizDeptService {

    @Autowired
    private TblSysDeptDao tblSysDeptDao;
    @Autowired
    private TblSysDeptUserDao tblSysDeptUserDao;
    @Autowired
    private BizDeptDao bizDeptDao;


    /**
     * 获取部门列表
     *
     * @param searchDetpListModule
     * @return
     */
    @Override
    public List<TblSysDept> searchDeptList(SearchDetpListModule searchDetpListModule) {
        return bizDeptDao.searchDept(searchDetpListModule);
    }

    /**
     * 获取部门列表
     *
     * @param searchDetpListModule
     * @return
     */
    @Override
    public PageListResultEntity<Map> searchDeptParent(SearchDetpListModule searchDetpListModule) {

        Integer pageIndex = searchDetpListModule.getPageIndex();
        Integer pageSize = searchDetpListModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }

        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Map> tPageInfo = new PageInfo<>(bizDeptDao.searchDeptParent(searchDetpListModule), pageSize);

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) tPageInfo.getTotal(), tPageInfo.getList());
    }

    @Override
    public Map getDeptInfo(String deptId) {
        return bizDeptDao.getDeptInfo(deptId);
    }


    /**
     * 获取用户所在部门
     *
     * @param user_id
     * @return
     */
    @Override
    public JSONObject getUserDept(String user_id) {
        TblSysDeptUser tblSysDeptUser = new TblSysDeptUser();
        tblSysDeptUser.setVUserId(user_id);
        List<TblSysDeptUser> byEntity = tblSysDeptUserDao.getByEntity(tblSysDeptUser);
        TblSysDeptUser tblSysDeptUser1 = byEntity.get(0);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_id", tblSysDeptUser1.getVUserId());
        jsonObject.put("dept_id", tblSysDeptUser1.getVDeptId());
        TblSysDept byId = tblSysDeptDao.getById(tblSysDeptUser1.getVDeptId());
        jsonObject.put("dept_name", byId.getVDeptName());
        return jsonObject;
    }

    /**
     * 创建部门
     *
     * @param tblSysDept
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public TblSysDept settingDeptUserAndParent(TblSysDept tblSysDept) {
        // 获取变量
        String userId = tblSysDept.getVCreateid();

        String vDeptId = tblSysDept.getVDeptId();
        Date date = new Date();
        // 1.2 设置上级编号 默认 数据为正常
        if (tblSysDept.getVParentId() == null) {
            JSONObject userDept = getUserDept(userId);
            tblSysDept.setVParentId(userDept.getString("dept_id"));
        }
        tblSysDept.setIStatus(SystemConstants.DATA_NORMAL);


        // 2.设置部门用户，默认当前创建用户用户在此部门下
        /*TblSysDeptUser tblSysDeptUser = new TblSysDeptUser();
        tblSysDeptUser.setDtModifytime(date);
        tblSysDeptUser.setDtCreatetime(date);
        tblSysDeptUser.setVCreateid(userId);
        tblSysDeptUser.setVModifyid(userId);
        tblSysDeptUser.setVDeptId(vDeptId);
        tblSysDeptUser.setVUserId(userId);

        tblSysDeptUserDao.insert(tblSysDeptUser);*/
        return tblSysDept;
    }

    /**
     * 获取部门详情
     *
     * @param deptId
     * @return
     */
    @Override
    public Object getDeptDetail(String deptId) {
        return bizDeptDao.getDeptDetail(deptId);
    }

    /**
     * 根据部门id获取所有子部门及自己
     *
     * @param deptId
     * @return
     */
    @Override
    @Cacheable(value = "dept", key = "targetClass + methodName +#p0")
    public List<String> getAllSubsByDeptId(String deptId) {
        // 获取部门下属及自己的所有部门ids
        List<String> subDeptIds = Lists.newArrayList();
        recursion(deptId, subDeptIds);
        // 添加上该部门自己
        subDeptIds.add(deptId);
        return subDeptIds;
    }

    @Override
    public int existsDeptByName(String deptName) {
        return bizDeptDao.existsDeptByName(deptName);
    }

    /**
     * 递归查询 下级部门的编号
     */
    private void recursion(String parentDeptId, List<String> results) {
        if (StringUtils.hasText(parentDeptId)) {
            // 获取子部门
            List<String> subDeptIds = bizDeptDao.getDeptIdsByParentDeptId(parentDeptId);
            if (null != subDeptIds && subDeptIds.size() > 0) {
                subDeptIds.forEach(id -> {
                    recursion(id, results);
                });
                results.addAll(subDeptIds);
            }
        }
    }
}
