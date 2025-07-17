package com.shmashine.socket.elevator.service.impl;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.shmashine.socket.elevator.dao.TblRedirectElevatorMappingDao;
import com.shmashine.socket.elevator.entity.TblRedirectElevatorMapping;
import com.shmashine.socket.elevator.service.TblRedirectElevatorMappingService;

/**
 * 重定向电梯映射服务实现
 */
@Service("tblRedirectElevatorMappingService")
public class TblRedirectElevatorMappingServiceImpl implements TblRedirectElevatorMappingService {

    @Resource
    private TblRedirectElevatorMappingDao tblRedirectElevatorMappingDao;

    @Override
    @Cacheable(value = "redirectElevatorMapping")
    public TblRedirectElevatorMapping getByElevatorCode(String elevatorCode) {
        return tblRedirectElevatorMappingDao.getByElevatorCode(elevatorCode);
    }

    @Override
    @CacheEvict(value = "redirectElevatorMapping", allEntries = true)
    public void clear() {
    }

}