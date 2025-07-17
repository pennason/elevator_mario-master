package com.shmashine.fault.task.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.shmashine.common.utils.SnowFlakeUtils;
import com.shmashine.fault.task.dao.BizEnventNoticTaskDao;
import com.shmashine.fault.task.entity.TblEnventNotice;
import com.shmashine.fault.task.service.BizEnventNoticTaskServer;

/**
 * BizEnventNoticTaskServerImpl
 */
@Service
public class BizEnventNoticTaskServerImpl implements BizEnventNoticTaskServer {

    @Autowired
    private BizEnventNoticTaskDao bizEnventNoticTaskDao;

    @Override
    public void saveYesterdayElevatorFaultStatics() {

        // 查询
        List<Map> list = bizEnventNoticTaskDao.getYesterdayElevatorFaultStatics();
        List<TblEnventNotice> noticeList = Lists.newArrayList();
        if (list != null) {
            for (Map map : list) {
                TblEnventNotice notice = new TblEnventNotice();
                notice.setVEnventNotifyId(SnowFlakeUtils.nextStrId());
                notice.setVElevatorCode(String.valueOf(map.get("")));
                notice.setVNoticeType("3");
                String message = "";
                notice.setVMessage(message);
            }
        }
        bizEnventNoticTaskDao.insertBatch(noticeList);

    }
}
