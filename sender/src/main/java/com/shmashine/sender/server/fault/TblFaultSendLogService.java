package com.shmashine.sender.server.fault;

import java.util.List;

import com.shmashine.common.entity.TblFaultSendLog;
import com.shmashine.sender.dao.TblFaultSendLogDao;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

public interface TblFaultSendLogService {

    TblFaultSendLogDao getTblFaultSendLogDao();

    TblFaultSendLog getById(Integer id);

    List<TblFaultSendLog> getByEntity(TblFaultSendLog tblFaultSendLog);

    List<TblFaultSendLog> listByEntity(TblFaultSendLog tblFaultSendLog);

    List<TblFaultSendLog> listByIds(List<Integer> ids);

    int insert(TblFaultSendLog tblFaultSendLog);

    int insertIsNotEmpty(TblFaultSendLog tblFaultSendLog);

    int insertBatch(List<TblFaultSendLog> list);

    int countAll();

    int countByEntity(TblFaultSendLog tblFaultSendLog);

    /**
     * 获取最近一小时推送失败的故障记录
     *
     * @return 故障列表
     */
    List<TblFaultSendLog> getNeedRetryPushFaults();

    /**
     * 更新推送记录
     *
     * @param fault 更新记录
     */
    void updateById(TblFaultSendLog fault);
}