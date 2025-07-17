package com.shmashine.api.service.orderBlank;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.entity.TblOrderInstall;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.orderBlank.TblOrderInstallModule;

public interface TblOrderInstallService {

    public List<TblOrderInstall> list();

    public int insert(@Param("tblOrderInstall") TblOrderInstall tblOrderInstall);

    public int update(@Param("tblOrderInstall") TblOrderInstall tblOrderInstall);

    public int delete(@Param("tblOrderInstall") TblOrderInstall tblOrderInstall);

    public TblOrderInstall detail(String tblOrderInstallId);

    public PageListResultEntity search(@Param("tblOrderInstallModule") TblOrderInstallModule tblOrderInstallModule);
}
