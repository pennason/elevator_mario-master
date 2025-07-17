package com.shmashine.sender.server.ruijin;


import java.util.List;

import com.shmashine.common.entity.TblThridPartyRuijinCheck;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

public interface TblThridPartyRuijinCheckService {

    int insert(TblThridPartyRuijinCheck tblThridPartyRuijinCheck);

    int insertBatch(List<TblThridPartyRuijinCheck> list);

    int deleteByReportNumber(String reportNumber);

    int deleteAll();

    TblThridPartyRuijinCheck getElevatorCheckInfo(String reportNumer);

    TblThridPartyRuijinCheck getElevatorLastCheckInfoByRegisterNumer(String registerNumber);
}