package com.shmashine.api.controller.fault;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.feign.RemoteCameraServer;
import com.shmashine.api.module.fault.input.FaultCameraModule;
import com.shmashine.api.redis.RedisService;
import com.shmashine.api.service.camera.BizCameraService;
import com.shmashine.common.entity.TblCamera;
import com.shmashine.common.entity.TblResponseXmReport;
import com.shmashine.common.model.Result;
import com.shmashine.common.utils.CameraUtils;
import com.shmashine.common.utils.SnowFlakeUtils;

/**
 * 调用摄像头服务，下载故障历史视频接口
 *
 * @author little.li
 */
@RestController
@RequestMapping("/fault/camera")
public class FaultCameraController {


    private final BizCameraService cameraService;

    private final RemoteCameraServer remoteCameraServer;

    private final RedisService redisService;

    @Autowired
    public FaultCameraController(BizCameraService cameraService, RedisService redisService, RemoteCameraServer remoteCameraServer) {
        this.cameraService = cameraService;
        this.redisService = redisService;
        this.remoteCameraServer = remoteCameraServer;
    }


    /**
     * 调用雄迈摄像头服务，下载历史视频
     */
    @PostMapping("/history/{elevatorCode}")
    public Object getCameraHistory(@PathVariable String elevatorCode, @RequestBody FaultCameraModule faultCameraModule) {

        TblCamera camera = cameraService.getByElevatorCode(elevatorCode);
        if (camera == null) {
            return new ResponseResult(ResponseResult.CODE_ERROR, "msg8_01");
        }
        if (camera.getICameraType() != 2) {
            return new ResponseResult(ResponseResult.CODE_ERROR, "msg8_02");
        }
        if (faultCameraModule.getStartTime() == null) {
            return new ResponseResult(ResponseResult.CODE_ERROR, "msg8_03");
        }

        //通过前端参数直接下载视频
        if (null == camera.getVSerialNumber() || "".equals(camera.getVSerialNumber())) {
            return ResponseResult.error();
        }
        if (null == faultCameraModule.getEndTime()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(faultCameraModule.getStartTime());
            calendar.add(calendar.MINUTE, 1);
            faultCameraModule.setEndTime(calendar.getTime());
        }

        String faultId = faultCameraModule.getId();
        // 保存记录到故障视频下载表
        Integer code = CameraUtils.saveXiongMaiHistoryVideo(faultId, camera.getVSerialNumber(),
                faultCameraModule.getStartTime(), faultCameraModule.getEndTime());
        if (code != 200) {
            return new ResponseResult(ResponseResult.CODE_ERROR, "msg8_04");
        } else {
            // 保存记录到故障视频下载表
            saveHistoryDownloadTask(elevatorCode, "1", faultId, faultCameraModule.getStartTime(), faultCameraModule.getEndTime());
        }

        return new ResponseResult(ResponseResult.CODE_OK, "msg8_05");
    }

    private Result saveHistoryDownloadTask(String elevatorCode, String fileType, String faultId, Date startTime, Date endTime) {
        TblResponseXmReport tblResponseXmReport = new TblResponseXmReport();
        tblResponseXmReport.setId(SnowFlakeUtils.nextStrId());
        //0：图片，1：视频
        tblResponseXmReport.setFileType(fileType);
        //0：待下载 1：下载成功 2：下载中 3:下载失败
        tblResponseXmReport.setFileStatus("2");
        tblResponseXmReport.setCreateTime(new Date());
        tblResponseXmReport.setElevatorCode(elevatorCode);
        tblResponseXmReport.setSerialNumber(elevatorCode);
        tblResponseXmReport.setFaultId(faultId);
        tblResponseXmReport.setStartTime(startTime);
        tblResponseXmReport.setEndTime(endTime);
        return remoteCameraServer.insertResponeXmReport(tblResponseXmReport);
    }


}
