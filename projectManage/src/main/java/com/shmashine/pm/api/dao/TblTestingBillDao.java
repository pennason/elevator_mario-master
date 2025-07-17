package com.shmashine.pm.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.shmashine.pm.api.entity.TblTestingBill;

public interface TblTestingBillDao {

    TblTestingBill getById(@NotNull String vTestingBillId);

    List<TblTestingBill> listByEntity(TblTestingBill tblTestingBill);

    TblTestingBill getByEntity(TblTestingBill tblTestingBill);

    int insert(@NotNull TblTestingBill tblTestingBill);

    int update(@NotNull TblTestingBill tblTestingBill);

    int deleteById(@NotNull String vTestingBillId);

    int insertBatch(@NotEmpty List<TblTestingBill> list);

    int countAll();
}
