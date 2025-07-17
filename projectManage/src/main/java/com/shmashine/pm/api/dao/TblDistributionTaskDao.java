package com.shmashine.pm.api.dao;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.shmashine.pm.api.entity.TblDistributionTask;

public interface TblDistributionTaskDao {

    TblDistributionTask getById(@NotNull String vDistributionTaskId);

    List<TblDistributionTask> listByEntity(TblDistributionTask tblDistributionTask);

    List<TblDistributionTask> getByEntity(TblDistributionTask tblDistributionTask);

    int insert(@NotNull TblDistributionTask tblDistributionTask);

    int update(@NotNull TblDistributionTask tblDistributionTask);

    int deleteById(@NotNull String vDistributionTaskId);

    int countAll();

    List<Map> countByStatus();
}
