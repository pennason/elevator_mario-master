package com.shmashine.api.service.building;

import java.util.List;

import com.shmashine.api.dao.TblBuildingDao;
import com.shmashine.common.entity.TblBuilding;

public interface TblBuildingServiceI {

    TblBuildingDao getTblBuildingDao();

    TblBuilding getById(String vVillageId);

    List<TblBuilding> getByEntity(TblBuilding tblBuilding);

    List<TblBuilding> listByEntity(TblBuilding tblBuilding);

    List<TblBuilding> listByIds(List<String> ids);

    int insert(TblBuilding tblBuilding);

    int insertBatch(List<TblBuilding> list);

    int update(TblBuilding tblBuilding);

    int updateBatch(List<TblBuilding> list);

    int deleteById(String vVillageId, String vBuildingId);

    int deleteByEntity(TblBuilding tblBuilding);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblBuilding tblBuilding);
}