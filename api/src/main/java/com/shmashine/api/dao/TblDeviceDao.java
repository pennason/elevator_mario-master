package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.api.controller.device.vo.DeviceParamConfReqVO;
import com.shmashine.api.controller.device.vo.DeviceParamConfRespVO;
import com.shmashine.api.controller.fault.vo.EventStatisticsReqVO;
import com.shmashine.api.entity.DeviceSensorAndStatusDTO;
import com.shmashine.api.entity.ElevatorEventStatistics;
import com.shmashine.api.entity.TblDeviceSensor;
import com.shmashine.api.module.device.SearchDeviceEventRecordModule;
import com.shmashine.api.module.fault.input.FaultStatisticsModule;
import com.shmashine.api.module.fault.output.EventElevatorDownloadModuleMap;
import com.shmashine.common.entity.TblDevice;

@Mapper
public interface TblDeviceDao {

    TblDevice getById(@NotNull String vDeviceId);

    List<TblDevice> listByEntity(TblDevice tblDevice);

    List<TblDevice> getByEntity(TblDevice tblDevice);

    List<TblDevice> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblDevice tblDevice);

    int insertBatch(@NotEmpty List<TblDevice> list);

    int update(@NotNull TblDevice tblDevice);

    int updateByField(@NotNull @Param("where") TblDevice where, @NotNull @Param("set") TblDevice set);

    int updateBatch(@NotEmpty List<TblDevice> list);

    int deleteById(@NotNull String vDeviceId);

    int deleteByEntity(@NotNull TblDevice tblDevice);

    int deleteByIds(@NotEmpty List<String> list);

    int deleteByElevatorCode(@Param("elevatorCode") String elevatorCode);

    int countAll();

    int countByEntity(TblDevice tblDevice);

    List<TblDevice> listByElevatorIds(@Param("elevatorIdList") List<String> elevatorIdList);

    List<String> getSensorTypeListByElevatorId(@Param("elevatorId") String elevatorId);

    List<TblDevice> deviceListByElevatorCode(@Param("elevatorCode") String elevatorCode);

    List<Map<String, Object>> searchDeviceEventRecord(@Param("module") SearchDeviceEventRecordModule module);

    List<Map<String, Object>> searchDeviceWaveForm(@Param("module") SearchDeviceEventRecordModule module);

    List<EventElevatorDownloadModuleMap> searchDeviceEventRecordBatch(@Param("module") FaultStatisticsModule module);

    List<String> getMasterVersions(@Param("module") SearchDeviceEventRecordModule module);

    /**
     * 获取设备参数配置
     *
     * @param elevatorId
     * @return
     */
    List<DeviceParamConfRespVO> getDeviceParamConf(String elevatorId);

    /**
     * 更新设备参数配置
     *
     * @param paramConf
     * @return
     */
    int updateDeviceParamConf(DeviceParamConfReqVO paramConf);

    /**
     * 新增设备参数配置
     *
     * @param paramConf
     */
    int insertDeviceParamConf(DeviceParamConfReqVO paramConf);

    /**
     * 电梯code和设备类型获取设备参数配置
     *
     * @param elevatorCode
     * @param sensorType
     * @return
     */
    DeviceParamConfRespVO getDeviceParamConfByElevatorCodeAndSensorType(@Param("elevatorCode") String elevatorCode, @Param("sensorType") String sensorType);

    /**
     * 批量新增传感器配置
     *
     * @param tblDeviceSensors
     * @return
     */
    int batchInsertDeviceSensors(List<TblDeviceSensor> tblDeviceSensors);

    /**
     * 删除传感器配置
     *
     * @param deviceId
     */
    void delDeviceSensorsById(String deviceId);

    /**
     * 根据电梯编号和设备类型获取设备信息
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @return 设备信息
     */
    TblDevice getByElevatorCodeAndSensorType(@Param("elevatorCode") String elevatorCode,
                                             @Param("sensorType") String sensorType);

    /**
     * 获取设备传感器配置
     *
     * @param elevatorCode 电梯code
     * @return 传感器配置列表
     */
    List<DeviceSensorAndStatusDTO> getDeviceSensorConfig(String elevatorCode);

    /**
     * 上下线记录统计根据电梯分组
     *
     * @param queryParam  查询条件
     * @param eleCodeList 电梯code列表
     * @return 统计记录
     */
    List<ElevatorEventStatistics> getElevatorEventStatisticsListByCode(
            @Param("queryParam") EventStatisticsReqVO queryParam,
            @Param("eleCodeList") List<String> eleCodeList);

    /**
     * 上下线记录统计根据时间分组
     *
     * @param queryParam  查询条件
     * @param eleCodeList 电梯code列表
     * @return 统计记录
     */
    List<ElevatorEventStatistics> getElevatorEventStatisticsListByTime(
            @Param("queryParam") EventStatisticsReqVO queryParam,
            @Param("eleCodeList") List<String> eleCodeList);

    /**
     * 获取设备版本列表
     *
     * @return 设备版本列表
     */
    List<String> getDeviceVersionList();

    /**
     * 获取【平层传感器】配置备注信息
     *
     * @param elevatorId 电梯id
     * @return 1:烟杆 2:小平层 3:U型光电 4:门磁
     */
    Integer getFloorSensorRemarkByElevatorId(String elevatorId);

    /**
     * 传感器关联配置故障屏蔽
     *
     * @param elevatorCode 电梯编号
     * @return 被屏蔽的故障列表
     */
    List<String> searchShieldedFaultList(String elevatorCode);
}