package com.shmashine.pm.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.shmashine.pm.api.entity.TblInvestigateBill;

public interface TblInvestigateBillDao {

    TblInvestigateBill getById(@NotNull String vInvestigateBillId);

    TblInvestigateBill getByElevatorId(@NotNull String vElevatorId);

    List<TblInvestigateBill> listByEntity(TblInvestigateBill tblInvestigateBill);

    List<TblInvestigateBill> getByEntity(TblInvestigateBill tblInvestigateBill);

    int insert(@NotNull TblInvestigateBill tblInvestigateBill);

    int insertBatch(@NotEmpty List<TblInvestigateBill> list);

    int update(@NotNull TblInvestigateBill tblInvestigateBill);

    int deleteById(@NotNull String vInvestigateBillId);

    int deleteByIds(@NotEmpty List<String> list);

}
