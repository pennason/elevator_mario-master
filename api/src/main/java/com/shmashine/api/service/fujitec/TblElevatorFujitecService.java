package com.shmashine.api.service.fujitec;

import java.util.List;

import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.common.entity.TblElevatorFujitec;

public interface TblElevatorFujitecService {

    //查询电梯
    List<TblElevatorFujitec> getElevatorByCondition(TblElevatorFujitec elevatorFujitec);

    //查询所有电梯
    List<TblElevatorFujitec> getAllElevator();

    //通过电梯编号获取电梯信息
    TblElevatorFujitec getElevatorByProductId(String ProductId);

    //查询电梯(带分页)
    PageListResultEntity getElevatorByConditionWithPage(TblElevatorFujitec elevatorFujitec);

    //新增电梯
    int insertElevator(TblElevatorFujitec elevatorFujitec);

    //根据id删除电梯
    int deleteElevatorById(String id);

    //更新电梯信息
    int updateElevatorFujitecInfo(TblElevatorFujitec tblElevatorFujitec);

    //新增电梯信息
    int insertElevatorFujitecInfo(TblElevatorFujitec tblElevatorFujitec);
}
