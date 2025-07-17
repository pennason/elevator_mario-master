package com.shmashine.pm.api.dao;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.shmashine.pm.api.entity.TblCheckingTask;

public interface TblCheckingTaskDao {

    TblCheckingTask getById(@NotNull String vCheckingTaskId);

    List<TblCheckingTask> listByEntity(TblCheckingTask tblCheckingTask);

    TblCheckingTask getByEntity(TblCheckingTask tblCheckingTask);

    int insert(@NotNull TblCheckingTask tblCheckingTask);

    int update(@NotNull TblCheckingTask tblCheckingTask);

    int deleteById(@NotNull String vCheckingTaskId);

    int countAll();

    Map countByStatus();
}
