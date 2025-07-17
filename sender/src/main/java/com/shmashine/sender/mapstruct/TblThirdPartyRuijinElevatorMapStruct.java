// Copyright (C) 2025 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.shmashine.common.entity.TblThirdPartyRuijinElevator;
import com.shmashine.sender.dataobject.TblThirdPartyRuijinElevatorDO;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2025/5/21 14:18
 * @since v1.0
 */

@Mapper
public interface TblThirdPartyRuijinElevatorMapStruct {

    TblThirdPartyRuijinElevatorMapStruct INSTANCE
            = Mappers.getMapper(TblThirdPartyRuijinElevatorMapStruct.class);

    TblThirdPartyRuijinElevatorDO entityToDo(TblThirdPartyRuijinElevator source);
}
