package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblThirdPartyRuijinEnventDetail;


public interface TblThirdPartyRuijinEnventDetailDao {

    TblThirdPartyRuijinEnventDetail getById(@NotNull String eventId);

    List<TblThirdPartyRuijinEnventDetail> listByEntity(TblThirdPartyRuijinEnventDetail tblThirdPartyRuijinEnventDetail);

    List<TblThirdPartyRuijinEnventDetail> getByEntity(TblThirdPartyRuijinEnventDetail tblThirdPartyRuijinEnventDetail);

    List<TblThirdPartyRuijinEnventDetail> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblThirdPartyRuijinEnventDetail tblThirdPartyRuijinEnventDetail);

    int insertIsNotEmpty(@NotNull TblThirdPartyRuijinEnventDetail tblThirdPartyRuijinEnventDetail);

    int insertBatch(@NotEmpty List<TblThirdPartyRuijinEnventDetail> list);

    int update(@NotNull TblThirdPartyRuijinEnventDetail tblThirdPartyRuijinEnventDetail);

    int updateByField(@NotNull @Param("where") TblThirdPartyRuijinEnventDetail where, @NotNull @Param("set") TblThirdPartyRuijinEnventDetail set);

    int updateBatch(@NotEmpty List<TblThirdPartyRuijinEnventDetail> list);

    int deleteById(@NotNull String eventId, String eventDetailId);

    int deleteByEntity(@NotNull TblThirdPartyRuijinEnventDetail tblThirdPartyRuijinEnventDetail);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblThirdPartyRuijinEnventDetail tblThirdPartyRuijinEnventDetail);

}