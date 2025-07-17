package com.shmashine.pm.api.service.investigateTask;

import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.dao.TblInvestigateTaskDao;
import com.shmashine.pm.api.entity.TblInvestigateTask;

public interface TblInvestigateTaskService {

    TblInvestigateTaskDao getTblInvestigateTaskDao();

    TblInvestigateTask getById(String vInvestigateTaskId);

    List<TblInvestigateTask> getByEntity(TblInvestigateTask tblInvestigateTask);

    List<TblInvestigateTask> listByEntity(TblInvestigateTask tblInvestigateTask);

//    List<TblInvestigateTask> listByIds(List<String> ids);

    int insert(TblInvestigateTask tblInvestigateTask);

//    int insertBatch(List<TblInvestigateTask> list);

    int update(TblInvestigateTask tblInvestigateTask);

//    int updateBatch(List<TblInvestigateTask> list);

    int deleteById(String vInvestigateTaskId);

//    int deleteByEntity(TblInvestigateTask tblInvestigateTask);

//    int deleteByIds(List<String> list);

    int countAll();

//    int countByEntity(TblInvestigateTask tblInvestigateTask);

    List<Map> countByStatus(String principalId);
}
