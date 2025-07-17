package com.shmashine.api.service.system;

import java.util.List;

import com.shmashine.api.dao.TblElevatorDao;
import com.shmashine.common.entity.TblElevator;

public interface TblElevatorServiceI {

    TblElevatorDao getTblElevatorDao();

    TblElevator getById(String vElevatorId);

    TblElevator getByElevatorCode(String elevatorCode);

    TblElevator getByEquipmentCode(String equipmentCode);

    TblElevator getByOneOfChoose(String elevatorId, String elevatorCode, String equipmentCode);

    List<TblElevator> getByEntity(TblElevator tblElevator);

    List<TblElevator> listByEntity(TblElevator tblElevator);

    List<TblElevator> listByIds(List<String> ids);

    List<TblElevator> listByCodes(List<String> codes);

    int insert(TblElevator tblElevator);

    int insertIsNotEmpty(TblElevator tblElevator);

    int insertBatch(List<TblElevator> list);

    int update(TblElevator tblElevator);

    int updateBatch(List<TblElevator> list);

    int deleteById(String vElevatorId);

    int deleteByEntity(TblElevator tblElevator);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblElevator tblElevator);

    List<String> checkExistsByCodes(List<String> list);

    int batchAddElevatorAndDeviceByProject(List<String> codes, String projectId, String deviceMark);
}