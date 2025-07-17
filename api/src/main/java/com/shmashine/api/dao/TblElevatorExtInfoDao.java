package com.shmashine.api.dao;

import com.shmashine.api.entity.TblElevatorExtInfo;


public interface TblElevatorExtInfoDao {

    int insert(TblElevatorExtInfo tblElevatorExtInfo);

    int update(TblElevatorExtInfo tblElevatorExtInfo);

    TblElevatorExtInfo getInfoByEntity(TblElevatorExtInfo tblElevatorExtInfo);
}
