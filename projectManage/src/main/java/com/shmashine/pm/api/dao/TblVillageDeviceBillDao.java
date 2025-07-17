package com.shmashine.pm.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.shmashine.common.entity.TblVillageDeviceBill;

public interface TblVillageDeviceBillDao {
    TblVillageDeviceBill getById(@NotNull String vVillageDeviceBillId);

    TblVillageDeviceBill getByVillageId(@NotNull String vVillageId);

    //    List<TblVillageDeviceBill> listByEntity(TblVillageDeviceBill tblVillageDeviceBill);

    List<TblVillageDeviceBill> getByEntity(TblVillageDeviceBill tblVillageDeviceBill);

    int insert(@NotNull TblVillageDeviceBill tblVillageDeviceBill);

    int update(@NotNull TblVillageDeviceBill tblVillageDeviceBill);

    int deleteById(@NotNull String vVillageDeviceBillId);

    int deleteByIds(@NotEmpty List<String> list);
}
