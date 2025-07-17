package com.shmashine.sender.server.ruijin.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.common.entity.TblThirdPartyRuijinWorkOrder;
import com.shmashine.sender.dao.TblThirdPartyRuijinWorkOrderDao;
import com.shmashine.sender.server.ruijin.TblThirdPartyRuijinWorkOrderService;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@Service
public class TblThirdPartyRuijinWorkOrderServiceImpl implements TblThirdPartyRuijinWorkOrderService {

    @Resource
    private TblThirdPartyRuijinWorkOrderDao tblThirdPartyRuijinWorkOrderDao;


    @Override
    public int insert(TblThirdPartyRuijinWorkOrder tblThirdPartyRuijinWorkOrder) {
        return tblThirdPartyRuijinWorkOrderDao.insert(tblThirdPartyRuijinWorkOrder);
    }

    @Override
    public int insertBatch(List<TblThirdPartyRuijinWorkOrder> list) {
        return tblThirdPartyRuijinWorkOrderDao.insertBatch(list);
    }

    @Override
    public int deleteByWorkOrderNumber(String registerNumber) {
        return tblThirdPartyRuijinWorkOrderDao.deleteByWorkOrderNumber(registerNumber);
    }

    @Override
    public int deleteAll() {
        return tblThirdPartyRuijinWorkOrderDao.deleteAll();
    }

    @Override
    public String getWorkOrderNumber(String workOrderNumber) {
        return tblThirdPartyRuijinWorkOrderDao.getWorkOrderNumber(workOrderNumber);
    }

    @Override
    public List<String> searchWorkOrderNumberByWorkOrderNumbers(List<String> regsNumber) {
        return tblThirdPartyRuijinWorkOrderDao.searchWorkOrderNumberByWorkOrderNumbers(regsNumber);
    }
}