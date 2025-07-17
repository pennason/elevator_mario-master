package com.shmashine.api.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblSysDeptLogo;

public interface BizSysDeptLogoDao {

    Map getByDeptId(@Param("deptId") String deptId);

    int updateDeptLogo(TblSysDeptLogo tblSysDeptLogo);
}
