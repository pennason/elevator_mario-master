package com.shmashine.pm.api.service.investigateBill;

import java.util.List;

import com.shmashine.pm.api.entity.TblInvestigateBill;

public interface TblInvestigateBillService {

    TblInvestigateBill getById(String vInvestigateBillId);

    TblInvestigateBill getByElevatorId(String vElevatorId);

    int insert(TblInvestigateBill tblInvestigateBill);

    int insertBatch(List<TblInvestigateBill> list);

    int update(TblInvestigateBill tblInvestigateBill);

    int deleteById(String vInvestigateBillId);

    List<TblInvestigateBill> listByEntity(TblInvestigateBill tblInvestigateBill);
}
