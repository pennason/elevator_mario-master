package com.shmashine.fault.file.service;

import java.util.List;

import com.shmashine.fault.file.dao.TblSysFileDao;
import com.shmashine.fault.file.entity.TblSysFile;

/**
 * TblSysFileServiceI
 */
public interface TblSysFileServiceI {

    TblSysFileDao getTblSysFileDao();

    TblSysFile getById(String vFileId);

    //根据故障id获取取证文件
    List<TblSysFile> getFilesById(String vFaultId);

    List<TblSysFile> getByEntity(TblSysFile tblSysFile);

    List<TblSysFile> listByEntity(TblSysFile tblSysFile);

    List<TblSysFile> listByIds(List<String> ids);

    int insert(TblSysFile tblSysFile);

    void insertBatch(List<TblSysFile> fileList);

    int update(TblSysFile tblSysFile);

    int updateBatch(List<TblSysFile> list);

    int deleteById(String vFileId);

    int deleteByEntity(TblSysFile tblSysFile);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblSysFile tblSysFile);
}