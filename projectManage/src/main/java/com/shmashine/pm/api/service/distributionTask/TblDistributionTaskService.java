package com.shmashine.pm.api.service.distributionTask;

import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.dao.TblDistributionTaskDao;
import com.shmashine.pm.api.entity.TblDistributionTask;

public interface TblDistributionTaskService {

    TblDistributionTaskDao getTblDistributionTaskDao();

    TblDistributionTask getById(String vDistributionTaskId);

    List<TblDistributionTask> getByEntity(TblDistributionTask tblDistributionTask);

    List<TblDistributionTask> listByEntity(TblDistributionTask tblDistributionTask);

    int insert(TblDistributionTask tblDistributionTask);

    int update(TblDistributionTask tblDistributionTask);

    int deleteById(String vDistributionTaskId);

    int countAll();

    List<Map> countByStatus();
}
