package com.shmashine.api.service.dept.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.api.dao.BizSysDeptLogoDao;
import com.shmashine.api.service.dept.BizDeptLogoService;
import com.shmashine.common.entity.TblSysDeptLogo;

/**
 * 默认
 *
 * @author chenx
 */

@Service
public class BizDeptLogServiceImpl implements BizDeptLogoService {

    private BizSysDeptLogoDao bizSysDeptLogoDao;

    @Autowired
    public BizDeptLogServiceImpl(BizSysDeptLogoDao bizSysDeptLogoDao) {
        this.bizSysDeptLogoDao = bizSysDeptLogoDao;
    }

    @Override
    public Map getSystemLogInfo(String deptId) {
        return bizSysDeptLogoDao.getByDeptId(deptId);
    }

    @Override
    public int update(TblSysDeptLogo tblSysDeptLogo) {
        return bizSysDeptLogoDao.updateDeptLogo(tblSysDeptLogo);
    }
}
