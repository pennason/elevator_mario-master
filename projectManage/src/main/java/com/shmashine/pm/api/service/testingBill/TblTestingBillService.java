package com.shmashine.pm.api.service.testingBill;

import java.util.List;

import com.shmashine.pm.api.dao.TblTestingBillDao;
import com.shmashine.pm.api.entity.TblTestingBill;

public interface TblTestingBillService {

    TblTestingBillDao getTblTestingBillDao();

    TblTestingBill getById(String vTestingBillId);

    TblTestingBill getByEntity(TblTestingBill tblTestingBill);

    int insert(TblTestingBill tblTestingBill);

    int update(TblTestingBill tblTestingBill);

    int deleteById(String vTestingBillId);

    int insertBatch(List<TblTestingBill> list);


}
