package com.shmashine.fault.fault.service;

import java.util.List;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.entity.TblSenSorFault;
import com.shmashine.common.message.MonitorMessage;
import com.shmashine.fault.fault.dao.TblFaultDao;
import com.shmashine.fault.fault.entity.TblFault;
import com.shmashine.fault.fault.entity.TblFaultDefinition0902;

/**
 * 故障服务接口
 */
public interface TblFaultServiceI {

    TblFaultDao getTblFaultDao();

    TblFault getById(String vFaultId);

    /**
     * 根据故障id获取传感器故障
     */
    TblSenSorFault getSenSorFaultById(String faultId);

    List<TblFault> getByEntity(TblFault tblFault);

    List<TblFault> listByEntity(TblFault tblFault);

    List<TblFault> listByIds(List<String> ids);

    int insert(TblFault tblFault);

    int insertBatch(List<TblFault> list);

    int update(TblFault tblFault);

    int updateBatch(List<TblFault> list);

    int deleteById(String vFaultId);

    int deleteByEntity(TblFault tblFault);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblFault tblFault);


    /**
     * 通过电梯编号和故障类型，查找故障中的记录
     *
     * @param elevatorCode    电梯编号
     * @param faultType       故障类型
     * @param faultSecondType 故障子类型
     */
    TblFault getInFaultByFaultType(String elevatorCode, String faultType, String faultSecondType);


    /**
     * 通过电梯编号和故障类型，查找故障中的记录
     *
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     */
    TblFault getInFaultByFaultType(String elevatorCode, String faultType);


    /**
     * 根据故障报文，新增故障记录
     *
     * @param messageJson  故障报文
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     */
    int addFaultByMessage(JSONObject messageJson, MonitorMessage monitorMessage, String elevatorCode,
                          String faultType, String faultSecondType);

    /**
     * 根据故障报文，消除故障记录
     *
     * @param messageJson 故障报文
     * @param fault       故障对象
     */
    void disappearFaultByMessage(JSONObject messageJson, TblFault fault);

    /**
     * 根据故障报文，消除传感器故障记录
     *
     * @param messageJson 故障报文
     * @param fault       故障对象
     */
    void disappearSensorFaultByMessage(JSONObject messageJson, TblSenSorFault fault);

    List<TblFault> getInFault(String elevatorCode);

    /**
     * 西子扶梯故障处理——新增故障
     */
    void addEscalatorFaultByMessage(JSONObject messageJson, String elevatorCode, String faultType,
                                    String faultSecondType, TblFaultDefinition0902 faultDefinition);

    /**
     * 非平层停梯识别到有人处理
     *
     * @param faultId 非平层故障id
     */
    void faultConfirmBy6(String faultId);

    /**
     * 查找故障中的非平层停梯或非平层困人故障
     *
     * @param elevatorCode 电梯code
     */
    List<TblFault> getInFaultByFault6(String elevatorCode);

    /**
     * 查找故障中的传感器故障
     *
     * @param elevatorCode 电梯code
     * @param faultType    故障类型
     */
    TblSenSorFault getInSensorFaultByFaultType(String elevatorCode, String faultType);

    /**
     * 根据故障报文，新增传感器故障记录
     *
     * @param messageJson  故障报文
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     */
    void addSensorFaultByMessage(JSONObject messageJson, String elevatorCode, String faultType,
                                 TblFaultDefinition0902 faultDefinition);


    void updateSensorFault(TblSenSorFault fault);

    /**
     * 设备离线1小时告警
     */
    void addDeviceTimeOutFault(String value);

    /**
     * 传感器故障check（20：00 传感器故障次数未到3次恢复，3次延续故障）
     */
    void checkSensorFaultIsContinue();

}
