package com.shmashine.api.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.api.controller.fault.vo.SearchFaultsReqVO;
import com.shmashine.api.module.fault.input.FaultStatisticsModule;
import com.shmashine.api.module.fault.input.SearchFaultModule;
import com.shmashine.api.module.fault.input.SearchSensorFaultModule;
import com.shmashine.api.module.fault.output.FaultDetailDownloadModuleMap;
import com.shmashine.api.module.fault.output.FaultResponseModule;
import com.shmashine.api.module.fault.output.FaultStatisticsExportModule;
import com.shmashine.api.module.fault.output.SensorFaultExportModule;
import com.shmashine.api.module.fault.output.UncivilizedBehaviorDownloadModuleMap;
import com.shmashine.common.entity.TblFault;

@Mapper
public interface BizFaultDao {

    List<Map> searchFaultList(SearchFaultsReqVO searchFaultModule);


    Map getFaultDetail(String faultId);

    /**
     * 昨日故障占比统计
     */
    List<Map> getYesterdayFaultStatics(@Param("isAdmin") boolean isAdmin, @Param("userId") String userId, @Param("elevatorIds") List<String> elevatorIds);

    List<Map> searchSensorFaultList(SearchSensorFaultModule searchFaultModule);

    Map getSensorFaultDetail(String faultId);

    int cancelSensorFault(@NotNull TblFault tblFault);

    /**
     * 单梯历史故障占比统计
     */
    List<Map> getHisFaultStatics(@Param("isAdmin") boolean isAdmin, @Param("userId") String userId, @Param("elevatorId") String elevatorId);

    /**
     * 单梯历史不文明行为占比统计
     */
    List<Map> getHospitalizationIonFaultStatistics(@Param("isAdmin") boolean isAdmin, @Param("userId") String userId, @Param("elevatorId") String elevatorId);

    /**
     * 根据条件获取故障数量前50的电梯编号
     */
    List<String> getTopHundredElevatorCode(FaultStatisticsModule faultStatisticsModule);

    List<FaultResponseModule> getStatisticsFaultList(FaultStatisticsModule faultStatisticsModule);

    List<FaultResponseModule> getStatisticsFaultListByElevatorCode(FaultStatisticsModule faultStatisticsModule);

    TblFault getByFaultId(@Param("faultId") String faultId);


    int update(@NotNull TblFault tblFault);

    List<FaultDetailDownloadModuleMap> searchFaultListDownload(@Param("module") SearchFaultsReqVO searchFaultModule);

    List<UncivilizedBehaviorDownloadModuleMap> searchUncivilizedBehaviorListDownload(@Param("module") SearchFaultsReqVO searchFaultModule);

    /**
     * 获取单个故障
     *
     * @param faultId
     * @return
     */
    TblFault getById(@NotNull String faultId);

    /**
     * 扫描没有二次确认的故障
     *
     * @return
     */
    List<Map<String, String>> taskReloadFaultConfirm();

    List<TblFault> getOnFualtBy37();

    int getFaultTempStatusById(@Param("faultId") String faultId);

    /**
     * 获取电梯离线记录——按时间展示
     *
     * @param faultStatisticsModule
     * @return
     */
    List<Map<String, Object>> getElevatorEventStatisticsByDate(FaultStatisticsModule faultStatisticsModule);

    List<Map<String, Object>> getSensorFaultBYDataAndStatus(@Param("projectId") String projectId);

    /**
     * 统计周困人次数
     */
    List<Map<String, Object>> tiringStatistics(@Param("userId") String userId, @Param("isAdmin") boolean isAdmin, @Param("projectId") String projectId);

    /**
     * 周检修次数统计
     */
    List<Map<String, Object>> maintenanceStatistics(@Param("userId") String userId, @Param("isAdmin") boolean isAdmin, @Param("projectId") String projectId);

    /**
     * 周故障次数统计
     */
    List<Map<String, Object>> faultStatistics(@Param("userId") String userId, @Param("isAdmin") boolean isAdmin, @Param("projectId") String projectId);

    /**
     * 周电动车次数统计
     */
    List<Map<String, Object>> bicycleStatistics(@Param("userId") String userId, @Param("isAdmin") boolean isAdmin, @Param("projectId") String projectId);

    /**
     * 反复阻挡门次数统计
     */
    List<Map<String, Object>> stopTheDoorStatistics(@Param("userId") String userId, @Param("isAdmin") boolean isAdmin, @Param("projectId") String projectId);

    /**
     * 统计获取日运行次数
     */
    List<Map<Object, Object>> runStatistics(@Param("userId") String userId, @Param("isAdmin") boolean isAdmin, @Param("projectId") String projectId);

    List<Map> searchTimeoutFaultList(SearchFaultModule searchFaultModule);

    /**
     * 离线一小时告警手动恢复
     *
     * @param id
     */
    void cancelTimeOutFault(String id);

    /**
     * 删除该梯所有故障
     *
     * @return
     */
    Integer delFaultByElevatorCode(String elevatorCode);

    /**
     * 根据id删除该故障
     *
     * @param faultId
     * @return
     */
    Integer delFaultById(String faultId);

    /**
     * 每日凌晨清理故障中故障
     */
    void clearInFaultingFault();

    /**
     * 获取电梯对应详细地址
     *
     * @param elevatorCodeList
     * @return
     */
    List<HashMap<String, String>> getElevatorAddressByEleCodes(List<String> elevatorCodeList);

    /**
     * 每日故障统计导出列表
     *
     * @param faultStatisticsModule
     * @return
     */
    List<FaultStatisticsExportModule> getStatisticsFaultExportList(FaultStatisticsModule faultStatisticsModule);

    /**
     * 设备故障导出
     *
     * @param searchSensorFaultModule
     * @return
     */
    List<SensorFaultExportModule> searchSensorFaultDownload(SearchSensorFaultModule searchSensorFaultModule);

    /**
     * 根据故障类型获取已安装故障中电梯列表
     *
     * @param faultTypeList
     * @return
     */
    List<String> getFaultElevatorListByFaultType(@Param("userId") String userId, @Param("isAdminFlag") boolean isAdminFlag, @Param("faultTypeList") List<String> faultTypeList);

    /**
     * 根据电梯id和故障上报时间获取故障列表
     *
     * @param elevatorCode 电梯id
     * @param startTime    故障开始时间
     * @param endTime      故障结束时间
     * @return 故障列表
     */
    List<TblFault> getByElevatorCodeAndReportTime(@Param("elevatorCode") String elevatorCode,
                                                  @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 获取传感器关联故障表
     */
    List<HashMap<String, String>> getSensorConfig();

    /**
     * 获取所有故障中传感器故障
     */
    List<HashMap<String, String>> getAllSensorFaultOnFaulting(@Param("faultNum") Integer faultNum,
                                                              @Param("elevatorCode") String elevatorCode);

    /**
     * 根据传感器故障id获取电梯编号
     *
     * @param faultId 故障id
     * @return 电梯编号
     */
    String getElevatorCodeBySensorFaultId(String faultId);

    List<FaultResponseModule> getFultList(FaultStatisticsModule faultStatisticsModule);
}
