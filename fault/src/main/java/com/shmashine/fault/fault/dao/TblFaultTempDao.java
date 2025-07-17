package com.shmashine.fault.fault.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.fault.fault.entity.TblFaultTemp;

/**
 * 故障记录临时表(TblFaultTemp)表数据库访问层
 *
 * @author makejava
 * @since 2020-06-29 18:40:09
 */
@Mapper
public interface TblFaultTempDao {

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
    TblFaultTemp getInFaultByFaultType(@Param("elevatorCode") String elevatorCode,
                                       @Param("faultType") String faultType,
                                       @Param("faultSecondType") String faultSecondType);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<TblFaultTemp> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param tblFaultTemp 实例对象
     * @return 对象列表
     */
    List<TblFaultTemp> queryAll(TblFaultTemp tblFaultTemp);

    /**
     * 新增数据
     *
     * @param tblFaultTemp 实例对象
     * @return 影响行数
     */
    int insert(TblFaultTemp tblFaultTemp);

    /**
     * 修改数据
     *
     * @param tblFaultTemp 实例对象
     * @return 影响行数
     */
    int update(TblFaultTemp tblFaultTemp);

    /**
     * 通过主键删除数据
     *
     * @param vFaultId 主键
     * @return 影响行数
     */
    int deleteById(String vFaultId);

    void insertUncivilizedBehavior37(TblFaultTemp faultTemp);
}