package com.shmashine.pm.api.service.dept;

import java.util.Map;

import com.shmashine.pm.api.entity.TblSysDeptLogo;

public interface BizDeptLogoService {

    Map getSystemLogInfo(String deptId);

    int update(TblSysDeptLogo tblSysDeptLogo);
}
