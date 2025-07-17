package com.shmashine.pm.api.service.testingBill;

import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.entity.TblTestingBill;
import com.shmashine.pm.api.entity.dto.TblTestingBillDto;
import com.shmashine.pm.api.module.testingBill.input.TestingBillModule;

/**
 * 默认说明
 */

public interface BizTestingBillService {

    TblTestingBillDto getByBizInfo(TblTestingBill tblTestingBill);

    List<Integer> getAllStatus(TestingBillModule module);

    List<Map> getStatusInfo(String vVillageId);

    Map getInfoAndElevatorInfo(TblTestingBill tblTestingBill);

    List<Map> selectByBillModule(TestingBillModule module);
}
