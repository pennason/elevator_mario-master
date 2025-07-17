package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblSysUser;

@Mapper
public interface TblSysUserDao {

    TblSysUser getById(@NotNull String vUserId);

    List<TblSysUser> listByEntity(TblSysUser tblSysUser);

    List<TblSysUser> getByEntity(TblSysUser tblSysUser);

    List<TblSysUser> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblSysUser tblSysUser);

    int insertBatch(@NotEmpty List<TblSysUser> list);

    int update(@NotNull TblSysUser tblSysUser);

    int updateByField(@NotNull @Param("where") TblSysUser where, @NotNull @Param("set") TblSysUser set);

    int updateBatch(@NotEmpty List<TblSysUser> list);

    int deleteById(@NotNull String vUserId);

    int deleteByEntity(@NotNull TblSysUser tblSysUser);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblSysUser tblSysUser);

    /**
     * 根据项目获取用户授权电梯数
     *
     * @param projectId        项目id
     * @param permissionUserId 用户id
     * @return
     */
    Integer countByProjectIdAndUser(@Param("projectId") String projectId, @Param("permissionUserId") String permissionUserId);

    /**
     * 根据小区获取用户授权电梯数
     *
     * @param villageId        小区id
     * @param permissionUserId 用户id
     * @return
     */
    Integer countByVillageIdAndUser(@Param("villageId") String villageId, @Param("permissionUserId") String permissionUserId);
}