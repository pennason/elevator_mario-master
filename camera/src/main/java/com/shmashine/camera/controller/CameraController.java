package com.shmashine.camera.controller;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shmashine.camera.model.base.ResponseResult;
import com.shmashine.camera.service.CameraServer;
import com.shmashine.camera.service.FileService;
import com.shmashine.camera.utils.VideoUtils;
import com.shmashine.common.dto.CamareMediaDownloadRequestDTO;
import com.shmashine.common.utils.OSSUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 摄像头相关接口
 *
 * @author little.li
 */
@RestController
@RequestMapping("/camera")
@Slf4j
@Tag(name = "摄像头相关接口", description = "摄像头相关接口 creater: little.li")
@SecurityScheme(name = "token", scheme = "bearer", type = SecuritySchemeType.HTTP)
public class CameraController {


    @Autowired
    private FileService fileService;


    @Resource(type = CameraServer.class)
    private CameraServer cameraServer;

    /**
     * 雄迈回调记录
     *
     * @param fileName
     * @return
     */
    @Operation(summary = "雄迈回调记录", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/xmnet/callback/videoDownload")
    public Object xmSaveVideoReport(@RequestParam String fileName) {
        //TODO 避免C端更新  沿用这个url，这里只做存储使用:videoDownload
        return cameraServer.xmSaveVideoReport(fileName);

    }

    /***
     *
     * @param fileName
     * @return
     */
    /*@PostMapping("/xmnet/callback/videoDownload")
    public Object xmSaveVideo(@RequestParam String fileName) {
        return cameraServer.xmSaveVideo(fileName);

    }*/
    @Operation(hidden = true)
    @PostMapping("/xmnet/callback/imageDownload")
    public Object xmSaveImage(@RequestParam String fileName) {
        cameraServer.xmSaveImage(fileName);
        return null;
    }

    //TODO 临时在老的接口调整 这样C的程序不用换接口
    /**
     * 雄迈摄像头 --- 录像存储回调
     */
    /*@PostMapping("/xmnet/callback/videoDownload")
    public Object xmSaveVideo(@RequestParam String fileName) {
        // h264文件转MP4 并上传oss
        log.info("---雄迈摄像头---录像存储回调开始:fileName：{}"+fileName);
        String MP4Path = VideoUtils.H264ToMp4(fileName);
        String faultId = VideoUtils.videoGetFaultId(fileName);
        // 落库
        fileService.saveVideoFile(MP4Path, faultId);
        log.info("---雄迈摄像头---录像存储回调---落库:fileName：{},MP4Path：{}"+fileName+MP4Path);
        // 删除文件
        VideoUtils.deleteFile(fileName);
        return null;
    }*/


    /**
     * 雄迈摄像头 --- 图片存储回调
     */
   /* @PostMapping("/xmnet/callback/imageDownload")
    public Object xmSaveImage(@RequestParam String fileName) {
        String faultId = VideoUtils.imageGetFaultId(fileName);

        // 获取图片list
        List<String> imageList = VideoUtils.getImageFileList(fileName, faultId);
        if (CollectionUtils.isEmpty(imageList)) {
            return null;
        }

        // 上传OSS
        List<String> ossFileUrlList = new ArrayList<>();
        for (String file : imageList) {
            String ossFileUrl = OSSUtil.saveFaultMP4(file);
            if (StringUtils.isEmpty(ossFileUrl)) {
                continue;
            }
            ossFileUrlList.add(ossFileUrl);
            log.info("---雄迈摄像头---图片存储回调开始:fileName：{}"+fileName);

            // 删除文件
            VideoUtils.deleteFile(file);
        }
        // 落库
        log.info("---雄迈摄像头---录像存储回调---落库:fileName：{},ossFileUrlList：{}"+fileName+ossFileUrlList.toString());
        fileService.saveImageFile(ossFileUrlList, faultId);

        return null;
    }*/


    /**
     * 海康摄像头 --- 录像存储回调
     */
    @Operation(summary = "海康摄像头 --- 录像存储回调", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/ezopen/callback/videoDownload")
    public Object ezSaveVideo(@RequestParam String filePath) {
        String faultId = VideoUtils.videoGetFaultId(filePath);
        // 上传OSS
        String fileUrl = OSSUtil.saveFaultMP4(filePath);
        if (StringUtils.isEmpty(fileUrl)) {
            return null;
        }
        // 落库
        fileService.saveVideoFile(fileUrl, faultId);
        // 删除文件
        VideoUtils.deleteFile(filePath);
        return null;
    }


    /**
     * 海康摄像头 --- 图片存储回调
     */
    @Operation(summary = "海康摄像头 --- 图片存储回调", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/ezopen/callback/imageDownload")
    public Object ezSaveImage(@RequestParam String filePath) {
        String faultId = VideoUtils.videoGetFaultId(filePath);

        // 获取图片list
        List<String> imageList = VideoUtils.makeScreenCut(filePath);
        if (CollectionUtils.isEmpty(imageList)) {
            return null;
        }

        // 上传OSS
        List<String> ossFileUrlList = new ArrayList<>();
        for (String file : imageList) {
            String ossFileUrl = OSSUtil.saveFaultMP4(file);
            if (StringUtils.isEmpty(ossFileUrl)) {
                continue;
            }
            ossFileUrlList.add(ossFileUrl);
            // 删除文件
            VideoUtils.deleteFile(file);
        }
        // 上传OSS
        String fileUrl = OSSUtil.saveFaultMP4(filePath);
        if (StringUtils.isNotEmpty(fileUrl)) {
            // 落库
            fileService.saveVideoFile(fileUrl, faultId);
            // 删除文件
            VideoUtils.deleteFile(filePath);
        }
        // 落库
        fileService.saveImageFile(ossFileUrlList, faultId);
        return null;
    }

    /**
     * 取证文件重新下载
     *
     * @param faultId
     * @param startTime
     * @param endTime
     * @return
     */
    @Operation(summary = "取证文件重新下载", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/fileReDownload")
    public ResponseResult fileReDownload(@RequestParam("faultId") String faultId,
                                         @RequestParam("startTime") String startTime,
                                         @RequestParam("endTime") String endTime) {

        return ResponseResult.successObj(fileService.fileReDownload(faultId, startTime, endTime));
    }

    /**
     * 取证文件手动上传替换
     *
     * @param faultId  故障id
     * @param fileType 文件类型
     * @param file     文件
     * @return 结果
     */
    @Operation(summary = "取证文件手动上传替换", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/fileReplace")
    public ResponseResult fileReplace(@RequestParam("faultId") String faultId,
                                      @RequestParam("fileType") Integer fileType,
                                      @RequestParam("file") MultipartFile file) {

        return fileService.fileReplace(faultId, fileType, file);
    }


    /**
     * 根据条件下载摄像头图片或者视频
     *
     * @param request 请求参数
     * @return 结果
     */
    @Operation(summary = "根据条件下载摄像头图片或者视频", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/download-camera-file-by-elevator-code")
    public ResponseEntity<String> downloadCameraFileByElevatorCode(@RequestBody CamareMediaDownloadRequestDTO request) {
        return cameraServer.downloadCameraFileByElevatorCode(request);
    }

    /**
     * 根据电梯编号获取语音对讲接口信息
     *
     * @param elevatorCode 电梯编号
     * @return 结果
     */
    @Operation(summary = "根据电梯编号获取语音对讲WSS信息", security = {@SecurityRequirement(name = "token")})
    @GetMapping("/voice/wss-info-by-elevator-code/{elevatorCode}")
    public ResponseResult getVoiceWssInfoByElevatorCode(@PathVariable("elevatorCode") String elevatorCode) {
        return cameraServer.getVoiceWssInfoByElevatorCode(elevatorCode, 1);
    }

    /**
     * 根据电梯编号获取语音对讲接口信息
     *
     * @param elevatorCode 电梯编号
     * @param domain       1:域名，0：IP+端口
     * @return 结果
     */
    @Operation(summary = "根据电梯编号获取语音对讲WSS信息-domain：1:域名，0：IP+端口", security = {@SecurityRequirement(name = "token")})
    @GetMapping("/voice/wss-info-by-elevator-code/{elevatorCode}/{domain}")
    public ResponseResult getVoiceWssInfoByElevatorCode(@PathVariable("elevatorCode") String elevatorCode, @PathVariable(value = "domain") Integer domain) {
        return cameraServer.getVoiceWssInfoByElevatorCode(elevatorCode, domain);
    }

    /**
     * 根据摄像头序列号获取语音对讲WSS信息
     *
     * @param cloudNumber 摄像头序列号
     * @return 结果
     */
    @Operation(summary = "根据摄像头序列号获取语音对讲WSS信息", security = {@SecurityRequirement(name = "token")})
    @GetMapping("/voice/wss-info-by-cloud-number/{cloudNumber}")
    public ResponseResult getVoiceWssInfoByCloudNumber(@PathVariable("cloudNumber") String cloudNumber) {
        return cameraServer.getVoiceWssInfoByCloudNumber(cloudNumber, 1);
    }

    /**
     * 根据摄像头序列号获取语音对讲WSS信息
     *
     * @param cloudNumber 摄像头序列号
     * @return 结果
     */
    @Operation(summary = "根据摄像头序列号获取语音对讲WSS信息-domain：1:域名，0：IP+端口", security = {@SecurityRequirement(name = "token")})
    @GetMapping("/voice/wss-info-by-cloud-number/{cloudNumber}/{domain}")
    public ResponseResult getVoiceWssInfoByCloudNumber(@PathVariable("cloudNumber") String cloudNumber, @PathVariable(value = "domain") Integer domain) {
        return cameraServer.getVoiceWssInfoByCloudNumber(cloudNumber, domain);
    }

}
