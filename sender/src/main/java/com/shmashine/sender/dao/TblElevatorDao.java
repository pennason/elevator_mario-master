package com.shmashine.sender.dao;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblElevator;
import com.shmashine.sender.entity.SendToUser;
import com.shmashine.sender.entity.TblHaierLiftInfo;

/**
 * 电梯表(TblElevator)表数据库访问层
 *
 * @author little.li
 * @since 2020-06-14 15:17:39
 */
@Mapper
public interface TblElevatorDao {

    /**
     * 根据电梯编号获取电梯
     */
    TblElevator getByElevatorCode(String elevatorCode);

    List<TblElevator> listOfAll();

    /**
     * 根据推送的平台编号获取有注册码的电梯
     */
    List<TblElevator> getByPtCode(String ptCode);

    /**
     * 根据维保工单更新维保时间
     *
     * @param registerNumber 电梯20位注册码
     * @param completeTime   维保完成时间  下次维保时间延后15天（自动计算）
     */
    int updateMaintainDateByRegisterNumber(@Param("registerNumber") String registerNumber,
                                           @Param("completeTime") Date completeTime);

    /**
     * 更新年检时间
     *
     * @param registerNumber 电梯20位注册码
     * @param lastDate       上次年检时间
     * @param nextDate       下次年检时间
     */
    int updateInspectDateByRegisterNumber(@Param("registerNumber") String registerNumber,
                                          @Param("lastDate") Date lastDate, @Param("nextDate") Date nextDate);

    ArrayList<String> getRegNumberByProjectId(String projectId);

    List<TblElevator> getByPtCodeAndProjectId(@Param("ptCode") String ptCode, @Param("projectId") String projectId);

    /**
     * 查找更新推送用户缓存
     */
    List<SendToUser> taskReloadSendToUser();

    /**
     * 根据唯一注册号获取海尔推送信息
     */
    TblHaierLiftInfo getLiftInfoCache(String equipmentCode);

    List<String> getElevatorRegisterNumerIsNotNull();


}