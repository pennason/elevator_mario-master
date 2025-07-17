package com.shmashine.pm.api.service.dept.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.pm.api.dao.BizSysDeptLogoDao;
import com.shmashine.pm.api.entity.TblSysDeptLogo;
import com.shmashine.pm.api.service.dept.BizDeptLogoService;

@Service
public class BizDeptLogoServiceImpl implements BizDeptLogoService {


    private BizSysDeptLogoDao bizSysDeptLogoDao;

    @Autowired
    public BizDeptLogoServiceImpl(BizSysDeptLogoDao bizSysDeptLogoDao) {
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
