package com.shmashine.pm.api.dao;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.pm.api.entity.TblTestingBill;
import com.shmashine.pm.api.entity.dto.TblTestingBillDto;
import com.shmashine.pm.api.module.testingBill.input.TestingBillModule;

public interface BizTestingBillDao {

    TblTestingBillDto getByBizInfo(TblTestingBill tblTestingBill);

    List<Integer> getAllStatus(TestingBillModule testingBillModule);

//    List<Integer> getAllStatusByVillageId(@Param("vVillageId") @NotNull String vVillageId);

    List<Map> getStatusInfo(@Param("vVillageId") @NotNull String vVillageId);

    Map getInfoAndElevatorInfo(TblTestingBill tblTestingBill);

    List<Map> selectByBillModule(TestingBillModule module);
}
