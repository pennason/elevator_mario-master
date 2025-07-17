package com.shmashine.api.service.fujitec;

import java.util.List;

import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.common.entity.TblSendDataLog;

/**
 * SendDataLogService
 *
 * @author chenx
 */

public interface TblSendDataLogService {
    //查询推送信息日志
    List<TblSendDataLog> getSendDataLogByCondition(TblSendDataLog tblSendDataLog);

    //查询推送信息日志(带分页)
    PageListResultEntity getSendDataLogByConditionWithPage(TblSendDataLog tblSendDataLog);

    //根据id查询推送信息日志
    TblSendDataLog getSendDataLogById(String id);

    //更新推送信息日志
    int updateSendDataLog(TblSendDataLog tblSendDataLog);

    //新增发送数据的信息
    int insertSendDataLog(TblSendDataLog tblSendDataLog);
}
