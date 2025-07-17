package com.shmashine.socket.fault.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.socket.fault.dao.TblFaultShieldDao;
import com.shmashine.socket.fault.entity.TblFaultShield;
import com.shmashine.socket.fault.service.TblFaultShieldService;

/**
 * 故障上报屏蔽规则表(TblFaultShield)表服务实现类
 *
 * @author little.li
 * @since 2020-06-14 15:18:17
 */
@Service("tblFaultShieldService")
public class TblFaultShieldServiceImpl implements TblFaultShieldService {


    @Resource
    private TblFaultShieldDao tblFaultShieldDao;


    /**
     * 获取所有故障屏蔽规则
     *
     * @param map 屏蔽规则缓存
     */
    @Override
    public List<TblFaultShield> list(Map<String, Object> map) {
        return tblFaultShieldDao.list(map);
    }

    @Override
    public void updateFaultShieldId(String elevatorCode, Integer faultType, long id) {
        tblFaultShieldDao.updateFaultShieldId(elevatorCode, faultType, String.valueOf(id));
    }


}