package com.shmashine.camera.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.camera.Handle.CameraResourceHandle;
import com.shmashine.camera.model.base.ResponseResult;
import com.shmashine.camera.vo.CameraResourceReqVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 获取摄像头监控、回放、图片
 *
 * @author  jiangheng
 * @version 2023/3/28 11:03
 *
 */
@RestController
@RequestMapping("camera/cameraResource")
@Slf4j
@Tag(name = "摄像头资源接口", description = "摄像头资源接口 creater: jiangheng")
@SecurityScheme(name = "token", scheme = "bearer", type = SecuritySchemeType.HTTP)
public class CameraResourceController {

    @Resource
    private CameraResourceHandle cameraResourceHandle;

    /**
     * 获取直播流
     */
    @Operation(summary = "获取直播流", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/getPreviewURL")
    public ResponseResult getPreviewURL(@RequestBody CameraResourceReqVO reqVO) {

        return ResponseResult.successObj(cameraResourceHandle.getPreviewURL(reqVO));
    }

    /**
     * 获取海康摄像头语音对讲流鉴权token
     *
     * @param channelCode 通道级联编码
     */
    @Operation(summary = "获取海康摄像头语音对讲流鉴权token", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/getHIkTalkStreamToken")
    public ResponseResult getHIkTalkStreamToken(@RequestParam String channelCode) {

        return ResponseResult.successObj(cameraResourceHandle.getHIkTalkStreamToken(channelCode));
    }

    /**
     * 获取回放流
     */
    @Operation(summary = "获取回放流", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/getPlaybackURL")
    public ResponseResult getPlaybackURL(@RequestBody CameraResourceReqVO reqVO) {

        return ResponseResult.successObj(cameraResourceHandle.getPlaybackURL(reqVO));
    }

    /**
     * 获取图片
     */
    @Operation(summary = "获取图片", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/getPictureURL")
    public ResponseResult getPictureURL(@RequestBody CameraResourceReqVO reqVO) {

        return ResponseResult.successObj(cameraResourceHandle.getPictureURL(reqVO));
    }

}
