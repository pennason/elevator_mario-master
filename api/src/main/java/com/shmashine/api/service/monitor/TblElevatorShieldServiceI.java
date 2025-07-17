package com.shmashine.api.service.monitor;

import java.util.List;

import com.shmashine.api.dao.TblElevatorShieldDao;
import com.shmashine.common.entity.TblElevatorShield;

public interface TblElevatorShieldServiceI {

    TblElevatorShieldDao getTblElevatorShieldDao();

    TblElevatorShield getById(String vElevatorShieldId);

    List<TblElevatorShield> getByEntity(TblElevatorShield tblElevatorShield);

    List<TblElevatorShield> listByEntity(TblElevatorShield tblElevatorShield);

    List<TblElevatorShield> listByIds(List<String> ids);

    int insert(TblElevatorShield tblElevatorShield);

    int insertBatch(List<TblElevatorShield> list);

    int update(TblElevatorShield tblElevatorShield);

    int updateBatch(List<TblElevatorShield> list);

    int deleteById(String vElevatorShieldId);

    int deleteByEntity(TblElevatorShield tblElevatorShield);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblElevatorShield tblElevatorShield);
}