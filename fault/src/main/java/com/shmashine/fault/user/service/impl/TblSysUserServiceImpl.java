package com.shmashine.fault.user.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.fault.user.dao.TblSysUserDao;
import com.shmashine.fault.user.entity.TblSysUser;
import com.shmashine.fault.user.service.TblSysUserServiceI;

/**
 * (TblSysUser)表服务实现
 */
@Service
public class TblSysUserServiceImpl implements TblSysUserServiceI {

    @Resource(type = TblSysUserDao.class)
    private TblSysUserDao tblSysUserDao;

    @Override
    public TblSysUserDao getTblSysUserDao() {
        return tblSysUserDao;
    }

    @Override
    public TblSysUser getById(String vUserId) {
        return tblSysUserDao.getById(vUserId);
    }

    @Override
    public List<TblSysUser> getByEntity(TblSysUser tblSysUser) {
        return tblSysUserDao.getByEntity(tblSysUser);
    }

    @Override
    public List<TblSysUser> listByEntity(TblSysUser tblSysUser) {
        return tblSysUserDao.listByEntity(tblSysUser);
    }

    @Override
    public List<TblSysUser> listByIds(List<String> ids) {
        return tblSysUserDao.listByIds(ids);
    }

    @Override
    public int insert(TblSysUser tblSysUser) {
        Date date = new Date();
        tblSysUser.setDtCreatetime(date);
        tblSysUser.setDtModifytime(date);
        return tblSysUserDao.insert(tblSysUser);
    }

    @Override
    public int insertBatch(List<TblSysUser> list) {
        return tblSysUserDao.insertBatch(list);
    }

    @Override
    public int update(TblSysUser tblSysUser) {
        tblSysUser.setDtModifytime(new Date());
        return tblSysUserDao.update(tblSysUser);
    }

    @Override
    public int updateBatch(List<TblSysUser> list) {
        return tblSysUserDao.updateBatch(list);
    }

    @Override
    public int deleteById(String vUserId) {
        return tblSysUserDao.deleteById(vUserId);
    }

    @Override
    public int deleteByEntity(TblSysUser tblSysUser) {
        return tblSysUserDao.deleteByEntity(tblSysUser);
    }

    @Override
    public int deleteByIds(List<String> list) {
        return tblSysUserDao.deleteByIds(list);
    }

    @Override
    public int countAll() {
        return tblSysUserDao.countAll();
    }

    @Override
    public int countByEntity(TblSysUser tblSysUser) {
        return tblSysUserDao.countByEntity(tblSysUser);
    }

    @Override
    public List<TblSysUser> getUserListByCode(String elevatorCode) {

        return tblSysUserDao.getUserListByCode(elevatorCode);
    }

    @Override
    public List<TblSysUser> queryElevatorPrincipal(String elevatorId) {
        return tblSysUserDao.queryElevatorPrincipal(elevatorId);
    }

    @Override
    public List<TblSysUser> getAllPrincipal() {
        return tblSysUserDao.getAllPrincipal();
    }

    @Override
    public List<String> getWeChatUserPhoneByUserId(List<String> userIds, Integer isPushTrappedPeople) {
        return tblSysUserDao.getWeChatUserPhoneByUserId(userIds, isPushTrappedPeople);
    }

}