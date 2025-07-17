package com.shmashine.sender.server.ruijin;


import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shmashine.common.entity.TblThirdPartyRuijinMaintenance;
import com.shmashine.sender.dataobject.TblThirdPartyRuijinMaintenanceDO;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

public interface TblThirdPartyRuijinMaintenanceService extends IService<TblThirdPartyRuijinMaintenanceDO> {

    int insert(TblThirdPartyRuijinMaintenance tblThirdPartyRuijinMaintenance);

    int insertBatch(List<TblThirdPartyRuijinMaintenance> list);

    int deleteById(String registerNumber);

    int deleteAll();

    TblThirdPartyRuijinMaintenance getMaintenanceInfoByRegisternumber(String registerNumber);

}