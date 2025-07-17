package com.shmashine.api.service.dept;

import java.util.List;

import com.shmashine.api.dao.TblSysDeptLogoDao;
import com.shmashine.common.entity.TblSysDeptLogo;

public interface TblSysDeptLogoServiceI {

    TblSysDeptLogoDao getTblSysDeptLogoDao();

    TblSysDeptLogo getById(String vLogoId);

    List<TblSysDeptLogo> getByEntity(TblSysDeptLogo tblSysDeptLogo);

    List<TblSysDeptLogo> listByEntity(TblSysDeptLogo tblSysDeptLogo);

    List<TblSysDeptLogo> listByIds(List<String> ids);

    int insert(TblSysDeptLogo tblSysDeptLogo);

    int insertBatch(List<TblSysDeptLogo> list);

    int update(TblSysDeptLogo tblSysDeptLogo);

    int updateBatch(List<TblSysDeptLogo> list);

    int deleteById(String vLogoId, String vDeptId);

    int deleteByEntity(TblSysDeptLogo tblSysDeptLogo);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblSysDeptLogo tblSysDeptLogo);
}