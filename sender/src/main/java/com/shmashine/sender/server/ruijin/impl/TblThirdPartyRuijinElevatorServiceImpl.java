package com.shmashine.sender.server.ruijin.impl;


import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shmashine.common.entity.TblThirdPartyRuijinElevator;
import com.shmashine.sender.dao.TblThirdPartyRuijinElevatorDao;
import com.shmashine.sender.dataobject.TblThirdPartyRuijinElevatorDO;
import com.shmashine.sender.server.ruijin.TblThirdPartyRuijinElevatorService;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@Service
public class TblThirdPartyRuijinElevatorServiceImpl
        extends ServiceImpl<TblThirdPartyRuijinElevatorDao, TblThirdPartyRuijinElevatorDO>
        implements TblThirdPartyRuijinElevatorService {


    @Override
    public int insert(TblThirdPartyRuijinElevator tblThirdPartyRuijinElevator) {
        return baseMapper.insert(tblThirdPartyRuijinElevator);
    }

    @Override
    public int insertBatch(List<TblThirdPartyRuijinElevator> list) {
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
    public TblThirdPartyRuijinElevator getRjElevatorInfo(String registerNumber) {
        return baseMapper.getRjElevatorInfo(registerNumber);
    }

    @Override
    public TblThirdPartyRuijinElevatorDO getByRegisterNumber(String registerNumber) {
        return list(lambdaQuery()
                .eq(TblThirdPartyRuijinElevatorDO::getRegisterNumber, registerNumber)
                .getWrapper())
                .stream()
                .findFirst()
                .orElse(null);
    }
}