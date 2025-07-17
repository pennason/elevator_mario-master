package com.shmashine.pm.api.service.checkingBill;

import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.entity.TblCheckingBill;
import com.shmashine.pm.api.module.checkingBill.CheckingBillModule;

public interface TblCheckingBillService {

    int insert(TblCheckingBill record);

    int batchInsert(List<TblCheckingBill> list);

    int update(TblCheckingBill record);

    List<TblCheckingBill> selectListByEntity(TblCheckingBill record);

    TblCheckingBill selectByCheckingTaskBillId(String vCheckingTaskBillId);

    TblCheckingBill selectByElevatorCode(String vElevatorCode);

    List<Integer> getAllStatus(CheckingBillModule checkingBillModule);

//    List<Integer> getAllStatusByVillageId(String vVillageId);

    List<Map> selectByBillModule(CheckingBillModule checkingBillModule);

    TblCheckingBill selectByBillId(String tblCheckingBillId);
}
