package com.shmashine.api.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.auth.Result;
import com.shmashine.api.module.camera.SearchCamerasModule;
import com.shmashine.api.module.elevator.XmHlsHttpOrHttpsModule;
import com.shmashine.api.module.fault.input.FaultCameraModule;
import com.shmashine.common.entity.TblCamera;
import com.shmashine.common.entity.TblResponseXmReport;

/**
 * 远程调用摄像头服务
 *
 * @author Dean Winchester
 */
@Component
@FeignClient(url = "${endpoint.shmashine-camera:shmashine-camera:8080}", value = "shmashine-camera")
public interface RemoteCameraServer {

    /**
     * 远程调用：摄像头服务
     * 基础接口：摄像头列表接口(分页数据)
     */
    @RequestMapping(value = {"/cameras/camerasBoundElevotor"}, method = {RequestMethod.POST})
    Object camerasBoundElevotor(@RequestBody SearchCamerasModule searchCamerasModule);


    /**
     * 远程调用：摄像头服务
     * 基础接口：摄像头相关视屏或图片列表接口(分页数据)
     */
    @RequestMapping(value = {"/cameras/searchCamerasVedioAndPicByPage"}, method = {RequestMethod.POST})
    Object searchCamerasVedioAndPicByPage(@RequestBody SearchCamerasModule searchCamerasModule);


    /**
     * 远程调用：摄像头服务
     * 基础接口：摄像头新增
     */
    @RequestMapping(value = {"/cameras/insert"}, method = {RequestMethod.POST})
    ResponseResult insert(@RequestBody TblCamera tblCamera);

    /**
     * 远程调用：摄像头服务
     * 基础接口：摄像头修改，删除
     */
    @RequestMapping(value = {"/cameras/update"}, method = {RequestMethod.POST})
    ResponseResult update(TblCamera tblCamera);

    /**
     * 远程调用：摄像头
     * 基础接口：摄像头保存并测试
     */
    @RequestMapping(value = {"/cameras/saveAndTest"}, method = {RequestMethod.POST})
    Object saveAndTest(@RequestBody TblCamera tblCamera);


    /**
     * 远程调用：摄像头
     * 雄迈获取hls流
     */
    @RequestMapping(value = {"/cameras/vedios/getVedioHlsForHttpsOrHttp"}, method = {RequestMethod.POST})
    Result getVedioHlsForHttpsOrHttp(@RequestBody XmHlsHttpOrHttpsModule xmHlsHttpOrHttpsModule);


    /**
     * 远程调用：摄像头
     * 根据电梯id获取摄像头信息
     */
    @RequestMapping(value = {"/cameras/cameraInfo/{elevatorId}"}, method = {RequestMethod.GET})
    Object getCameraInfoByElevatorId(@PathVariable(value = "elevatorId") String elevatorId);

    /**
     * 远程调用：摄像头
     * 根据电梯编号获取摄像头信息
     */
    @RequestMapping(value = {"/cameras/cameraInfo/getByCode/{code}"}, method = {RequestMethod.GET})
    Object getByCode(@PathVariable(value = "code") String code);

    /**
     * 远程调用：摄像头
     * 获取萤石云token
     */
    @GetMapping("/cameras/ezopenToken")
    Object ezopenToken();

    /**
     * 远程调用：摄像头
     * 调用雄迈摄像头服务，下载历史视频
     */
    @RequestMapping(value = {"/cameras/history/{elevatorCode}"}, method = {RequestMethod.POST})
    Object getCameraHistory(@PathVariable(value = "elevatorCode") String elevatorCode,
                            @RequestBody FaultCameraModule faultCameraModule);


    /**
     * 添加文件上传记录
     */
    @RequestMapping(value = {"/cameras/vedios/insertResponeXmReport"}, method = {RequestMethod.POST})
    com.shmashine.common.model.Result insertResponeXmReport(@RequestBody TblResponseXmReport tblResponseXmReport);

    /**
     * 更新文件上传记录
     */
    @RequestMapping(value = {"/cameras/vedios/updateResponeXmReport"}, method = {RequestMethod.POST})
    com.shmashine.common.model.Result updateResponeXmReport(@RequestBody TblResponseXmReport tblResponseXmReport);

    /**
     * 摄像头资产管理
     */
    @RequestMapping(value = {"/cameras/searchCamerasByPage"}, method = {RequestMethod.POST})
    Object searchCamerasByPage(SearchCamerasModule searchCamerasModule);

    /**
     * 远程调用：摄像头服务
     * 基础接口：绑定解绑，根据摄像头id
     */
    @RequestMapping(value = {"/cameras/updateElevotorBound"}, method = {RequestMethod.POST})
    Object updateElevotorBound(TblCamera tblCamera);

    /**
     * excel批量导入摄像头
     */
    @RequestMapping(value = {"/cameras/excelResolve"}, method = {RequestMethod.POST})
    ResponseResult excelResolve(List<TblCamera> tblCameras);
}
