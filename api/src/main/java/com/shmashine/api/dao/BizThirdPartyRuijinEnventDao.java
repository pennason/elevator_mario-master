package com.shmashine.api.dao;

import org.apache.ibatis.annotations.Param;

public interface BizThirdPartyRuijinEnventDao {

    void deleteThirdPartyRuijinEnventData(@Param("eventNumber") String eventNumber);

    void deleteThirdPartyRuijinEnventDataDetail(@Param("eventNumber") String eventNumber);

}