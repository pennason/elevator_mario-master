package com.shmashine.api.controller.camera;

import java.util.HashMap;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.api.entity.CameraAlarmConfig;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.auth.Result;
import com.shmashine.api.module.camera.CameraModule;
import com.shmashine.api.module.elevator.ElevatorDetailModule;
import com.shmashine.api.redis.utils.RedisUtils;
import com.shmashine.api.service.camera.BizCameraService;
import com.shmashine.api.service.camera.TblCameraServiceI;
import com.shmashine.api.service.camera.impl.CameraServerClientBeanServiceImpl;
import com.shmashine.api.service.elevator.BizElevatorService;
import com.shmashine.cameratysl.client.RemoteCameraTyslClient;
import com.shmashine.common.constants.CameraConstants;
import com.shmashine.common.constants.RedisConstants;
import com.shmashine.common.entity.TblCamera;
import com.shmashine.common.utils.CameraUtils;
import com.shmashine.common.utils.SnowFlakeUtils;
import com.shmashine.haierCamera.client.RemoteHaierCameraClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 摄像头信息接口
 *
 * @author little.li
 */

@Slf4j
@RestController
@RequestMapping("/camera")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class BizCameraController {
    private final BizCameraService bizCameraService;
    private final TblCameraServiceI tblCameraServiceI;
    private final BizElevatorService bizElevatorService;
    private final RemoteHaierCameraClient haierCameraClient;
    private final RemoteCameraTyslClient tyslCameraClient;
    private final RedisUtils redisUtils;
    private final CameraServerClientBeanServiceImpl cameraServerBeanService;

    /**
     * 根据电梯id 获取摄像头
     *
     * @param elevatorId
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @GetMapping("/{elevatorId}")
    public Object getByElevatorId(@PathVariable String elevatorId) {
        TblCamera tblCamera = bizCameraService.getByElevatorId(elevatorId);
        CameraModule cameraModule = getCamera(tblCamera);
        return ResponseResult.successObj(cameraModule);
    }

    /**
     * 根据麦信设备编号(elevatorCode)或电梯注册码(equipmentCode) 获取摄像头
     *
     * @param code code
     * @return #type:com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.module.camera.CameraModule#
     */
    @GetMapping("/getByCode/{code}")
    public Object getByCode(@PathVariable String code) {
        if (!StringUtils.hasText(code)) {
            return Result.error("请输入麦信设备编号或电梯注册码");
        }

        // 根据注册码查询电梯code
        ElevatorDetailModule elevator = bizElevatorService.getElevatorByEquipmentCode(code);
        String elevatorCode = (null == elevator) ? code : elevator.getElevatorCode();
        TblCamera tblCamera = bizCameraService.getByElevatorCode(elevatorCode);

        CameraModule cameraModule = getCamera(tblCamera);
        return ResponseResult.successObj(cameraModule);
    }

    /**
     * 根据麦信设备编号(elevatorCode)或电梯注册码(equipmentCode) 获取摄像头
     *
     * @param tblCamera
     * @return #type:com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.module.camera.CameraModule#
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/saveAndDebug")
    public Object getByCode(@RequestBody TblCamera tblCamera) {
        if (!StringUtils.hasText(tblCamera.getVElevatorCode())) {
            return ResponseResult.error();
        }
        // 根据注册码查询电梯code
        TblCamera cameraOld = bizCameraService.getByElevatorId(tblCamera.getVElevatorId());
        if (cameraOld != null) {
            tblCamera.setVCameraId(cameraOld.getVCameraId());
            tblCameraServiceI.update(tblCamera);
        } else {
            tblCamera.setVCameraId(SnowFlakeUtils.nextStrId());
            tblCameraServiceI.insert(tblCamera);
        }
        TblCamera cameraNew = bizCameraService.getByElevatorId(tblCamera.getVElevatorId());
        CameraModule cameraModule = getCamera(cameraNew);
        return ResponseResult.successObj(cameraModule);
    }

    /**
     * 获取萤石云token
     *
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @GetMapping("/ezopenToken")
    public Object ezopenToken() {
        return ResponseResult.successObj(bizCameraService.getEzopenToken());
    }

    private CameraModule getCamera(TblCamera tblCamera) {
        if (null == tblCamera) {
            return null;
        }
        CameraModule cameraModule = new CameraModule();
        BeanUtils.copyProperties(tblCamera, cameraModule);
        if (cameraModule.getiCameraType() == null) {
            return cameraModule;
        }

        if (cameraModule.getiCameraType() == CameraConstants.CameraType.HAIKANG_YS.getType()) {
            /*获取天翼物联平台配置*/
            HashMap<String, String> info = bizCameraService.getHkCameraInfo(tblCamera.getVCameraId());
            /*若为天翼物联平台；则调用camera生成直播流*/
            if (info != null) {
                cameraModule.setvHlsUrl(info.get("hlsHttp"));
                cameraModule.setvRtmpUrl(info.get("rtmpUrl"));
                return cameraModule;
            }

            String haikangHlsUrl = getHaikangHlsUrl(tblCamera.getVCloudNumber(), 120);
            if (haikangHlsUrl != null) {
                cameraModule.setvHlsUrl(haikangHlsUrl);
            }

            // 摄像头类型 1：海康，2：雄迈   海康萤石云需要token访问
            cameraModule.setToken(bizCameraService.getEzopenToken());
        }
        if (cameraModule.getiCameraType() == CameraConstants.CameraType.XIONGMAI.getType()) {
            // 摄像头类型 1：海康，2：雄迈   雄迈hls流获取
            String hlsUrl = CameraUtils.getXiongMaiHlsWithHttps(tblCamera.getVCloudNumber(),
                    tblCamera.getVElevatorCode());
            cameraModule.setvHlsUrl(hlsUrl);
        }
        if (cameraModule.getiCameraType() == CameraConstants.CameraType.HAIER.getType()) {
            // 摄像头类型 1：海康，2：雄迈，3：海尔
            String rest = haierCameraClient.getCameraHlsUrlByElevatorId(cameraModule.getvElevatorId());
            JSONObject messageJSON = JSONObject.parseObject(rest);
            JSONObject data = messageJSON.getJSONObject("data");
            String playUrl = data.getString("playUrl");
            cameraModule.setvHlsUrl(playUrl);
        }
        if (cameraModule.getiCameraType() == CameraConstants.CameraType.TYYY.getType()
                || cameraModule.getiCameraType() == CameraConstants.CameraType.TYBD.getType()) {
            // 摄像头类型 5:天翼云眼，6：中兴
            var res = tyslCameraClient.getCameraStreamUrl(cameraModule.getvElevatorCode(), "HLS");
            if (res != null && HttpStatus.OK.value() == res.getCode()) {
                cameraModule.setvHlsUrl(res.getData().toString());
            } else {
                log.error("getCameraStreamUrl error: {}", JSON.toJSONString(res));
            }
        }
        return cameraModule;
    }

    /**
     * 根据电梯ID获取摄像头报警配置
     *
     * @param elevatorId 电梯id
     * @return
     */
    @PostMapping("/getCameraAlarmConfig")
    public ResponseResult getCameraAlarmConfig(@RequestParam("elevatorId") @NotBlank String elevatorId) {
        return ResponseResult.successObj(bizCameraService.getCameraAlarmConfig(elevatorId));
    }

    /**
     * 根据电梯编号获取一张实时图片
     *
     * @param elevatorCode 电梯编号
     * @return 图片地址
     */
    @PostMapping("/getLivePictureByElevatorCode")
    public ResponseResult getLivePictureByElevatorCode(@RequestParam("elevatorCode") @NotBlank String elevatorCode) {
        return cameraServerBeanService.getLivePictureByElevatorCode(elevatorCode);
    }


    /**
     * 摄像头报警配置新增
     *
     * @param cameraAlarmConfig 摄像头报警配置
     * @return
     */
    @PostMapping("/addCameraAlarmConfig")
    public ResponseResult addCameraAlarmConfig(@RequestBody CameraAlarmConfig cameraAlarmConfig) {
        return ResponseResult.successObj(bizCameraService.addCameraAlarmConfig(cameraAlarmConfig));
    }

    /**
     * 摄像头报警配置删除
     *
     * @param id 摄像头报警配置ID
     * @return
     */
    @PostMapping("/delCameraAlarmConfig")
    public ResponseResult delCameraAlarmConfig(@RequestParam("id") @NotBlank String id) {
        return ResponseResult.successObj(bizCameraService.delCameraAlarmConfig(id));
    }

    /**
     * 摄像头报警配置更新
     *
     * @param cameraAlarmConfig 摄像头报警配置
     * @return
     */
    @PostMapping("/updateCameraAlarmConfig")
    public ResponseResult updateCameraAlarmConfig(@RequestBody CameraAlarmConfig cameraAlarmConfig) {
        return ResponseResult.successObj(bizCameraService.updateCameraAlarmConfig(cameraAlarmConfig));
    }

    /**
     * 获取海康hls播放流地址
     *
     * @param vCloudNumber 摄像头序列号
     * @param expireTime   失效时间 s
     * @return
     */
    private String getHaikangHlsUrl(String vCloudNumber, int expireTime) {

        String url = redisUtils.get(RedisConstants.CAMERA_HAIKANG_HLSURL + vCloudNumber);
        if (!StringUtils.hasText(url) || "null".equals(url)) {
            url = CameraUtils.getHaiKangUrl(vCloudNumber, expireTime);
            //redis缓存海康hlsUrl
            redisUtils.set(RedisConstants.CAMERA_HAIKANG_HLSURL + vCloudNumber, url, expireTime);
        }
        return url;
    }
}