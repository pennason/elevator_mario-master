package com.shmashine.pm.api.dao;

import java.util.List;

import com.shmashine.pm.api.entity.TblInstallingBill;

public interface TblInstallingBillDao {

    TblInstallingBill getById(String vInstallBillId);

    TblInstallingBill getByEntity(TblInstallingBill tblInstallingBill);

    int insert(TblInstallingBill tblInstallingBill);

    int insertBatch(List<TblInstallingBill> list);

    int update(TblInstallingBill tblInstallingBill);

    int deleteById(String vInstallingBillId);

    List<TblInstallingBill> selectByTaskId(String vInstallingTaskId);
}
