package com.shmashine.socket.nezha.service.impl;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.socket.nezha.dao.FailureShieldDao;
import com.shmashine.socket.nezha.domain.FailureShieldDO;
import com.shmashine.socket.nezha.service.FailureShieldService;

/**
 * 故障上报规则表
 *
 * @author little.li
 */
@Service
public class FailureShieldServiceImpl implements FailureShieldService {


    private final FailureShieldDao failureShieldDao;


    @Autowired
    public FailureShieldServiceImpl(FailureShieldDao failureShieldDao) {
        this.failureShieldDao = failureShieldDao;
    }


    @Override
    public List<FailureShieldDO> list(Map<String, Object> map) {
        return failureShieldDao.list(map);
    }


}
