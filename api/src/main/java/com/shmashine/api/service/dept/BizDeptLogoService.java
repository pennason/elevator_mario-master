package com.shmashine.api.service.dept;

import java.util.Map;

import com.shmashine.common.entity.TblSysDeptLogo;

public interface BizDeptLogoService {

    Map getSystemLogInfo(String deptId);

    int update(TblSysDeptLogo tblSysDeptLogo);
}
