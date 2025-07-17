package com.shmashine.api.controller.camera;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.module.auth.Result;
import com.shmashine.api.module.camera.VedioResponse;
import com.shmashine.api.service.file.TblSysFileServiceI;

/**
 * 历史视频、图片相关接口
 */
@RestController
@RequestMapping("/history/alarm")
public class HistoryAlarmController {


    @Autowired
    private TblSysFileServiceI tblSysFileService;

    /**
     * 根据告警ID获取录像
     *
     * @param businessNo
     * @return #type:com.shmashine.api.module.auth.Result,com.shmashine.api.module.camera.VedioResponse#
     */
    @GetMapping("/vedio/{businessNo}")
    public Result getVedio(@PathVariable String businessNo) {
        String failureNo = businessNo.replace("shmx-", "");
        // 故障录制视频
        String videoUrl = tblSysFileService.getVideoUrl(failureNo, 2, 1);
        return Result.success(new VedioResponse(videoUrl), "成功");
    }

    /**
     * 根据告警ID获取pic
     *
     * @param businessNo
     * @return #type:com.shmashine.api.module.auth.Result,com.shmashine.api.module.camera.VedioResponse#
     */
    @GetMapping("/picture/{businessNo}")
    public Result getPicture(@PathVariable String businessNo) {
        String failureNo = businessNo.replace("shmx-", "");
        // 故障录制图片
        String videoUrl = tblSysFileService.getVideoUrl(failureNo, 2, 0);
        return Result.success(new VedioResponse(videoUrl), "成功");
    }

}