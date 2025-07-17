package com.shmashine.api.service.system;

import java.util.List;

import com.shmashine.api.dao.TblThirdPartyRuijinEnventDao;
import com.shmashine.common.entity.TblThirdPartyRuijinEnvent;

public interface TblThirdPartyRuijinEnventServiceI {

    TblThirdPartyRuijinEnventDao getTblThirdPartyRuijinEnventDao();

    TblThirdPartyRuijinEnvent getById(String eventid);

    List<TblThirdPartyRuijinEnvent> getByEntity(TblThirdPartyRuijinEnvent tblThirdPartyRuijinEnvent);

    List<TblThirdPartyRuijinEnvent> listByEntity(TblThirdPartyRuijinEnvent tblThirdPartyRuijinEnvent);

    List<TblThirdPartyRuijinEnvent> listByIds(List<String> ids);

    int insert(TblThirdPartyRuijinEnvent tblThirdPartyRuijinEnvent);

    int insertBatch(List<TblThirdPartyRuijinEnvent> list);

    int update(TblThirdPartyRuijinEnvent tblThirdPartyRuijinEnvent);

    int updateBatch(List<TblThirdPartyRuijinEnvent> list);

    int deleteById(String eventid);

    int deleteByEntity(TblThirdPartyRuijinEnvent tblThirdPartyRuijinEnvent);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblThirdPartyRuijinEnvent tblThirdPartyRuijinEnvent);
}