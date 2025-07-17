package com.shmashine.sender.server.fault;


import java.util.HashMap;
import java.util.List;

import com.shmashine.common.entity.TblFaultMapping;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

public interface TblFaultMappingService {

    TblFaultMapping getByEntity(TblFaultMapping tblFaultMapping);

    List<TblFaultMapping> listByEntity(TblFaultMapping tblFaultMapping);

    List<TblFaultMapping> getByPlatformCode(String platformCode);

    TblFaultMapping getByPtCodeAndMxType(String ptCode, String faultType);

    /**
     * 根据故障id获取取证文件
     */
    HashMap<String, String> getEventUrl(String faultId);
}