package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.entity.TblOrderBlank;
import com.shmashine.api.module.orderBlank.TblOrderBlankModule;

public interface TblOrderBlankDao {

    public List<TblOrderBlank> list();

    public int insertRecord(@Param("tblOrderBlank") TblOrderBlank tblOrderBlank);

    public int updateRecord(@Param("tblOrderBlank") TblOrderBlank tblOrderBlank);

    public int deleteRecord(@Param("tblOrderBlank") TblOrderBlank tblOrderBlank);

    public TblOrderBlank detail(String tblOrderBlankId);

    List<Map<String, Object>> search(@Param("tblOrderBlankModule") TblOrderBlankModule tblOrderBlankModule);
}
