package com.shmashine.api.service.haierCamera.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.api.dao.HaierCameraDao;
import com.shmashine.api.entity.TblHaierLiftInfo;
import com.shmashine.api.entity.TblHaierProject;
import com.shmashine.api.service.haierCamera.HaierCameraService;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/10/27 14:23
 */
@Service
public class HaierCameraServiceImpl implements HaierCameraService {

    @Autowired
    private HaierCameraDao haierCameraDao;

    @Override
    public void insertAndUpdataProjects(List<TblHaierProject> projects) {
        haierCameraDao.insertAndUpdataProjects(projects);
    }

    @Override
    public void insertAndUpdataLiftInfos(List<TblHaierLiftInfo> tblHaierLiftInfos) {
        haierCameraDao.insertAndUpdataLiftInfos(tblHaierLiftInfos);
    }

    @Override
    public String getDeviceNoByRegistrationCode(String registrationCode) {
        return haierCameraDao.getDeviceNoByRegistrationCode(registrationCode);
    }

    @Override
    public TblHaierProject getProjectById(String projectId) {
        return haierCameraDao.getProjectById(projectId);
    }

}
