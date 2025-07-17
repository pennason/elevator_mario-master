package com.shmashine.pm.api.dao;

import javax.validation.constraints.NotNull;

import com.shmashine.pm.api.entity.TblDistributionTaskFile;

public interface TblDistributionTaskFileDao {

    TblDistributionTaskFile getById(@NotNull String vDistributioinTaskFileId);

    TblDistributionTaskFile getByEntity(TblDistributionTaskFile tblInvestigateTaskFile);

    int insert(@NotNull TblDistributionTaskFile tblInvestigateTaskFile);

    int update(@NotNull TblDistributionTaskFile tblInvestigateTaskFile);

    int deleteById(@NotNull String vDistributioinTaskFileId);
}
