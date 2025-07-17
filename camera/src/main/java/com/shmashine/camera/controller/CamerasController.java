package com.shmashine.camera.controller;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.camera.model.FaultCameraModule;
import com.shmashine.camera.model.ImageHandleRequest;
import com.shmashine.camera.model.Result;
import com.shmashine.camera.model.SearchCamerasModule;
import com.shmashine.camera.model.VedioResponse;
import com.shmashine.camera.model.VideoHandlerRequest;
import com.shmashine.camera.model.XmHlsHttpOrHttpsModule;
import com.shmashine.camera.model.base.ResponseResult;
import com.shmashine.camera.service.CameraServer;
import com.shmashine.common.entity.TblCamera;
import com.shmashine.common.entity.TblResponseXmReport;
import com.shmashine.common.model.request.FaceRecognitionRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 摄像头相关服务
 *
 * @author Dean Winchester
 */

@Tag(name = "摄像头服务整合", description = "摄像头服务整合 creater: jiangheng")
@RestController
@RequestMapping("/cameras")
@SecurityScheme(name = "token", scheme = "bearer", type = SecuritySchemeType.HTTP)
public class CamerasController {

    @Resource(type = CameraServer.class)
    private CameraServer cameraServer;

    /**
     * 摄像头绑定管理
     * 基础接口——摄像头绑定列表接口(分页数据)
     */
    @Operation(summary = "【API服务调用】：基础接口：摄像头绑定分页列表", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/camerasBoundElevotor")
    public Object camerasBoundElevotor(@RequestBody SearchCamerasModule searchCamerasModule) {
        return ResponseResult.successObj(cameraServer.searchElevatorListByPage(searchCamerasModule));
    }

    /**
     * 摄像头管理
     * api服务调用，摄像头管理列表
     */
    @Operation(summary = "【API服务调用】：基础接口：摄像头管理分页列表", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/searchCamerasByPage")
    public Object searchCamerasByPage(@RequestBody SearchCamerasModule searchCamerasModule) {
        return ResponseResult.successObj(cameraServer.searchCamerasListByPage(searchCamerasModule));
    }

    /**
     * 基础接口——新增摄像头信息
     */
    @Operation(summary = "【API服务调用】：新增摄像头信息", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/insert")
    public ResponseResult insert(@RequestBody TblCamera tblCamera) {
        return cameraServer.insert(tblCamera);
    }

    /**
     * 基础接口——修改或删除摄像头信息
     */
    @Operation(summary = "【API服务调用】：修改摄像头信息", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/update")
    public ResponseResult update(@RequestBody TblCamera tblCamera) {
        return cameraServer.update(tblCamera);
    }

    /**
     * 电梯绑定修改
     */
    @Operation(summary = "【API服务调用】：修改摄像头信息", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/updateElevotorBound")
    public ResponseResult updateElevotorBound(@RequestBody TblCamera tblCamera) {
        return cameraServer.updateElevotorBound(tblCamera);
    }

    /**
     * API服务调用：根据麦信设备编号(elevatorCode)或电梯注册码(equipmentCode) 获取摄像头
     */
    @Operation(summary = "【API服务调用】：根据麦信设备编号(elevatorCode)或电梯注册码(equipmentCode) 获取摄像头",
            description = "API服务调用：根据麦信设备编号(elevatorCode)或电梯注册码(equipmentCode) 获取摄像头",
            security = {@SecurityRequirement(name = "token")})
    @PostMapping("/saveAndTest")
    public Object saveAndTest(@RequestBody TblCamera tblCamera) {
        return ResponseResult.successObj(cameraServer.saveAndTest(tblCamera));
    }

    @Operation(summary = "【API服务调用】：查询摄像头关联的视频或图片文件", description = "基础接口：查询摄像头关联的视频或图片文件",
            security = {@SecurityRequirement(name = "token")})
    @PostMapping("/searchCamerasVedioAndPicByPage")
    public Object searchCamerasVedioAndPicByPage(@RequestBody SearchCamerasModule searchCamerasModule) {
        return ResponseResult.successObj(cameraServer.searchCamerasVedioAndPicByPage(searchCamerasModule));
    }


    /**
     * Socket服务调用：根据电梯编号 获取rtmp流地址
     */
    @Operation(summary = "【Socket服务调用】：根据电梯编号 获取rtmp流地址（本地库查询）", description = "Socket服务调用：根据电梯编号 获取rtmp流地址（本地库查询）",
            security = {@SecurityRequirement(name = "token")})
    @Parameters({@Parameter(name = "elevatorCode", description = "电梯编号", required = true, in = ParameterIn.PATH)})
    @GetMapping("/getRtmpUrlByElevatorCode/{elevatorCode}")
    public String getRtmpUrlByElevatorCode(@PathVariable String elevatorCode) {
        return cameraServer.getRtmpUrlByElevatorCode(elevatorCode);
    }


    /**
     * Socket服务调用：通过电梯编号 获取hls流地址
     *
     * @param elevatorCode 电梯编号
     * @return hls流地址
     */
    @Operation(summary = "【Socket服务调用】：通过电梯编号 获取hls流地址（本地库查询）", description = "Socket服务调用：通过电梯编号 获取hls流地址（本地库查询）",
            security = {@SecurityRequirement(name = "token")})
    @Parameters({@Parameter(name = "elevatorCode", description = "电梯编号", required = true, in = ParameterIn.PATH)})
    @GetMapping("/getHlsUrlByElevatorCode/{elevatorCode}")
    public String getHlsUrlByElevatorCode(@PathVariable String elevatorCode) {
        return cameraServer.getHlsUrlByElevatorCode(elevatorCode);
    }

    /**
     * Socket服务调用：截取摄像头当前一帧图片，返回图片存储路径
     * 雄迈摄像头通过序列号获取流地址，海康通过rtmpUrl获取
     *
     * @param elevatorCode 电梯编号
     * @return 图片存储路径
     */
    @Operation(summary = "【Socket服务调用】：截取摄像头当前一帧图片(困人)，返回图片存储路径", description = "每一秒自动请求一次，最多请求三次",
            security = {@SecurityRequirement(name = "token")})
    @Parameters({@Parameter(name = "elevatorCode", description = "电梯编号", required = true, in = ParameterIn.PATH)})
    @GetMapping("/getCurrentImagePathByElevatorCode/{elevatorCode}")
    public String getCurrentImagePathByElevatorCode(@PathVariable String elevatorCode) {
        return cameraServer.getCurrentImagePathByElevatorCode(elevatorCode);
    }


    /**
     * API服务调用：根据电梯id获取摄像头信息
     */
    @Operation(summary = "【API服务调用】：根据电梯id获取摄像头信息", description = "API服务调用：根据电梯id获取摄像头信息",
            security = {@SecurityRequirement(name = "token")})
    @Parameters({@Parameter(name = "elevatorId", description = "电梯id", required = true, in = ParameterIn.PATH)})
    @GetMapping("/cameraInfo/{elevatorId}")
    public Object getCameraInfoByElevatorId(@PathVariable String elevatorId) {
        return ResponseResult.successObj(cameraServer.getCameraInfoByElevatorId(elevatorId));
    }

    /**
     * API服务调用：根据麦信设备编号(elevatorCode)或电梯注册码(equipmentCode) 获取摄像头
     */
    @Operation(summary = "【API服务调用】：根据麦信设备编号(elevatorCode)或电梯注册码(equipmentCode) 获取摄像头",
            description = "API服务调用：根据麦信设备编号(elevatorCode)或电梯注册码(equipmentCode) 获取摄像头",
            security = {@SecurityRequirement(name = "token")})
    @Parameters({@Parameter(name = "code", description = "电梯编号/电梯注册码", required = true, in = ParameterIn.PATH)})
    @GetMapping("/cameraInfo/getByCode/{code}")
    public Object getByCode(@PathVariable String code) {
        return ResponseResult.successObj(cameraServer.getByCode(code));
    }

    /**
     * API服务调用：获取萤石云token
     */
    @Operation(summary = "【API服务调用】：获取萤石云token", description = "API服务调用：获取萤石云token",
            security = {@SecurityRequirement(name = "token")})
    @GetMapping("/ezopenToken")
    public Object ezopenToken() {
        return ResponseResult.successObj(cameraServer.getEzopenToken());
    }


    /**
     * API服务调用：根据告警ID获取录像
     */
    @Operation(summary = "【API服务调用】：根据告警ID获取录像（本地库查询）", description = "API服务调用：根据告警ID获取录像（本地库查询）",
            security = {@SecurityRequirement(name = "token")})
    @Parameters({@Parameter(name = "businessNo", description = "告警id", required = true, in = ParameterIn.PATH)})
    @GetMapping("/history/vedio/{businessNo}")
    public Result getVedio(@PathVariable String businessNo) {
        return Result.success(new VedioResponse(cameraServer.getVedio(businessNo)), "成功");
    }

    /**
     * API服务调用：根据告警ID获取pic
     */
    @Operation(summary = "【API服务调用】：根据告警ID获取pic（本地库查询）", description = "API服务调用：根据告警ID获取pic（本地库查询）",
            security = {@SecurityRequirement(name = "token")})
    @Parameters({@Parameter(name = "businessNo", description = "告警id", required = true, in = ParameterIn.PATH)})
    @GetMapping("/history/picture/{businessNo}")
    public Result getPicture(@PathVariable String businessNo) {
        return Result.success(new VedioResponse(cameraServer.getPicture(businessNo)), "成功");
    }


    /**
     * 调用雄迈摄像头服务，下载历史视频
     */
    @Operation(summary = "【API服务调用】：调用雄迈摄像头服务，下载历史视频",
            description = "1.调用雄迈下载历史视频，2.雄迈回调接口，/xmnet/callback/videoDownload，进行落库",
            security = {@SecurityRequirement(name = "token")})
    @Parameters({@Parameter(name = "elevatorCode", description = "电梯编号（雄迈摄像头）", required = true,
            in = ParameterIn.PATH)})
    @PostMapping("/history/{elevatorCode}")
    public Object getCameraXMHistory(@PathVariable String elevatorCode,
                                     @RequestBody FaultCameraModule faultCameraModule) {
        return cameraServer.getCameraXmHistory(elevatorCode, faultCameraModule);
    }

    @Operation(summary = "【Fault服务调用】：录像存储处理", description = "雄迈通过序列号实时获取流，海康通过VHlsUrl获取流",
            security = {@SecurityRequirement(name = "token")})
    @PostMapping("/videoHandlerApplication")
    public Object videoHandlerApplication(@RequestBody VideoHandlerRequest videoHandlerRequest) {
        return cameraServer.videoHandlerApplication(videoHandlerRequest);
    }

    @Operation(summary = "【Fault服务调用】：截图处理", description = "摄像头类型为雄迈才可以截图",
            security = {@SecurityRequirement(name = "token")})
    @PostMapping("/imageHandleApplication")
    public Object imageHandleApplication(@RequestBody ImageHandleRequest imageHandleRequest) {
        return cameraServer.imageHandleApplication(imageHandleRequest);
    }


    /**
     * API服务调用：雄迈摄像头 --- 录像存储回调
     */
    @Operation(hidden = true)
    @PostMapping("/xmnet/callback/videoDownload")
    public Object xmSaveVideo(@RequestParam String fileName) {
        return cameraServer.xmSaveVideo(fileName);

    }


    /**
     * API服务调用：雄迈摄像头 --- 图片存储回调
     */
    @Operation(hidden = true)
    @PostMapping("/xmnet/callback/imageDownload")
    public Object xmSaveImage(@RequestParam String fileName) {
        cameraServer.xmSaveImage(fileName);
        return null;
    }


    /**
     * API服务调用：海康摄像头 --- 录像存储回调
     */
    @Operation(hidden = true)
    @PostMapping("/ezopen/callback/videoDownload")
    public Object ezSaveVideo(@RequestParam String filePath) {
        cameraServer.ezSaveVideo(filePath);
        return null;
    }


    /**
     * API服务调用：海康摄像头 --- 图片存储回调
     */
    @Operation(hidden = true)
    @PostMapping("/ezopen/callback/imageDownload")
    public Object ezSaveImage(@RequestParam String filePath) {
        cameraServer.ezSaveImage(filePath);
        return null;
    }


    @Operation(summary = "【测试视频流地址】", description = "海康：直接返回数据库存储的hls流地址，雄迈：根据输入的http或https进行返回实时流地址",
            security = {@SecurityRequirement(name = "token")})
    @PostMapping("/vedios/getVedioHlsForHttpsOrHttp")
    public Result getVedioHlsForHttpsOrHttp(@RequestBody XmHlsHttpOrHttpsModule xmHlsHttpOrHttpsModule) {
        return cameraServer.getVedioHlsForHttpsOrHttp(xmHlsHttpOrHttpsModule);
    }

    /**
     * 添加文件上传记录
     */
    @Operation(summary = "【Fault服务调用】", description = "Fault模块调用，添加记录",
            security = {@SecurityRequirement(name = "token")})
    @PostMapping("/vedios/insertResponeXmReport")
    Result insertResponeXmReport(@RequestBody TblResponseXmReport tblResponseXmReport) {
        return cameraServer.insertResponeXmReport(tblResponseXmReport);
    }

    /**
     * 更新文件上传记录
     */
    @Operation(summary = "【Fault服务调用】", description = "Fault模块调用，更新记录",
            security = {@SecurityRequirement(name = "token")})
    @PostMapping("/vedios/updateResponeXmReport")
    Result updateResponeXmReport(@RequestBody TblResponseXmReport tblResponseXmReport) {
        return cameraServer.updateResponeXmReport(tblResponseXmReport);
    }


    @Operation(summary = "【Socket服务人脸识别】", description = "Socket,人脸识别",
            security = {@SecurityRequirement(name = "token")})
    @PostMapping("/faceRecognition")
    Integer faceRecognition(@RequestBody FaceRecognitionRequest faceRecognitionRequest) {
        return 1;
    }

    @Operation(summary = "【Api服务调用，批量添加摄像头】", description = "excel批量添加摄像头",
            security = {@SecurityRequirement(name = "token")})
    @PostMapping("/excelResolve")
    ResponseResult excelResolve(@RequestBody List<TblCamera> tblCameras) {

        Boolean flag = false;

        for (TblCamera tblCamera : tblCameras) {
            ResponseResult responseResult = cameraServer.insert(tblCamera);
            if ("1111".equals(responseResult.getCode())) {
                flag = true;
            }
        }
        if (flag) {
            //excel表格有部分数据错误（可能是电梯已绑定，序列号已经注册），该部分数据请重新导入!
            return new ResponseResult(ResponseResult.CODE_ERROR, "msg9_06");
        }
        return ResponseResult.success();
    }

}
