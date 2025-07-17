package com.shmashine.pm.api.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.shmashine.pm.api.entity.TblProject;
import com.shmashine.pm.api.module.project.input.SearchElevatorProjectListModule;
import com.shmashine.pm.api.module.project.input.SearchElevatorProjectModule;
import com.shmashine.pm.api.module.project.output.ResultProjectDetail;

/**
 * 电梯项目DAO
 */
public interface BizElevatorProjectDao {


    List<Map> searchElevatorProjectList(SearchElevatorProjectListModule searchElevatorProjectListModule);

    ResultProjectDetail searchElevatorProjectInfo(TblProject tblProject);

    List<Map> searchElevatorProjectSelectList(SearchElevatorProjectModule searchElevatorProjectModule);


    List<String> getProjectIdsByDeptIds(@Param("deptIds") List<String> deptIds);

    List<Map> getVillagesStatus(@Param("vProjectId") String vProjectId);

    List<Map> getCountByStatus();

    List<Map> getStatistics(SearchElevatorProjectListModule searchElevatorProjectListModule);
}
