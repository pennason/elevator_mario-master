package com.shmashine.api.service.system;

import java.util.List;

import com.shmashine.api.dao.TblThirdPartyRuijinEnventDetailDao;
import com.shmashine.common.entity.TblThirdPartyRuijinEnventDetail;

public interface TblThirdPartyRuijinEnventDetailServiceI {

    TblThirdPartyRuijinEnventDetailDao getTblThirdPartyRuijinEnventDetailDao();

    TblThirdPartyRuijinEnventDetail getById(String eventid);

    List<TblThirdPartyRuijinEnventDetail> getByEntity(TblThirdPartyRuijinEnventDetail tblThirdPartyRuijinEnventDetail);

    List<TblThirdPartyRuijinEnventDetail> listByEntity(TblThirdPartyRuijinEnventDetail tblThirdPartyRuijinEnventDetail);

    List<TblThirdPartyRuijinEnventDetail> listByIds(List<String> ids);

    int insert(TblThirdPartyRuijinEnventDetail tblThirdPartyRuijinEnventDetail);

    int insertBatch(List<TblThirdPartyRuijinEnventDetail> list);

    int update(TblThirdPartyRuijinEnventDetail tblThirdPartyRuijinEnventDetail);

    int updateBatch(List<TblThirdPartyRuijinEnventDetail> list);

    int deleteById(String eventid, String eventdetailid);

    int deleteByEntity(TblThirdPartyRuijinEnventDetail tblThirdPartyRuijinEnventDetail);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblThirdPartyRuijinEnventDetail tblThirdPartyRuijinEnventDetail);
}