package com.shmashine.socket.nezha.service.impl;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.socket.nezha.dao.ElevatorEventRecordDao;
import com.shmashine.socket.nezha.domain.ElevatorEventRecordDO;
import com.shmashine.socket.nezha.service.ElevatorEventRecordService;

/**
 * 电梯事件记录（上下线）
 */
@Service
public class ElevatorEventRecordServiceImpl implements ElevatorEventRecordService {


    @Autowired
    private ElevatorEventRecordDao elevatorEventRecordDao;

    @Override
    public ElevatorEventRecordDO get(Long id) {
        return elevatorEventRecordDao.get(id);
    }

    @Override
    public List<ElevatorEventRecordDO> list(Map<String, Object> map) {
        return elevatorEventRecordDao.list(map);
    }

    @Override
    public List<Map<String, Object>> freqsortlist(Map<String, Object> map) {
        return elevatorEventRecordDao.freqsort();
    }

    @Override
    public List<Map<String, Object>> trendmap(Map<String, Object> map) {
        return elevatorEventRecordDao.trendmap();

    }

    @Override
    public int count(Map<String, Object> map) {
        return elevatorEventRecordDao.count(map);
    }

    @Override
    public int save(ElevatorEventRecordDO elevatorEventRecord) {
        return elevatorEventRecordDao.save(elevatorEventRecord);
    }

    @Override
    public ElevatorEventRecordDO getLatest(String code) {
        return elevatorEventRecordDao.getLatest(code);
    }

}
