package com.shmashine.hkCameraForTY.task;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shmashine.hkCameraForTY.dao.CameraForTYDao;
import com.shmashine.hkCameraForTY.entity.TblResponseHkReport;
import com.shmashine.hkCameraForTY.service.HkVideoService;

/**
 * 定时任务
 * @author: jiangheng
 * @version: 1.0
 * @date: 2021/6/15 14:42
 */
@Component
public class HkVideoTask {

    @Resource
    private CameraForTYDao testDao;

    @Autowired
    private HkVideoService hkVideoService;

    /**
     * 定时任务处理，定时拉取海康获取流失败的记录，并重新保存取证文件
     * 5分钟拉取一次
     */
    @Async
    @Scheduled(fixedRate = 300000, initialDelay = 10000)
    public void hkVideoTask() {

        /*获取需要重新拉取的视频*/
        List<TblResponseHkReport> tblResponseHkReportList = testDao.queryResHkReport();

        /*重新拉取*/
        tblResponseHkReportList.forEach(it -> hkVideoService.hkVideoDownloadReset(it));

    }
}
