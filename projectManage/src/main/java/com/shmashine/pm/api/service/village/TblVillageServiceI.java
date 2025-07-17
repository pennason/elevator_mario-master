package com.shmashine.pm.api.service.village;

import java.util.List;

import com.shmashine.common.entity.TblVillage;
import com.shmashine.pm.api.dao.TblVillageDao;

/**
 * TblVillageService
 */

public interface TblVillageServiceI {

    TblVillageDao getTblVillageDao();

    TblVillage getById(String vVillageId);

    List<TblVillage> getByEntity(TblVillage tblVillage);

    List<TblVillage> listByEntity(TblVillage tblVillage);

    List<TblVillage> listByIds(List<String> ids);

    int insert(TblVillage tblVillage);

    int insertBatch(List<TblVillage> list);

    int update(TblVillage tblVillage);

    int updateBatch(List<TblVillage> list);

    int deleteById(String vVillageId);

    int deleteByEntity(TblVillage tblVillage);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblVillage tblVillage);

    int existsByName(String vVillageName, String vProjectId);
}
