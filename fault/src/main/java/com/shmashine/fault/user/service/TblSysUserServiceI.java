package com.shmashine.fault.user.service;

import java.util.List;

import com.shmashine.fault.user.dao.TblSysUserDao;
import com.shmashine.fault.user.entity.TblSysUser;

/**
 * 系统用户服务接口
 */
public interface TblSysUserServiceI {

    TblSysUserDao getTblSysUserDao();

    TblSysUser getById(String vUserId);

    List<TblSysUser> getByEntity(TblSysUser tblSysUser);

    List<TblSysUser> listByEntity(TblSysUser tblSysUser);

    List<TblSysUser> listByIds(List<String> ids);

    int insert(TblSysUser tblSysUser);

    int insertBatch(List<TblSysUser> list);

    int update(TblSysUser tblSysUser);

    int updateBatch(List<TblSysUser> list);

    int deleteById(String vUserId);

    int deleteByEntity(TblSysUser tblSysUser);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblSysUser tblSysUser);


    /**
     * 根据电梯编号，获取拥有电梯权限的用户列表
     *
     * @param elevatorCode 电梯编号
     * @return 用户列表
     */
    List<TblSysUser> getUserListByCode(String elevatorCode);

    /**
     * 获取西子扶梯电梯负责人
     *
     * @param elevatorId 电梯id
     */
    List<TblSysUser> queryElevatorPrincipal(String elevatorId);

    /**
     * 获取所有西子电梯负责人
     */
    List<TblSysUser> getAllPrincipal();

    /**
     * 根据用户id获取需要推送困人短信的微信用户手机号
     *
     * @param userIds             用户id列表
     * @param isPushTrappedPeople 是否推送困人短信
     * @return 手机号列表
     */
    List<String> getWeChatUserPhoneByUserId(List<String> userIds, Integer isPushTrappedPeople);
}