package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.api.module.fault.input.SearchFaultTempModule;
import com.shmashine.common.entity.TblFaultTemp;

/**
 * 故障记录临时表(TblFaultTemp)表数据库访问层
 *
 * @author makejava
 * @since 2020-06-29 18:40:09
 */
@Mapper
public interface BizFaultTempDao {

    List<Map> searchFaultList(SearchFaultTempModule searchFaultTempModule);

    TblFaultTemp queryById(String faultId);

    Map getFaultDetail(String faultId);

    int update(@NotNull TblFaultTemp tblFaultTemp);

    TblFaultTemp queryElectricMobileById(String faultId);

    void updateElectricMobile(@NotNull TblFaultTemp tblFaultTemp);

    /**
     * 获取故障审核角色的电话
     */
    List<String> getSeatsTel();

    Integer checkToSend(@Param("elevatorCode") String elevatorCode, @Param("faultType") String faultType);

    /**
     * 更新标注状态
     *
     * @param faultId      故障ID
     * @param hasMarkLabel 标注状态
     * @return 结果
     */
    Integer updateMarkLabel(@Param("faultId") String faultId, @Param("hasMarkLabel") Integer hasMarkLabel);

    /**
     * 每日凌晨恢复困人临时表中故障
     *
     * @param today     当前日期，格式 yyyy-MM-dd
     * @param yesterday 昨日日期，格式 yyyy-MM-dd
     */
    void recoverPeopleTrappedFault(@Param("today") String today, @Param("yesterday") String yesterday);

}