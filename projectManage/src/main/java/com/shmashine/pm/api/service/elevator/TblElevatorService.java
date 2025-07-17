package com.shmashine.pm.api.service.elevator;

import java.util.List;
import java.util.Map;

import com.shmashine.common.entity.TblElevator;
import com.shmashine.pm.api.dao.TblElevatorDao;

public interface TblElevatorService {

    TblElevatorDao getTblElevatorDao();

    TblElevator getById(String vElevatorId);

    List<TblElevator> getByEntity(TblElevator tblElevator);

    List<TblElevator> listByEntity(TblElevator tblElevator);

    List<TblElevator> listByIds(List<String> ids);

    List<TblElevator> listByCodes(List<String> codes);

    int insert(TblElevator tblElevator);

    int insertIsNotEmpty(TblElevator tblElevator);

    int insertBatch(List<TblElevator> list);

    int insertBatchParty(List<TblElevator> list);

    int update(TblElevator tblElevator);

    int updateBatch(List<TblElevator> list);

    int deleteById(String vElevatorId);

    int deleteByEntity(TblElevator tblElevator);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblElevator tblElevator);

    List<String> checkExistsByCodes(List<String> list);

//    int batchAddElevatorAndDeviceByProject(List<String> codes,String projectId, String deviceMark);

    int updateElevatorCodeBatch(List<TblElevator> list);

    List<Map> countWithPmStatus(String vVillageId);

    Map getRelatedInfoById(String elevatorId);

    TblElevator getByElevatorCode(String vElevatorCode);
}
