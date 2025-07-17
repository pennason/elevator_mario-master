package com.shmashine.api.service.system;

import java.util.List;

import com.shmashine.api.dao.TblSysSequencemanagerDao;
import com.shmashine.common.entity.TblSysSequencemanager;

public interface TblSysSequencemanagerServiceI {

    TblSysSequencemanagerDao getTblSysSequencemanagerDao();

    TblSysSequencemanager getById(String vSeqId);

    List<TblSysSequencemanager> getByEntity(TblSysSequencemanager tblSysSequencemanager);

    List<TblSysSequencemanager> listByEntity(TblSysSequencemanager tblSysSequencemanager);

    List<TblSysSequencemanager> listByIds(List<String> ids);

    int insert(TblSysSequencemanager tblSysSequencemanager);

    int insertBatch(List<TblSysSequencemanager> list);

    int update(TblSysSequencemanager tblSysSequencemanager);

    int updateBatch(List<TblSysSequencemanager> list);

    int deleteById(String vSeqId);

    int deleteByEntity(TblSysSequencemanager tblSysSequencemanager);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblSysSequencemanager tblSysSequencemanager);
}