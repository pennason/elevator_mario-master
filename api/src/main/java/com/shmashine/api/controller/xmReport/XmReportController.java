package com.shmashine.api.controller.xmReport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.dao.BizElevatorDao;
import com.shmashine.api.entity.TblRequestXmReport;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.feign.RemoteCameraServer;
import com.shmashine.api.module.elevator.SearchElevatorModule;
import com.shmashine.api.module.fault.input.FaultCameraModule;
import com.shmashine.api.redis.RedisService;
import com.shmashine.api.service.camera.BizCameraService;
import com.shmashine.api.service.elevatorproject.BizProjectService;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.api.service.xmReport.XmReportService;
import com.shmashine.common.entity.TblCamera;
import com.shmashine.common.model.Result;
import com.shmashine.common.utils.CameraUtils;
import com.shmashine.common.utils.SnowFlakeUtils;

/**
 * 雄迈报告
 *
 * @author jiangheng
 * @since 2020/12/28 —— 11:05
 */
@RestController
@RequestMapping("/report")
public class XmReportController extends BaseRequestEntity {

    @Autowired
    private XmReportService xmReportService;

    private final RemoteCameraServer remoteCameraServer;

    private final BizCameraService cameraService;

    private final RedisService redisService;

    @Autowired
    private BizUserService bizUserService;

    @Resource
    private BizElevatorDao bizElevatorDao;

    @Autowired
    private BizProjectService bizProjectService;

    @Autowired
    public XmReportController(BizCameraService cameraService, RedisService redisService, RemoteCameraServer remoteCameraServer) {
        this.cameraService = cameraService;
        this.redisService = redisService;
        this.remoteCameraServer = remoteCameraServer;
    }

    /**
     * 分页查询故障视频
     *
     * @param tblRequestXmReport
     * @return 故障视频信息
     */
    @PostMapping("/xmReportBypage")
    public Object queryXmReportBypage(@RequestBody TblRequestXmReport tblRequestXmReport) {

        //拿到该对应账号授权的梯
        SearchElevatorModule searchElevatorModule = new SearchElevatorModule();
        searchElevatorModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchElevatorModule.setUserId(getUserId());
        List<String> eleCodes = getEleCode(searchElevatorModule);


        PageListResultEntity xmReport = xmReportService.queryXmReportBypage(tblRequestXmReport, eleCodes);

        return ResponseResult.successObj(xmReport);
    }

    /**
     * 远程调用：摄像头
     * 调用雄迈摄像头服务，下载历史视频
     *
     * @param elevatorCode
     * @param faultCameraModule
     * @return
     */
    @PostMapping("/getRemoteXmCameraHistory/{elevatorCode}")
    public Object getRemoteXmCameraHistory(@PathVariable String elevatorCode, @RequestBody FaultCameraModule faultCameraModule) {
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
        if (null == faultCameraModule.getStartTime() || null == faultCameraModule.getEndTime()) {
            return ResponseResult.error();
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

    /**
     * 手动获取错误视频
     *
     * @param faultId 故障id
     * @return url
     */
    @GetMapping("/getUrlByHand/{faultId}")
    public ResponseResult getUrlByHand(@PathVariable String faultId) {
        //手动获取视频地址
        String url = xmReportService.getUrlByHand(faultId);
        if (url == null) {
            return new ResponseResult(ResponseResult.CODE_ERROR, "视频下载失败，请重新下载视频");
        }
        return ResponseResult.successObj(url);
    }

    private Result saveHistoryDownloadTask(String elevatorCode, String fileType, String faultId, Date startTime, Date endTime) {
        com.shmashine.common.entity.TblResponseXmReport tblResponseXmReport = new com.shmashine.common.entity.TblResponseXmReport();
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

    /**
     * 查询所有授权电梯
     *
     * @param searchElevatorModule 用户信息
     * @return 已授权的电梯
     */
    public List<String> getEleCode(SearchElevatorModule searchElevatorModule) {

        // 查询数据
        List<Map> maps = bizElevatorDao.searchElevatorListByProjectId(searchElevatorModule);

        List<String> list = new ArrayList<>();
        for (Map it : maps) {
            list.add(it.get("v_elevator_code").toString());
        }

        return list;
    }

}
