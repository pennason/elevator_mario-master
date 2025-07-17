package com.shmashine.api.controller.camera;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shmashine.api.dao.BizElevatorDao;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.feign.RemoteCameraServer;
import com.shmashine.api.module.auth.Result;
import com.shmashine.api.module.camera.SearchCamerasModule;
import com.shmashine.api.module.elevator.SearchElevatorModule;
import com.shmashine.api.module.elevator.XmHlsHttpOrHttpsModule;
import com.shmashine.api.module.fault.input.FaultCameraModule;
import com.shmashine.api.service.camera.TblCameraServiceI;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.common.entity.TblCamera;
import com.shmashine.common.utils.POIUtils;

/**
 * 调用远程摄像头服务
 *
 * @author Dean Winchester
 */
@RestController
@RequestMapping("/remote/camera")
public class CameraController extends BaseRequestEntity {

    @Autowired
    private RemoteCameraServer remoteCameraServer;

    @Autowired
    private BizCameraController bizCameraController;

    @Autowired
    private BizUserService bizUserService;

    @Resource
    private BizElevatorDao bizElevatorDao;

    @Autowired
    private TblCameraServiceI tblCameraServiceI;

    /**
     * 摄像头绑定管理
     * 远程调用：摄像头服务
     * 基础接口：摄像头列表接口(分页数据)
     *
     * @param searchCamerasModule 查询条件
     * @return 电梯对应的绑定状态
     */
    @PostMapping("/camerasBoundElevotor")
    Object camerasBoundElevotor(@RequestBody SearchCamerasModule searchCamerasModule) {

        //获取授权的电梯
        //拿到该对应账号授权的梯
        SearchElevatorModule searchElevatorModule = new SearchElevatorModule();
        searchElevatorModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchElevatorModule.setUserId(getUserId());
        List<String> eleCodes = getEleCode(searchElevatorModule);

        if (eleCodes == null || eleCodes.size() == 0) {
            //没有电梯绑定，直接返回null
            return null;
        }

        //添加授权电梯数据
        searchCamerasModule.setEleCodes(eleCodes);

        //查询数据
        return ResponseResult.successObj(tblCameraServiceI.camerasBoundElevotor(searchCamerasModule));

        //return ResponseResult.successObj(remoteCameraServer.camerasBoundElevotor(searchCamerasModule));
    }

    /**
     * 摄像头资产管理
     * 远程调用:摄像头服务
     *
     * @param searchCamerasModule 查询条件
     * @return 摄像头列表
     */
    @PostMapping("/searchCamerasByPage")
    ResponseResult searchCamerasByPage(@RequestBody SearchCamerasModule searchCamerasModule) {
        searchCamerasModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        searchCamerasModule.setUserId(getUserId());
        return ResponseResult.successObj(remoteCameraServer.searchCamerasByPage(searchCamerasModule));
    }

    @PostMapping("/searchCamerasVedioAndPicByPage")
    Object searchCamerasVedioAndPicByPage(@RequestBody SearchCamerasModule searchCamerasModule) {
        return ResponseResult.successObj(remoteCameraServer.searchCamerasVedioAndPicByPage(searchCamerasModule));
    }


    /**
     * 远程调用：摄像头服务
     * 基础接口：摄像头新增，需要判断摄像头id是否存在
     *
     * @param tblCamera 查询条件
     * @return 结果
     */
    @PostMapping(value = {"/insert"})
    ResponseResult insertCameras(@RequestBody TblCamera tblCamera) {
        return ResponseResult.successObj(remoteCameraServer.insert(tblCamera));
    }

    /**
     * 远程调用：摄像头服务
     * 基础接口：摄像头修改、删除
     *
     * @param tblCamera 查询条件
     * @return 结果
     */
    @PostMapping("/update")
    ResponseResult updateCameras(@RequestBody TblCamera tblCamera) {
        return ResponseResult.successObj(remoteCameraServer.update(tblCamera));
    }

    @PostMapping("/updateElevotorBound")
    ResponseResult updateElevotorBound(@RequestBody TblCamera tblCamera) {
        return ResponseResult.successObj(remoteCameraServer.updateElevotorBound(tblCamera));
    }

    /**
     * 远程调用：摄像头
     * 基础接口：摄像头保存并测试
     *
     * @param tblCamera 查询条件
     * @return 结果
     */
    @PostMapping(value = {"/saveAndTest"})
    public Object saveAndTest(@RequestBody TblCamera tblCamera) {
        return ResponseResult.successObj(remoteCameraServer.saveAndTest(tblCamera));
    }

    /**
     * 远程调用：摄像头信息
     * 雄迈获取hls流
     *
     * @param xmHlsHttpOrHttpsModule 查询条件
     * @return 结果
     */
    @PostMapping(value = {"/getRemoteVedioHlsForHttpsOrHttp"})
    Result getRemoteVedioHlsForHttpsOrHttp(@RequestBody XmHlsHttpOrHttpsModule xmHlsHttpOrHttpsModule) {
        return remoteCameraServer.getVedioHlsForHttpsOrHttp(xmHlsHttpOrHttpsModule);
    }

    /**
     * 远程调用：摄像头
     * 根据电梯id获取摄像头信息
     *
     * @param elevatorId 电梯id
     * @return 摄像头信息
     */
    @GetMapping("/getRemoteCameraInfoByElevatorId/{elevatorId}")
    public Object getRemoteCameraInfoByElevatorId(@PathVariable String elevatorId) {
        return ResponseResult.successObj(remoteCameraServer.getCameraInfoByElevatorId(elevatorId));
    }

    /**
     * 根据电梯编号查询摄像头信息
     *
     * @param code 电梯编号
     * @return 摄像头信息
     */
    @GetMapping("/getRemoteByCode/{code}")
    public Object getRemoteByCode(@PathVariable String code) {
        // todo 临时改成可用的  远程需要调试
        return bizCameraController.getByCode(code);
    }

    /**
     * 远程调用：摄像头
     * 获取萤石云token
     *
     * @return 萤石云token
     */
    @GetMapping("/getRemoteEzopenToken")
    public Object getRemoteEzopenToken() {
        return ResponseResult.successObj(remoteCameraServer.ezopenToken());
    }

    /**
     * 远程调用：摄像头
     * 调用雄迈摄像头服务，下载历史视频
     *
     * @param elevatorCode 电梯编号
     * @param faultCameraModule 错误信息
     * @return 结果
     */
    @PostMapping("/getRemoteXmCameraHistory/{elevatorCode}")
    public Object getRemoteXmCameraHistory(@PathVariable String elevatorCode,
                                           @RequestBody FaultCameraModule faultCameraModule) {
        return remoteCameraServer.getCameraHistory(elevatorCode, faultCameraModule);
    }

    /**
     * 批量上传添加摄像头
     *
     * @param file excel文件
     * @return 结果
     */
    @PostMapping("/excelResolve")
    public ResponseResult excelResolve(@RequestParam("excelfile") MultipartFile file) {
        try {
            System.out.println("文件上传成功");
            List<String[]> lists = POIUtils.readExcel(file);
            List<TblCamera> tblCameras = stringTOList(lists);
            return remoteCameraServer.excelResolve(tblCameras);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseResult();
        }
    }

    /**
     * 获取海康上报自行车的电动自行车
     *
     * @return  electricBicycle
     * @flag 0:获取识别成功，1:获取识别失败
     */
    @GetMapping("/electricBicycleConfirm")
    public ResponseResult electricCarConfirm(
            @RequestParam(value = "elevatorCode", required = false) String elevatorCode,
            @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
            @RequestParam("pageIndex") int pageIndex, @RequestParam("pageSize") int pageSize) {
        var electricBicycle = tblCameraServiceI.electricBicycleConfirm(elevatorCode, startTime, endTime, pageIndex,
                pageSize);
        return ResponseResult.successObj(electricBicycle);
    }

    private List<TblCamera> stringTOList(List<String[]> list) {

        ArrayList<TblCamera> tblCameras = new ArrayList<>();

        for (String[] strings : list) {

            tblCameras.add(TblCamera.builder()
                    .vCameraId(strings[0])
                    .vElevatorCode(strings[1])
                    .iCameraType(Integer.parseInt(strings[2]))
                    .vCloudNumber(strings[3])
                    .vUsername(strings[4])
                    .vPassword(strings[5])
                    .vHlsUrl(strings[6])
                    .vRtmpUrl(strings[7])
                    .vPrivateUrl(strings[8])
                    .isActivate(Integer.parseInt(strings[9])).build());

        }
        return tblCameras;
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
