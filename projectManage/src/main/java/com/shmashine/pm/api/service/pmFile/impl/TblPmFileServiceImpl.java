package com.shmashine.pm.api.service.pmFile.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.pm.api.dao.TblPmFileDao;
import com.shmashine.pm.api.entity.TblPmFile;
import com.shmashine.pm.api.service.pmFile.TblPmFileService;

@Service
public class TblPmFileServiceImpl implements TblPmFileService {

    @Resource(type = TblPmFileDao.class)
    private TblPmFileDao tblPmFileDao;

    @Override
    public TblPmFileDao getTblPmFileDao() {
        return this.tblPmFileDao;
    }

    @Override
    public TblPmFile getById(String vTargetId) {
        return tblPmFileDao.getById(vTargetId);
    }

    @Override
    public TblPmFile getByEntity(TblPmFile tblPmFile) {
        return tblPmFileDao.getByEntity(tblPmFile);
    }

//    @Override
//    public List<TblInvestigateTask> listByEntity(TblPmFile tblInvestigateTaskFile) {
//        return tblPmFileDao.lis(tblInvestigateTaskFile);
//    }

    @Override
    public int insert(TblPmFile tblPmFile) {
        tblPmFile.setDtCreateTime(new Date());
        tblPmFile.setDtModifyTime(new Date());
        tblPmFile.setiDelFlag(0);
        return tblPmFileDao.insert(tblPmFile);
    }

    @Override
    public int update(TblPmFile tblPmFile) {
        tblPmFile.setDtModifyTime(new Date());
        return tblPmFileDao.update(tblPmFile);
    }

    @Override
    public int deleteById(String vPmFileId) {
        return tblPmFileDao.deleteById(vPmFileId);
    }
}
