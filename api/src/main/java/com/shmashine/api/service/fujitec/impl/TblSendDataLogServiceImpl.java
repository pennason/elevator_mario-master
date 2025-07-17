package com.shmashine.api.service.fujitec.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.api.dao.TblSendDataLogDao;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.service.fujitec.TblSendDataLogService;
import com.shmashine.common.constants.SystemConstants;
import com.shmashine.common.entity.TblSendDataLog;

@Service
public class TblSendDataLogServiceImpl implements TblSendDataLogService {

    @Resource
    private TblSendDataLogDao sendDataLogDao;


    @Override
    public List<TblSendDataLog> getSendDataLogByCondition(TblSendDataLog tblSendDataLog) {
        return sendDataLogDao.getSendDataLogByCondition(tblSendDataLog);
    }

    @Override
    public PageListResultEntity getSendDataLogByConditionWithPage(TblSendDataLog tblSendDataLog) {
        Integer pageIndex = tblSendDataLog.getPageIndex();
        Integer pageSize = tblSendDataLog.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;

        }

        PageHelper.startPage(pageIndex, pageSize);

        PageInfo<Map<String, Object>> hashMapPageInfo = new PageInfo(sendDataLogDao.getSendDataLogByCondition(tblSendDataLog), pageSize);

        return new PageListResultEntity(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(), hashMapPageInfo.getList());
    }

    @Override
    public TblSendDataLog getSendDataLogById(String id) {
        return sendDataLogDao.getSendDataLogById(id);
    }

    @Override
    public int updateSendDataLog(TblSendDataLog tblSendDataLog) {
        return sendDataLogDao.updateSendDataLog(tblSendDataLog);
    }

    //新增发送数据的信息
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertSendDataLog(TblSendDataLog tblSendDataLog) {
        return sendDataLogDao.insertSendDataLog(tblSendDataLog);
    }
}
