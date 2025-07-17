package com.shmashine.pm.api.dao;

import javax.validation.constraints.NotNull;

import com.shmashine.pm.api.entity.TblInvestigateOnceBill;

public interface TblInvestigateOnceBillDao {

    TblInvestigateOnceBill getById(@NotNull String vInvestigateOnceBillId);

    TblInvestigateOnceBill getByTaskId(@NotNull String vInvestigateTaskId);

    TblInvestigateOnceBill getByElevatorCode(@NotNull String vElevatorCode);

    int insert(@NotNull TblInvestigateOnceBill tblInvestigateOnceBill);

    int update(@NotNull TblInvestigateOnceBill tblInvestigateOnceBill);

    int deleteById(@NotNull String vInvestigateOnceBillId);
}
