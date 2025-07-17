package com.shmashine.fault.fault.dao;


import java.util.HashMap;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblSenSorFault;
import com.shmashine.fault.fault.entity.TblFault;

/**
 * 故障表
 */
@Mapper
public interface TblFaultDao {

    TblFault getById(@NotNull String vFaultId);

    TblSenSorFault getSenSorFaultById(@NotNull String faultId);

    List<TblFault> listByEntity(TblFault tblFault);

    List<TblFault> getByEntity(TblFault tblFault);

    List<TblFault> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblFault tblFault);

    int insertBatch(@NotEmpty List<TblFault> list);

    int update(@NotNull TblFault tblFault);

    int updateByField(@NotNull @Param("where") TblFault where, @NotNull @Param("set") TblFault set);

    int updateBatch(@NotEmpty List<TblFault> list);

    int deleteById(@NotNull String vFaultId);

    int deleteByEntity(@NotNull TblFault tblFault);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblFault tblFault);

    /**
     * 通过电梯编号和故障类型，查找故障中的记录
     *
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     */
    TblFault getInFaultByFaultTypeAndSecondType(@Param("elevatorCode") String elevatorCode,
                                                @Param("faultType") String faultType,
                                                @Param("faultSecondType") String faultSecondType);

    TblFault getInFaultByFaultType(@Param("elevatorCode") String elevatorCode, @Param("faultType") String faultType);

    List<TblFault> getInFault(@Param("elevatorCode") String elevatorCode);

    List<TblFault> getInFaultByFault6(String elevatorCode);

    /**
     * 添加传感器故障记录
     *
     * @param tblFault 故障记录
     */
    void addSensorFault(TblSenSorFault tblFault);

    /**
     * 获取故障中的传感器故障
     *
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     */
    TblSenSorFault getInSensorFaultByFaultType(@Param("elevatorCode") String elevatorCode,
                                               @Param("faultType") String faultType);

    /**
     * 恢复传感器故障
     *
     * @param fault 故障
     */
    int disappearSensorFault(TblSenSorFault fault);

    /*添加设备离线一小时故障*/
    void addDeviceTimeOutFault(@Param("id") String id, @Param("elevatorCode") String elevatorCode,
                               @Param("sensorType") String sensorType);

    /**
     * 获取所有故障中传感器故障
     */
    List<HashMap<String, String>> getAllSensorFaultOnFaulting(@Param("faultNum") Integer faultNum,
                                                              @Param("elevatorCode") String elevatorCode);

    /**
     * 获取今日达三次传感器故障
     */
    List<HashMap<String, String>> getTodaySensorFaultOnThreeTimes();

    /**
     * 恢复故障中传感器故障
     *
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     */
    void disappearSensorFaultByEleCodeAndFaultType(@Param("elevatorCode") String elevatorCode,
                                                   @Param("faultType") String faultType);

    /**
     * 获取传感器关联故障表
     */
    List<HashMap<String, String>> getSensorConfig();

}