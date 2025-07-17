package com.shmashine.pm.api.service.installingTask;

import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.dao.TblInstallingTaskDao;
import com.shmashine.pm.api.entity.TblInstallingTask;

public interface TblInstallingTaskService {

    TblInstallingTaskDao getTblInstallingTaskDao();

    TblInstallingTask getById(String vInstallingTaskId);

    List<TblInstallingTask> getByEntity(TblInstallingTask tblInstallingTask);

    List<TblInstallingTask> listByEntity(TblInstallingTask tblInstallingTask);

    int insert(TblInstallingTask tblInstallingTask);

    int update(TblInstallingTask tblInstallingTask);

    int deleteById(String vInstallingTaskId);

    List<Map> countByStatus();
}
