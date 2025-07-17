package com.shmashine.api.service.fujitec.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.api.dao.TblElevatorFujitecDao;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.service.fujitec.TblElevatorFujitecService;
import com.shmashine.common.constants.SystemConstants;
import com.shmashine.common.entity.TblElevatorFujitec;

@Service
public class TblElevatorFujitecServiceImpl implements TblElevatorFujitecService {

    @Resource
    private TblElevatorFujitecDao elevatorFujitecDao;

    @Override
    public List<TblElevatorFujitec> getElevatorByCondition(TblElevatorFujitec elevatorFujitec) {
        return elevatorFujitecDao.getElevatorByCondition(elevatorFujitec);
    }

    @Override
    public List<TblElevatorFujitec> getAllElevator() {
        return elevatorFujitecDao.getAllElevator();

    }

    @Override
    public TblElevatorFujitec getElevatorByProductId(String ProductId) {
        return elevatorFujitecDao.getElevatorByProductId(ProductId);
    }

    @Override
    public PageListResultEntity getElevatorByConditionWithPage(TblElevatorFujitec elevatorFujitec) {
        Integer pageIndex = elevatorFujitec.getPageIndex();
        Integer pageSize = elevatorFujitec.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;

        }

        PageHelper.startPage(pageIndex, pageSize);

        PageInfo<Map<String, Object>> hashMapPageInfo = new PageInfo(elevatorFujitecDao.getElevatorByCondition(elevatorFujitec), pageSize);

        return new PageListResultEntity(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(), hashMapPageInfo.getList());
    }


    @Override
    public int insertElevator(TblElevatorFujitec elevatorFujitec) {
        return elevatorFujitecDao.insertElevator(elevatorFujitec);
    }

    @Override
    public int deleteElevatorById(String id) {
        return elevatorFujitecDao.deleteElevatorById(id);
    }

    @Override
    public int updateElevatorFujitecInfo(TblElevatorFujitec tblElevatorFujitec) {
        return elevatorFujitecDao.updateElevatorFujitecInfo(tblElevatorFujitec);
    }

    @Override
    public int insertElevatorFujitecInfo(TblElevatorFujitec tblElevatorFujitec) {
        return elevatorFujitecDao.insertElevatorFujitecInfo(tblElevatorFujitec);
    }
}
