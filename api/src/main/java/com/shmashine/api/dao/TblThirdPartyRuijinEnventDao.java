package com.shmashine.api.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblThirdPartyRuijinEnvent;


public interface TblThirdPartyRuijinEnventDao {

    TblThirdPartyRuijinEnvent getById(@NotNull String eventId);

    List<TblThirdPartyRuijinEnvent> listByEntity(TblThirdPartyRuijinEnvent tblThirdPartyRuijinEnvent);

    List<TblThirdPartyRuijinEnvent> getByEntity(TblThirdPartyRuijinEnvent tblThirdPartyRuijinEnvent);

    List<TblThirdPartyRuijinEnvent> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblThirdPartyRuijinEnvent tblThirdPartyRuijinEnvent);

    int insertBatch(@NotEmpty List<TblThirdPartyRuijinEnvent> list);

    int update(@NotNull TblThirdPartyRuijinEnvent tblThirdPartyRuijinEnvent);

    int updateByField(@NotNull @Param("where") TblThirdPartyRuijinEnvent where, @NotNull @Param("set") TblThirdPartyRuijinEnvent set);

    int updateBatch(@NotEmpty List<TblThirdPartyRuijinEnvent> list);

    int deleteById(@NotNull String eventId);

    int deleteByEntity(@NotNull TblThirdPartyRuijinEnvent tblThirdPartyRuijinEnvent);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblThirdPartyRuijinEnvent tblThirdPartyRuijinEnvent);

    List<Map<String, String>> getNeedSupplementElevaotr();

    int repairOrderInfoSupplement(@Param("registerNumber") String registerNumber, @Param("reportTime") Date reportTime, @Param("description") String description);
}