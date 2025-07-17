package com.shmashine.fault.user.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.fault.user.entity.TblSysUser;

/**
 * tbl_sys_user
 */
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

    List<TblSysUser> getUserListByCode(@Param("elevatorCode") String elevatorCode);

    public List<String> getSeatsTel();

    /**
     * 获取西子扶梯电梯负责人
     *
     * @param elevatorId 电梯id
     */
    List<TblSysUser> queryElevatorPrincipal(@Param("elevatorId") String elevatorId);

    /**
     * 获取所有西子扶梯负责人
     */
    List<TblSysUser> getAllPrincipal();

    /**
     * 根据用户id获取需要推送困人短信的微信用户手机号
     *
     * @param userIds             用户id列表
     * @param isPushTrappedPeople 是否推送困人短信
     * @return 手机号列表
     */
    List<String> getWeChatUserPhoneByUserId(@Param("userIds") List<String> userIds,
                                            @Param("isPushTrappedPeople") Integer isPushTrappedPeople);

}