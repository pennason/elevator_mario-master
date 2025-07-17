package com.shmashine.api.service.village;

import java.util.List;
import java.util.Map;

import com.shmashine.api.dao.TblVillageDao;
import com.shmashine.common.entity.TblVillage;

public interface TblVillageServiceI {

    TblVillageDao getTblVillageDao();

    TblVillage getById(String vVillageId);

    List<TblVillage> getByEntity(TblVillage tblVillage);

    List<TblVillage> listByEntity(TblVillage tblVillage);

    List<TblVillage> listByIds(List<String> ids);

    List<String> getVillageIdsByProjectId(String projectId);

    int insert(TblVillage tblVillage);

    int insertBatch(List<TblVillage> list);

    int update(TblVillage tblVillage);

    int updateBatch(List<TblVillage> list);

    int deleteById(String vVillageId);

    int deleteByEntity(TblVillage tblVillage);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblVillage tblVillage);

    void extendVillageInfo(List<Map> list);

    TblVillage getVillageByProjectIdAndVillageName(String projectId, String villageName);
}