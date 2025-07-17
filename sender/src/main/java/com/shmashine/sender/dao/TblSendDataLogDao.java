package com.shmashine.sender.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.shmashine.common.entity.TblSendDataLog;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@Mapper
public interface TblSendDataLogDao {

    //通过目标城市区号查询发送数据的信息
    List<TblSendDataLog> getSendDataLogByCode(String areaCode);

    //新增发送数据的信息
    int insertSendDataLog(TblSendDataLog tblSendDataLog);
}
