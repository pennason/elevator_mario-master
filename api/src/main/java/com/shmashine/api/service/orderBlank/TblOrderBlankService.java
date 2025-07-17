package com.shmashine.api.service.orderBlank;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.entity.TblOrderBlank;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.orderBlank.TblOrderBlankModule;

public interface TblOrderBlankService {

    public List<TblOrderBlank> list();

    public int insert(@Param("tblOrderBlank") TblOrderBlank tblOrderBlank);

    public int update(@Param("tblOrderBlank") TblOrderBlank tblOrderBlank);

    public int delete(@Param("tblOrderBlank") TblOrderBlank tblOrderBlank);

    public TblOrderBlank detail(String tblOrderBlankId);

    public PageListResultEntity search(@Param("tblOrderBlankModule") TblOrderBlankModule tblOrderBlankModule);
}
