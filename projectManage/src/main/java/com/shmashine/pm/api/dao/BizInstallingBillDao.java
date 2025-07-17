package com.shmashine.pm.api.dao;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.pm.api.entity.dto.TblInstallingBillDto;
import com.shmashine.pm.api.module.installingBill.InstallingBillModule;

public interface BizInstallingBillDao {

    List<Map> getStatusInfo(@Param("vVillageId") @NotNull String vVillageId);

    TblInstallingBillDto getBizInfoById(@Param("vInstallingBillId") @NotNull String vInstallingBillId);

    List<Integer> getAllStatus(InstallingBillModule installingBillModule);

//    List<Integer> getAllStatusByVillageId(@Param("vVillageId") @NotNull String vVillageId);

    List<Map> selectByBillModule(InstallingBillModule module);
}
