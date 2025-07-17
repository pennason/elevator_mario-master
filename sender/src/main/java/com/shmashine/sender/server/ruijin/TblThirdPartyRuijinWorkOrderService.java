package com.shmashine.sender.server.ruijin;


import java.util.List;

import com.shmashine.common.entity.TblThirdPartyRuijinWorkOrder;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

public interface TblThirdPartyRuijinWorkOrderService {

    int insert(TblThirdPartyRuijinWorkOrder tblThirdPartyRuijinWorkOrder);

    int insertBatch(List<TblThirdPartyRuijinWorkOrder> list);

    int deleteByWorkOrderNumber(String workOrderNumber);

    int deleteAll();

    String getWorkOrderNumber(String workOrderNumber);

    /**
     * 获取存在的工单id
     */
    List<String> searchWorkOrderNumberByWorkOrderNumbers(List<String> regsNumber);

}