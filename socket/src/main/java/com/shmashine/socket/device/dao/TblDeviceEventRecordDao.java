package com.shmashine.socket.device.dao;

import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Mapper;

import com.shmashine.socket.device.entity.TblDeviceEventRecord;


/**
 * 设备上下线事件记录表(TblDeviceEventRecord)表数据库访问层
 *
 * @author little.li
 * @since 2020-06-14 15:14:31
 */
@Mapper
public interface TblDeviceEventRecordDao {


    int insert(@NotNull TblDeviceEventRecord tblDeviceEventRecord);


}