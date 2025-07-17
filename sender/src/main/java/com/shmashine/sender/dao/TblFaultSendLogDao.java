package com.shmashine.sender.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblFaultSendLog;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@Mapper
public interface TblFaultSendLogDao {

    TblFaultSendLog getById(@NotNull Integer id);

    List<TblFaultSendLog> listByEntity(TblFaultSendLog tblFaultSendLog);

    List<TblFaultSendLog> getByEntity(TblFaultSendLog tblFaultSendLog);

    List<TblFaultSendLog> listByIds(@NotEmpty List<Integer> list);

    int insert(@NotNull TblFaultSendLog tblFaultSendLog);

    int insertIsNotEmpty(@NotNull TblFaultSendLog tblFaultSendLog);

    int insertBatch(@NotEmpty List<TblFaultSendLog> list);

    int update(@NotNull TblFaultSendLog tblFaultSendLog);

    int updateByField(@NotNull @Param("where") TblFaultSendLog where, @NotNull @Param("set") TblFaultSendLog set);

    int updateBatch(@NotEmpty List<TblFaultSendLog> list);

    int deleteById(@NotNull Integer id);

    int deleteByEntity(@NotNull TblFaultSendLog tblFaultSendLog);

    int deleteByIds(@NotEmpty List<Integer> list);

    int countAll();

    int countByEntity(TblFaultSendLog tblFaultSendLog);

    List<TblFaultSendLog> getNeedRetryPushFaults();

    void updateById(TblFaultSendLog fault);
}