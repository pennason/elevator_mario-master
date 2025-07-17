package com.shmashine.pm.api.service.villageDeviceBill;

import com.shmashine.common.entity.TblVillageDeviceBill;
import com.shmashine.pm.api.dao.TblVillageDeviceBillDao;

public interface TblVillageDeviceBillService {

    TblVillageDeviceBillDao getTblVillageDeviceBillDao();

    TblVillageDeviceBill getById(String vVillageDeviceBillId);

    TblVillageDeviceBill getByVillageId(String vVillageId);

    //    List<TblVillageDeviceBill> listByEntity(TblVillageDeviceBill tblVillageDeviceBill);

    int insert(TblVillageDeviceBill tblVillageDeviceBill);

    int update(TblVillageDeviceBill tblVillageDeviceBill);

    int deleteById(String vVillageDeviceBillId);


}
