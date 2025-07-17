package com.shmashine.api.service.dept.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.shmashine.api.dao.BizDeptDao;
import com.shmashine.api.dao.BizUserDao;
import com.shmashine.api.dao.TblSysDeptDao;
import com.shmashine.api.dao.TblSysDeptUserDao;
import com.shmashine.api.module.dept.input.SearchDetpListModule;
import com.shmashine.api.service.dept.BizDeptService;
import com.shmashine.api.service.system.TblSysDeptServiceI;
import com.shmashine.api.service.system.TblSysDeptUserServiceI;
import com.shmashine.api.service.system.TblSysUserServiceI;
import com.shmashine.api.util.EncodeUtil;
import com.shmashine.common.constants.BusinessConstants;
import com.shmashine.common.constants.SystemConstants;
import com.shmashine.common.entity.TblSysDept;
import com.shmashine.common.entity.TblSysDeptUser;
import com.shmashine.common.entity.TblSysUser;
import com.shmashine.common.utils.CryptoUtil;
import com.shmashine.common.utils.SnowFlakeUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BizDeptServiceImpl implements BizDeptService {


    private TblSysDeptDao tblSysDeptDao;

    private TblSysDeptUserDao tblSysDeptUserDao;

    private BizDeptDao bizDeptDao;

    private TblSysDeptServiceI tblSysDeptServiceI;

    private TblSysUserServiceI tblSysUserServiceI;

    private TblSysDeptUserServiceI tblSysDeptUserServiceI;

    private BizUserDao bizUserDao;

    @Autowired
    public BizDeptServiceImpl(TblSysDeptDao tblSysDeptDao, TblSysDeptUserDao tblSysDeptUserDao, BizDeptDao bizDeptDao, TblSysDeptServiceI tblSysDeptServiceI, TblSysUserServiceI tblSysUserServiceI, TblSysDeptUserServiceI tblSysDeptUserServiceI, BizUserDao bizUserDao) {
        this.tblSysDeptDao = tblSysDeptDao;
        this.tblSysDeptUserDao = tblSysDeptUserDao;
        this.bizDeptDao = bizDeptDao;
        this.tblSysDeptServiceI = tblSysDeptServiceI;
        this.tblSysUserServiceI = tblSysUserServiceI;
        this.tblSysDeptUserServiceI = tblSysDeptUserServiceI;
        this.bizUserDao = bizUserDao;
    }

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
    public List<TblSysDept> searchDeptParent(SearchDetpListModule searchDetpListModule) {
        return bizDeptDao.searchDeptParent(searchDetpListModule);
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
    @CacheEvict(value = "dept", allEntries = true)
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
    public List<TblSysDept> getAll(SearchDetpListModule searchDetpListModule) {
        return bizDeptDao.getAll(searchDetpListModule);
    }

    /**
     * 上传外协团队数据
     *
     * @param data
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean geneOutServiceData(List<List<String>> data) throws Exception {
        TblSysDept rootDept = tblSysDeptDao.findByName("系统", null);

        for (List<String> item : data) {
            String deptName = item.get(1);
            if (deptName == null && deptName.equals("")) {
                throw new RuntimeException("公司名称不能为空");
            }

            TblSysDept tblSysDept = tblSysDeptDao.findByName(deptName, null);

            if (tblSysDept == null) {
                tblSysDept = new TblSysDept();

                if (deptName != null && !deptName.equals("")) {
                    if (tblSysDeptDao.existsByName(deptName) != 1) {
                        tblSysDept.setVDeptId(SnowFlakeUtils.nextStrId());
                        tblSysDept.setVParentId(rootDept.getVDeptId());
                        tblSysDept.setVDeptName(deptName);
                        tblSysDept.setIDeptTypeId(4);
                        tblSysDept.setIStatus(0);
                        tblSysDept.setiWuyePlatform(2);
                        tblSysDept.setiLevel(1);
                        tblSysDept.setiOutService(true);
                        int insertRes = tblSysDeptServiceI.insert(tblSysDept);
                        if (insertRes != 1)
                            throw new RuntimeException("新建公司失败");
                    }
                }
            }

            String deptNameBu = item.get(2);
            if (deptNameBu == null && deptNameBu.equals("")) {
                throw new RuntimeException("部门名称不能为空");
            }

            TblSysDept tblSysDeptBu = tblSysDeptDao.findByName(deptNameBu, tblSysDept.getVDeptId());

            if (tblSysDeptBu == null) {
                tblSysDeptBu = new TblSysDept();

                tblSysDeptBu.setVDeptId(SnowFlakeUtils.nextStrId());
                tblSysDeptBu.setVDeptName(deptNameBu);
                tblSysDeptBu.setIDeptTypeId(4);
                tblSysDeptBu.setIStatus(0);
                tblSysDeptBu.setiWuyePlatform(2);
                tblSysDeptBu.setiLevel(2);
                tblSysDeptBu.setVParentId(tblSysDept.getVDeptId());
                tblSysDeptBu.setiOutService(true);
                int insertResBu = tblSysDeptServiceI.insert(tblSysDeptBu);
                if (insertResBu != 1)
                    throw new RuntimeException("新建部门失败");
            }

            HashMap userInfo = bizUserDao.getUserDetailByMobile(CryptoUtil.encryptAesBase64("vMobile", item.get(5)));

            if (userInfo == null) {
                TblSysUser user = new TblSysUser();

                user.setVUserId(SnowFlakeUtils.nextStrId());
                user.setVUsername(item.get(4));
                user.setVName(item.get(4));
                user.setvPosition(item.get(3));
                user.setVMobile(item.get(5));
                user.setvIdentity(item.get(6));
                user.setvOpsCertCode(item.get(7));
                user.setvOpsCertAgency(item.get(8));
                user.setIStatus(0);
                user.setRemark(item.get(10));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                user.setDtOpsCertExpire(sdf.parse(item.get(9)));
                user.setVPassword(EncodeUtil.BCryptPasswordEncoder(BusinessConstants.INITIAL_PASSWORD));
                int insertUserRes = tblSysUserServiceI.insert(user);

                if (insertUserRes != 1) {
                    throw new RuntimeException("新建外协用户失败");
                } else {
                    TblSysDeptUser tblSysDeptUser = new TblSysDeptUser();

                    tblSysDeptUser.setVUserId(user.getVUserId());
                    tblSysDeptUser.setVDeptId(tblSysDeptBu.getVDeptId());

                    if (tblSysDeptUserServiceI.insert(tblSysDeptUser) != 1) {
                        throw new RuntimeException("外协用户分配部门失败");
                    }
                }
            } else {
                throw new RuntimeException("用户已经存在");
            }

        }

        return true;
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
