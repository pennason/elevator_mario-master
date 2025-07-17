package com.shmashine.pm.api.dao;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.shmashine.pm.api.entity.TblInvestigateTask;

public interface TblInvestigateTaskDao {

    TblInvestigateTask getById(@NotNull String vInvestigateTaskId);

    List<TblInvestigateTask> listByEntity(TblInvestigateTask tblInvestigateTask);

    List<TblInvestigateTask> getByEntity(TblInvestigateTask tblInvestigateTask);

    int insert(@NotNull TblInvestigateTask tblInvestigateTask);

    int update(@NotNull TblInvestigateTask tblInvestigateTask);

    int deleteById(@NotNull String vInvestigateTaskId);

    int countAll();

    List<Map> countByStatus(String principalId);
}
