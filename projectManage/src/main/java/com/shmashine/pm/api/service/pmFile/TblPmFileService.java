package com.shmashine.pm.api.service.pmFile;

import com.shmashine.pm.api.dao.TblPmFileDao;
import com.shmashine.pm.api.entity.TblPmFile;

public interface TblPmFileService {

    TblPmFileDao getTblPmFileDao();

    TblPmFile getById(String vInvestigateTaskId);

    TblPmFile getByEntity(TblPmFile tblPmFile);

//    List<TblInvestigateTask> listByEntity(TblPmFile tblInvestigateTaskFile);

    int insert(TblPmFile tblPmFile);

    int update(TblPmFile tblPmFile);

    int deleteById(String vTargetId);
}
