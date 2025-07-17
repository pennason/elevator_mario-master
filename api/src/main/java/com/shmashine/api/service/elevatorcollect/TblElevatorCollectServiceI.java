package com.shmashine.api.service.elevatorcollect;

import java.util.List;

import com.shmashine.api.dao.TblElevatorCollectDao;
import com.shmashine.common.entity.TblElevatorCollect;

public interface TblElevatorCollectServiceI {

    TblElevatorCollectDao getTblElevatorCollectDao();

    TblElevatorCollect getById(String vUserId);

    List<TblElevatorCollect> getByEntity(TblElevatorCollect tblElevatorCollect);

    List<TblElevatorCollect> listByEntity(TblElevatorCollect tblElevatorCollect);

    List<TblElevatorCollect> listByIds(List<String> ids);

    int insert(TblElevatorCollect tblElevatorCollect);

    int insertBatch(List<TblElevatorCollect> list);

    int update(TblElevatorCollect tblElevatorCollect);

    int updateBatch(List<TblElevatorCollect> list);

    int deleteById(String vUserId, String vElevatorId);

    int deleteByEntity(TblElevatorCollect tblElevatorCollect);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblElevatorCollect tblElevatorCollect);
}