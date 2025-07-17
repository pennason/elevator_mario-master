package com.shmashine.socket.fault.service;

import java.util.List;


/**
 * 故障记录表(TblFault)表服务接口
 *
 * @author little.li
 * @since 2020-06-14 15:35:01
 */
public interface TblFaultService {


    /**
     * 更新手动恢复故障状态
     *
     * @param elevatorCode 电梯编号
     * @param faultNum     故障类型
     * @param manualClear  手动恢复状态
     */
    void updateManualClear(String elevatorCode, int faultNum, Integer manualClear);


    /**
     * 根据电梯编号获取所有故障中的故障类型
     *
     * @param elevatorCode 电梯编号
     * @return 故障主类型列表
     */
    List<String> getFaultTypeByCode(String elevatorCode);


    /**
     * 根据电梯编号获取所有故障中的故障子类型
     *
     * @param elevatorCode 电梯编号
     * @return 故障子类型列表
     */
    List<String> getFaultSecondTypeByCode(String elevatorCode);


    /**
     * 清空所有的临时故障
     *
     * @param elevatorCode 电梯编号
     */
    void disappearAllTempFault(String elevatorCode);

}