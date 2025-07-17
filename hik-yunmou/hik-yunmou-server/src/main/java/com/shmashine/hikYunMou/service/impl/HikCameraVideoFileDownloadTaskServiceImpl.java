package com.shmashine.hikYunMou.service.impl;


import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shmashine.hikYunMou.dao.HikCameraVideoFileDownloadTaskDao;
import com.shmashine.hikYunMou.entity.HikCameraVideoFileDownloadTask;
import com.shmashine.hikYunMou.service.HikCameraVideoFileDownloadTaskService;

/**
 * @author  jiangheng
 * @version 2022/11/10 16:50
 * @description: com.shmashine.hikvPlatform.service.impl
 */
@Service
public class HikCameraVideoFileDownloadTaskServiceImpl extends ServiceImpl<HikCameraVideoFileDownloadTaskDao, HikCameraVideoFileDownloadTask> implements HikCameraVideoFileDownloadTaskService {
    @Override
    public List<HikCameraVideoFileDownloadTask> queryDownloadtaskList() {
        return baseMapper.queryDownloadtaskList();
    }
}
