package com.shmashine.pm.api.service.checkingTask;

import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.dao.TblCheckingTaskDao;
import com.shmashine.pm.api.entity.TblCheckingTask;

public interface TblCheckingTaskService {

    TblCheckingTaskDao getTblCheckingTaskDao();

    TblCheckingTask getById(String vCheckingTaskId);

    TblCheckingTask getByEntity(TblCheckingTask tblCheckingTask);

    List<TblCheckingTask> listByEntity(TblCheckingTask tblCheckingTask);

    int insert(TblCheckingTask tblCheckingTask);

    int update(TblCheckingTask tblCheckingTask);

    int deleteById(String vCheckingTaskId);

    Map countByStatus();
}
