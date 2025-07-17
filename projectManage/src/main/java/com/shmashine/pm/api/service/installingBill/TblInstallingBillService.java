package com.shmashine.pm.api.service.installingBill;

import java.util.List;

import com.shmashine.pm.api.dao.TblInstallingBillDao;
import com.shmashine.pm.api.entity.TblInstallingBill;

public interface TblInstallingBillService {

    TblInstallingBillDao getTblInstallingBillDao();

    TblInstallingBill getById(String vInstallingBillId);

    TblInstallingBill getByEntity(TblInstallingBill tblInstallingBill);

//    TblInstallingBill getByInstallingTaskId(String investigateTaskId);

    int insert(TblInstallingBill tblInstallingBill);

    int update(TblInstallingBill tblInstallingBill);

    int deleteById(String vInstallingBillId);

    int insertBatch(List<TblInstallingBill> list);

    List<TblInstallingBill> selectByTaskId(String vInstallingTaskId);
}
