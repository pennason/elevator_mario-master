package com.shmashine.api.service.system;

import java.util.List;

import com.shmashine.api.dao.TblFaultDao;
import com.shmashine.common.entity.TblFault;

public interface TblFaultServiceI {

    TblFaultDao getTblFaultDao();

    TblFault getById(String vFaultId);

    List<TblFault> getByEntity(TblFault tblFault);

    List<TblFault> listByEntity(TblFault tblFault);

    List<TblFault> listByIds(List<String> ids);

    int insert(TblFault tblFault);

    int insertBatch(List<TblFault> list);

    int update(TblFault tblFault);

    int updateBatch(List<TblFault> list);

    int deleteById(String vFaultId);

    int deleteByEntity(TblFault tblFault);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblFault tblFault);
}