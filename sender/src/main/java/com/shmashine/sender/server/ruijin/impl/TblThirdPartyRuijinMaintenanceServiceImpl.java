package com.shmashine.sender.server.ruijin.impl;


import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shmashine.common.entity.TblThirdPartyRuijinMaintenance;
import com.shmashine.sender.dao.TblThirdPartyRuijinMaintenanceDao;
import com.shmashine.sender.dataobject.TblThirdPartyRuijinMaintenanceDO;
import com.shmashine.sender.server.ruijin.TblThirdPartyRuijinMaintenanceService;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@Service
public class TblThirdPartyRuijinMaintenanceServiceImpl
        extends ServiceImpl<TblThirdPartyRuijinMaintenanceDao, TblThirdPartyRuijinMaintenanceDO>
        implements TblThirdPartyRuijinMaintenanceService {


    @Override
    public int insert(TblThirdPartyRuijinMaintenance tblThirdPartyRuijinMaintenance) {
        return baseMapper.insert(tblThirdPartyRuijinMaintenance);
    }

    @Override
    public int insertBatch(List<TblThirdPartyRuijinMaintenance> list) {
        return baseMapper.insertBatch(list);
    }

    @Override
    public int deleteById(String registerNumber) {
        return baseMapper.deleteById(registerNumber);
    }

    @Override
    public int deleteAll() {
        return baseMapper.deleteAll();
    }

    @Override
    public TblThirdPartyRuijinMaintenance getMaintenanceInfoByRegisternumber(String registerNumber) {
        return baseMapper.getMaintenanceInfoByRegisternumber(registerNumber);
    }
}