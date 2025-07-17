package com.shmashine.api.dao;

import java.util.List;

import com.shmashine.api.entity.TblHaierLiftInfo;
import com.shmashine.api.entity.TblHaierProject;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/10/27 14:39
 */
public interface HaierCameraDao {

    /**
     * 添加海尔项目信息
     *
     * @param projects
     */
    void insertAndUpdataProjects(List<TblHaierProject> projects);

    /**
     * 添加海尔电梯信息
     *
     * @param tblHaierLiftInfos
     */
    void insertAndUpdataLiftInfos(List<TblHaierLiftInfo> tblHaierLiftInfos);

    /**
     * @param registrationCode
     * @return
     */
    String getDeviceNoByRegistrationCode(String registrationCode);

    /**
     * 根据项目id获取项目
     *
     * @param projectId
     * @return
     */
    TblHaierProject getProjectById(String projectId);
}
