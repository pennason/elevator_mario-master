package com.shmashine.pm.api.service.installingBill;

import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.entity.dto.TblInstallingBillDto;
import com.shmashine.pm.api.module.installingBill.InstallingBillModule;

public interface BizInstallingBillService {

    List<Map> getStatusInfo(String vVillageId);

    TblInstallingBillDto getBizInfoById(String vInstallingBillId);

    List<Integer> getAllStatus(InstallingBillModule installingBillModule);

//    List<Integer> getAllStatusByVillageId(String vVillageId);

    List<Map> selectByBillModule(InstallingBillModule module);
}
