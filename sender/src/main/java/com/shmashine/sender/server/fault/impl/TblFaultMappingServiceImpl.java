package com.shmashine.sender.server.fault.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.shmashine.common.entity.TblFaultMapping;
import com.shmashine.sender.dao.TblFaultMappingDao;
import com.shmashine.sender.server.fault.TblFaultMappingService;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@Service
public class TblFaultMappingServiceImpl implements TblFaultMappingService {

    @Resource(type = TblFaultMappingDao.class)
    private TblFaultMappingDao tblFaultMappingDao;

    @Override
    public TblFaultMapping getByEntity(TblFaultMapping tblFaultMapping) {
        return tblFaultMappingDao.getByEntity(tblFaultMapping);
    }

    @Override
    public List<TblFaultMapping> listByEntity(TblFaultMapping tblFaultMapping) {
        return tblFaultMappingDao.listByEntity(tblFaultMapping);
    }

    @Override
    public List<TblFaultMapping> getByPlatformCode(String platformCode) {
        return tblFaultMappingDao.getByPlatformCode(platformCode);
    }

    @Override
    @Cacheable(cacheNames = "faultMapping_cache", key = "#ptCode+#mxFaultType", unless = "#result == null")
    public TblFaultMapping getByPtCodeAndMxType(String ptCode, String mxFaultType) {
        TblFaultMapping dto = new TblFaultMapping();
        dto.setPlatformCode(ptCode);
        dto.setMxFaultType(mxFaultType);
        return tblFaultMappingDao.getByEntity(dto);
    }

    @Override
    public HashMap<String, String> getEventUrl(String faultId) {
        return tblFaultMappingDao.getEventUrl(faultId);
    }

}