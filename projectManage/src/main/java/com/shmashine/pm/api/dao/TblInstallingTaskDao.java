package com.shmashine.pm.api.dao;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.shmashine.pm.api.entity.TblInstallingTask;

public interface TblInstallingTaskDao {

    TblInstallingTask getById(@NotNull String vInstallingTaskId);

    List<TblInstallingTask> listByEntity(TblInstallingTask tblInstallingTask);

    List<TblInstallingTask> getByEntity(TblInstallingTask tblInstallingTask);

    int insert(@NotNull TblInstallingTask tblInstallingTask);

    int update(@NotNull TblInstallingTask tblInstallingTask);

    int deleteById(@NotNull String vInstallingTaskId);

    int countAll();

    List<Map> countByStatus();
}
