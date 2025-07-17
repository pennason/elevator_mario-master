package com.shmashine.sender.server.ruijin;


import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shmashine.common.entity.TblThirdPartyRuijinElevator;
import com.shmashine.sender.dataobject.TblThirdPartyRuijinElevatorDO;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

public interface TblThirdPartyRuijinElevatorService extends IService<TblThirdPartyRuijinElevatorDO> {

    int insert(TblThirdPartyRuijinElevator tblThirdPartyRuijinElevator);

    int insertBatch(List<TblThirdPartyRuijinElevator> list);

    int deleteById(String registerNumber);

    int deleteAll();

    TblThirdPartyRuijinElevator getRjElevatorInfo(String registerNumber);

    TblThirdPartyRuijinElevatorDO getByRegisterNumber(String registerNumber);
}