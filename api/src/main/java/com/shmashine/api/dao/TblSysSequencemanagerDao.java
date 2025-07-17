package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblSysSequencemanager;


public interface TblSysSequencemanagerDao {

    TblSysSequencemanager getById(@NotNull String vSeqId);

    List<TblSysSequencemanager> listByEntity(TblSysSequencemanager tblSysSequencemanager);

    List<TblSysSequencemanager> getByEntity(TblSysSequencemanager tblSysSequencemanager);

    List<TblSysSequencemanager> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblSysSequencemanager tblSysSequencemanager);

    int insertBatch(@NotEmpty List<TblSysSequencemanager> list);

    int update(@NotNull TblSysSequencemanager tblSysSequencemanager);

    int updateByField(@NotNull @Param("where") TblSysSequencemanager where, @NotNull @Param("set") TblSysSequencemanager set);

    int updateBatch(@NotEmpty List<TblSysSequencemanager> list);

    int deleteById(@NotNull String vSeqId);

    int deleteByEntity(@NotNull TblSysSequencemanager tblSysSequencemanager);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblSysSequencemanager tblSysSequencemanager);

}