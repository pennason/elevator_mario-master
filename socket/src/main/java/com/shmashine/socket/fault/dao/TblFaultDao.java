package com.shmashine.socket.fault.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * 故障记录表(TblFault)表数据库访问层
 *
 * @author little.li
 * @since 2020-06-14 15:35:00
 */
@Mapper
public interface TblFaultDao {


    /**
     * 更新手动恢复故障状态
     *
     * @param elevatorCode 电梯编号
     * @param faultNum     故障num
     * @param manualClear  手动恢复
     */
    void updateManualClear(@Param("elevatorCode") String elevatorCode, @Param("faultNum") int faultNum,
                           @Param("manualClear") Integer manualClear);

    /**
     * 更加电梯编号获取所有故障中的故障编号
     *
     * @param elevatorCode 电梯编号
     */
    List<String> getByCodeFailure(String elevatorCode);


    List<String> getFaultSecondTypeByCode(String elevatorCode);

    /**
     * 清空所有的临时故障
     *
     * @param elevatorCode 电梯编号
     */
    void disappearAllTempFault(String elevatorCode);
}