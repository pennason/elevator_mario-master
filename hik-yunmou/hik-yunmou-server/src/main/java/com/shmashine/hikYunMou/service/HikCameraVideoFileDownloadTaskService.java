package com.shmashine.hikYunMou.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shmashine.hikYunMou.entity.HikCameraVideoFileDownloadTask;

/**
 * @author  jiangheng
 * @version 2022/11/10 16:49
 * @description: com.shmashine.hikvPlatform.service
 */
public interface HikCameraVideoFileDownloadTaskService extends IService<HikCameraVideoFileDownloadTask> {


    List<HikCameraVideoFileDownloadTask> queryDownloadtaskList();

}
