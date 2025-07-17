package com.shmashine.sender.server.dataAccount;

import java.util.List;
import java.util.Map;

import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblElevatorInfos;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

public interface BizDataAccountService {
    List<Map> list();

    /**
     * 推送临港电梯
     */
    List<TblElevator> getElevatorsByLingang();

    /**
     * 获取临港电梯基础信息
     */
    List<TblElevatorInfos> getElevatorsInfosByLingang();


}
