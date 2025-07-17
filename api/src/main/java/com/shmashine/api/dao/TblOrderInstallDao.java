package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.entity.TblOrderInstall;
import com.shmashine.api.module.orderBlank.TblOrderInstallModule;

public interface TblOrderInstallDao {

    public List<TblOrderInstall> list();

    public int insertRecord(@Param("tblOrderInstall") TblOrderInstall tblOrderInstall);

    public int updateRecord(@Param("tblOrderInstall") TblOrderInstall tblOrderInstall);

    public int deleteRecord(@Param("tblOrderInstall") TblOrderInstall tblOrderInstall);

    public TblOrderInstall detail(String tblOrderInstallId);

    List<Map<String, Object>> search(@Param("tblOrderInstallModule") TblOrderInstallModule tblOrderInstallModule);
}
