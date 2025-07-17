package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblEnventNotice;

public interface TblEnventNoticeDao {

    TblEnventNotice getById(@NotNull String vEnventNotifyId);

    List<TblEnventNotice> listByEntity(TblEnventNotice tblEnventNotice);

    List<TblEnventNotice> getByEntity(TblEnventNotice tblEnventNotice);

    List<TblEnventNotice> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblEnventNotice tblEnventNotice);

    int insertBatch(@NotEmpty List<TblEnventNotice> list);

    int update(@NotNull TblEnventNotice tblEnventNotice);

    int updateByField(@NotNull @Param("where") TblEnventNotice where, @NotNull @Param("set") TblEnventNotice set);

    int updateBatch(@NotEmpty List<TblEnventNotice> list);

    int deleteById(@NotNull String vEnventNotifyId);

    int deleteByEntity(@NotNull TblEnventNotice tblEnventNotice);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblEnventNotice tblEnventNotice);

}