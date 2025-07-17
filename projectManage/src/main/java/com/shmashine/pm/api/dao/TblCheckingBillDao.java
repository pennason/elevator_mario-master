package com.shmashine.pm.api.dao;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.pm.api.entity.TblCheckingBill;
import com.shmashine.pm.api.module.checkingBill.CheckingBillModule;

public interface TblCheckingBillDao {

    int insert(TblCheckingBill record);

    int batchInsert(List<TblCheckingBill> list);

    int update(TblCheckingBill record);

    List<TblCheckingBill> selectListByEntity(TblCheckingBill record);

    List<TblCheckingBill> selectByCheckingTaskId(@Param("vCheckingTaskId") @NotNull String vCheckingTaskId);

    TblCheckingBill selectByElevatorCode(@Param("vElevatorCode") @NotNull String vElevatorCode);

    List<Integer> getAllStatus(CheckingBillModule checkingBillModule);

//    List<Integer> getAllStatusByVillageId(@Param("vVillageId") @NotNull String vVillageId);

    List<Map> selectByBillModule(CheckingBillModule checkingBillModule);

    TblCheckingBill selectByBillId(@Param("vCheckingBillId") String tblCheckingBillId);
}
