package com.shmashine.pm.api.dao;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.shmashine.pm.api.entity.TblTestingTask;

public interface TblTestingTaskDao {

    TblTestingTask getById(@NotNull String vTestingTaskId);

    List<TblTestingTask> listByEntity(TblTestingTask tblTestingTask);

    TblTestingTask getByEntity(TblTestingTask tblTestingTask);

    int insert(@NotNull TblTestingTask tblTestingTask);

    int update(@NotNull TblTestingTask tblTestingTask);

    int deleteById(@NotNull String vTestingTaskId);

    int countAll();

    List<Map> countByStatus();
}
