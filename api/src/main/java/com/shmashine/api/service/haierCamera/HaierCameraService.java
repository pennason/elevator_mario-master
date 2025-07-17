package com.shmashine.api.service.haierCamera;

import java.util.List;

import com.shmashine.api.entity.TblHaierLiftInfo;
import com.shmashine.api.entity.TblHaierProject;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/10/27 14:19
 */
public interface HaierCameraService {

    /**
     * 更新/插入项目信息
     *
     * @param projects
     */
    void insertAndUpdataProjects(List<TblHaierProject> projects);

    /**
     * 更新/插入电梯信息
     *
     * @param tblHaierLiftInfos
     */
    void insertAndUpdataLiftInfos(List<TblHaierLiftInfo> tblHaierLiftInfos);

    /**
     * 根据注册code获取物联网设备编号
     *
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
