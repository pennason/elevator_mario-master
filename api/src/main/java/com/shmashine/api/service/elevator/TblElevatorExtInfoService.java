package com.shmashine.api.service.elevator;


import com.shmashine.api.entity.TblElevatorExtInfo;

public interface TblElevatorExtInfoService {

    int insert(TblElevatorExtInfo tblElevatorExtInfo);

    int update(TblElevatorExtInfo tblElevatorExtInfo);

    TblElevatorExtInfo getInfoByEntity(TblElevatorExtInfo tblElevatorExtInfo);
}
