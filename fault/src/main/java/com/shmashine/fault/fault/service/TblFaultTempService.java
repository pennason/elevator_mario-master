package com.shmashine.fault.fault.service;

import java.util.List;

import com.shmashine.fault.fault.entity.TblFaultTemp;

/**
 * 故障记录临时表(TblFaultTemp)表服务接口
 *
 * @author makejava
 * @since 2020-06-29 18:40:09
 */
public interface TblFaultTempService {

    /**
     * 通过ID查询单条数据
     *
     * @param vFaultId 主键
     * @return 实例对象
     */
    TblFaultTemp queryById(String vFaultId);

    /**
     * 通过电梯编号和故障类型，查找故障中的记录
     *
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     */
    TblFaultTemp getInFaultByFaultType(String elevatorCode, String faultType, String faultSecondType);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<TblFaultTemp> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param tblFaultTemp 实例对象
     * @return 实例对象
     */
    TblFaultTemp insert(TblFaultTemp tblFaultTemp);

    /**
     * 修改数据
     *
     * @param tblFaultTemp 实例对象
     * @return 实例对象
     */
    TblFaultTemp update(TblFaultTemp tblFaultTemp);

    /**
     * 通过主键删除数据
     *
     * @param vFaultId 主键
     * @return 是否成功
     */
    boolean deleteById(String vFaultId);

    /**
     * 获取故障审核角色的电话
     */
    List<String> getSeatsTel();

    void insertUncivilizedBehavior37(TblFaultTemp faultTemp);
}