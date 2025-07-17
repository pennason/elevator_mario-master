package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.entity.SysUserResourceForExcel;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblSysUserResource;

public interface TblSysUserResourceDao {

    TblSysUserResource getById(@NotNull String vUserId);

    List<String> getElevatorCodesByUserId(@NotNull String vUserId);

    List<TblSysUserResource> listByEntity(TblSysUserResource tblSysUserResource);

    //List<TblSysUserResource> getByEntity(TblSysUserResource tblSysUserResource);

    List<TblSysUserResource> listByIds(@NotEmpty List<String> list);

    /**
     * 根据资源ID列表获取用户ID列表
     *
     * @param resourceIdList 资源ID列表
     * @return 用户ID列表
     */
    List<String> getUserIdListByResourceIdList(@Param("resourceIdList") List<String> resourceIdList);

    /**
     * 根据用户ID获取资源ID列表
     */
    List<String> getResourceIdListByUserId(@Param("userId") String userId);

    int insert(@NotNull TblSysUserResource tblSysUserResource);

    int insertBatch(@NotEmpty List<TblSysUserResource> list);

    int update(@NotNull TblSysUserResource tblSysUserResource);

    int updateByField(@NotNull @Param("where") TblSysUserResource where, @NotNull @Param("set") TblSysUserResource set);

    int updateBatch(@NotEmpty List<TblSysUserResource> list);

    int deleteById(@NotNull String vUserId, String vResourceId);

    int deleteByUserId(@NotNull String vUserId);

    int deleteByEntity(@NotNull TblSysUserResource tblSysUserResource);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblSysUserResource tblSysUserResource);

    /**
     * 对excel导入的电梯进行授权
     *
     * @param sysUserResourceList
     */
    void addElevatorByExcel(@Param("sysUserResourceList") List<SysUserResourceForExcel> sysUserResourceList);

    /**
     * 对添加成功的电梯进行绑定
     *
     * @param userId
     * @param tblElevator
     */
    void batchByUserId(@Param("userId") String userId, @Param("tblElevator") TblElevator tblElevator);

    /**
     * 批量删除用户授权电梯
     *
     * @param userId
     * @param removedData
     */
    void deleteByUserAndResourcesId(@Param("userId") String userId, @Param("removedData") List<String> removedData);
}