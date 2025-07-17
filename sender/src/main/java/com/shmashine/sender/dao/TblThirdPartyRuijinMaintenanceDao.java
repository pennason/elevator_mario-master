package com.shmashine.sender.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shmashine.common.entity.TblThirdPartyRuijinMaintenance;
import com.shmashine.sender.dataobject.TblThirdPartyRuijinMaintenanceDO;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@Mapper
public interface TblThirdPartyRuijinMaintenanceDao extends BaseMapper<TblThirdPartyRuijinMaintenanceDO> {

    int insert(TblThirdPartyRuijinMaintenance tblThirdPartyRuijinMaintenance);

    int insertBatch(List<TblThirdPartyRuijinMaintenance> list);

    int deleteById(String registerNumber);

    int deleteAll();

    TblThirdPartyRuijinMaintenance getMaintenanceInfoByRegisternumber(String registerNumber);
}