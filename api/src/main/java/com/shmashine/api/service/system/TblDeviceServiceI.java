package com.shmashine.api.service.system;

import java.util.List;

import com.shmashine.api.controller.device.vo.DeviceParamConfReqVO;
import com.shmashine.api.controller.device.vo.DeviceParamSyncReqVO;
import com.shmashine.api.controller.device.vo.DeviceSensorAndStatusRespVO;
import com.shmashine.api.controller.device.vo.GetDeviceParamConfRespVO;
import com.shmashine.api.controller.device.vo.SearchDeviceConfPageReqVO;
import com.shmashine.api.dao.TblDeviceDao;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.device.SearchDeviceEventRecordModule;
import com.shmashine.common.entity.TblDevice;

/**
 * 设备服务接口
 */

public interface TblDeviceServiceI {

    TblDeviceDao getTblDeviceDao();

    TblDevice getById(String vDeviceId);

    List<TblDevice> getByEntity(TblDevice tblDevice);

    List<TblDevice> listByEntity(TblDevice tblDevice);

    List<TblDevice> listByIds(List<String> ids);

    int insert(TblDevice tblDevice);

    int insertBatch(List<TblDevice> list);

    int update(TblDevice tblDevice);

    int updateBatch(List<TblDevice> list);

    int deleteById(String vDeviceId);

    int deleteByEntity(TblDevice tblDevice);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblDevice tblDevice);

    List<TblDevice> deviceListByElevatorCode(String elevatorCode);

    PageListResultEntity searchDeviceEventRecord(SearchDeviceEventRecordModule module);

    PageListResultEntity searchDeviceWaveForm(SearchDeviceEventRecordModule module);

    List<String> getMasterVersions(SearchDeviceEventRecordModule module);

    /**
     * 获取设备参数配置
     */
    GetDeviceParamConfRespVO getDeviceParamConf(String elevatorId);

    /**
     * 配置设备参数
     */
    Boolean deviceParamConfigure(List<DeviceParamConfReqVO> deviceParamConfReqVO, String userId);

    /**
     * 获取设备配置列表
     */
    PageListResultEntity searchDeviceConfPage(SearchDeviceConfPageReqVO searchElevatorModule);

    String deviceParamSync(DeviceParamSyncReqVO reqVO);

    /**
     * 设置设备是否开启人员检测
     *
     * @param elevatorCode 电梯编号
     * @param status       0：关闭（默认） 1：开启
     */
    String setDetectedPeopleNumsIsOpen(String elevatorCode, Integer status);

    /**
     * 获取设备传感器列表和传感器故障状态
     *
     * @param elevatorCode 电梯编号
     * @return 传感器列表和传感器故障状态
     */
    List<DeviceSensorAndStatusRespVO> getDeviceSensorAndStatus(String elevatorCode);

    /**
     * 获取设备版本列表
     *
     * @return 版本列表
     */
    List<String> getDeviceVersionList();
}