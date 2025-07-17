package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblFaultSendShiled;

public interface TblFaultSendShiledDao {

    TblFaultSendShiled getById(@NotNull String vFaultSendShiledId);

    List<TblFaultSendShiled> listByEntity(TblFaultSendShiled tblFaultSendShiled);

    List<TblFaultSendShiled> getByEntity(TblFaultSendShiled tblFaultSendShiled);

    List<TblFaultSendShiled> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblFaultSendShiled tblFaultSendShiled);

    int insertBatch(@NotEmpty List<TblFaultSendShiled> list);

    int update(@NotNull TblFaultSendShiled tblFaultSendShiled);

    int updateByField(@NotNull @Param("where") TblFaultSendShiled where, @NotNull @Param("set") TblFaultSendShiled set);

    int updateBatch(@NotEmpty List<TblFaultSendShiled> list);

    int deleteById(@NotNull String vFaultSendShiledId);

    int deleteByEntity(@NotNull TblFaultSendShiled tblFaultSendShiled);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblFaultSendShiled tblFaultSendShiled);

}