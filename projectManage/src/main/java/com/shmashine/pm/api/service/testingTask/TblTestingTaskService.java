package com.shmashine.pm.api.service.testingTask;

import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.dao.TblTestingTaskDao;
import com.shmashine.pm.api.entity.TblTestingTask;

public interface TblTestingTaskService {

    TblTestingTaskDao getTblTestingTaskDao();

    TblTestingTask getById(String vTestingTaskId);

    TblTestingTask getByEntity(TblTestingTask tblTestingTask);

    List<TblTestingTask> listByEntity(TblTestingTask tblTestingTask);

    int insert(TblTestingTask tblTestingTask);

    int update(TblTestingTask tblTestingTask);

    int deleteById(String vInstallingTaskId);

    int countAll();

    List<Map> countByStatus();
}
