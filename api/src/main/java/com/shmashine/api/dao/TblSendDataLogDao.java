package com.shmashine.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.shmashine.common.entity.TblSendDataLog;

/**
 * senddatalog
 *
 * @author chenx
 */

@Mapper
public interface TblSendDataLogDao {

    //查询推送信息日志
    List<TblSendDataLog> getSendDataLogByCondition(TblSendDataLog tblSendDataLog);

    //根据id查询推送信息日志
    TblSendDataLog getSendDataLogById(String id);

    //更新推送信息日志
    int updateSendDataLog(TblSendDataLog tblSendDataLog);

    //新增发送数据的信息
    int insertSendDataLog(TblSendDataLog tblSendDataLog);
}
