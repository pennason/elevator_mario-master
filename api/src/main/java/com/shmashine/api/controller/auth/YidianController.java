package com.shmashine.api.controller.auth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.annotation.SaIgnore;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.api.controller.auth.VO.GetHisAlarmFileReqVO;
import com.shmashine.api.controller.auth.VO.GetHisAlarmFileRespVO;
import com.shmashine.api.module.auth.AuthToken;
import com.shmashine.api.module.auth.Result;
import com.shmashine.api.module.auth.TokenRequest;
import com.shmashine.api.module.camera.VedioRequest;
import com.shmashine.api.module.camera.VedioResponse;
import com.shmashine.api.module.elevator.ElevatorDetailModule;
import com.shmashine.api.redis.utils.RedisUtils;
import com.shmashine.api.service.camera.BizCameraService;
import com.shmashine.api.service.elevator.BizElevatorService;
import com.shmashine.api.service.file.TblSysFileServiceI;
import com.shmashine.common.constants.CameraConstants;
import com.shmashine.common.constants.RedisConstants;
import com.shmashine.common.entity.TblCamera;
import com.shmashine.common.entity.TblSysFile;
import com.shmashine.common.utils.CameraUtils;
import com.shmashine.common.utils.RedisKeyUtils;
import com.shmashine.haierCamera.client.RemoteHaierCameraClient;
import com.shmashine.hikYunMou.client.RemoteHikCloudClient;
import com.shmashine.satoken.client.SaTokenClientAppletsClient;
import com.shmashine.satoken.dto.SaResultDTO;


/**
 * 仪电数据平台对接
 */
@SaIgnore
@RestController
@RequestMapping("/v1")
public class YidianController {

    @Autowired
    private BizCameraService bizCameraService;

    @Autowired
    private BizElevatorService bizElevatorService;

    @Autowired
    private TblSysFileServiceI tblSysFileService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private RemoteHaierCameraClient haierCameraClient;

    @Resource
    private RemoteHikCloudClient hikCloudClient;

    @Resource
    private SaTokenClientAppletsClient saTokenClientAppletsClient;

    /**
     * （对外调用）获取Token令牌
     */
    @RequestMapping("/getToken")
    public Result refreshToken(@RequestBody TokenRequest tokenRequest) {
        AuthToken authToken = new AuthToken();

        SaResultDTO saResultDTO = saTokenClientAppletsClient.doLogin(tokenRequest.getAppKey(),
                tokenRequest.getAppSecret());

        if (saResultDTO.getCode() == 200) {
            authToken.setAccessToken(saResultDTO.getData().toString());
            authToken.setExpiresTime(86400);
            return Result.success(authToken, "success");
        }
        return Result.error();
    }

    /**
     * 上海市智慧电梯平台需要提供通过 API 的方式获取 https 视频流接口的 URL
     */
    // CHECKSTYLE:OFF
    @RequestMapping("/media/getRealvideoAuthorization")
    public Result getRealVideoAuthorization(@RequestBody VedioRequest vedioRequest) {
        String registerNumber = vedioRequest.getRegisterNumber();
        if (!StringUtils.hasText(registerNumber)) {
            return Result.error("注册码为空");
        }

        String s = redisUtils.get("yidian:stopGetUrl:" + registerNumber);
        if (registerNumber.equals(s)) {
            return Result.error("流量没有啦");
        }

        // 根据注册码查询电梯code
        ElevatorDetailModule elevator = bizElevatorService.getElevatorByEquipmentCode(registerNumber);
        // 根据code查询摄像头
        TblCamera tblCamera = bizCameraService.getByElevatorCode(elevator.getElevatorCode());
        if (null != tblCamera && tblCamera.getICameraType() != null) {
            String hlsUrl = tblCamera.getVHlsUrl();
            if (StringUtils.hasText(hlsUrl)) {
                hlsUrl = hlsUrl.replace("http://", "https://");
            }
            //海康处理——默认播放2分钟
            if (tblCamera.getICameraType() != null
                    && tblCamera.getICameraType() == CameraConstants.CameraType.HAIKANG_YS.getType()) {
                hlsUrl = getHaikangHlsUrl(tblCamera.getVCloudNumber(), 120);
            }

            //海康云眸处理——默认播放2分钟
            if (tblCamera.getICameraType() != null
                    && tblCamera.getICameraType() == CameraConstants.CameraType.HAIKANG_YM.getType()) {
                hlsUrl = hikCloudClient.previewURLs(tblCamera.getVCloudNumber(), "2", 2, "120");
            }

            // 雄迈摄像头随时取最新的
            if (tblCamera.getICameraType() != null
                    && tblCamera.getICameraType() == CameraConstants.CameraType.XIONGMAI.getType()) {
                hlsUrl = CameraUtils.getXiongMaiHlsWithHttps(tblCamera.getVCloudNumber(), elevator.getElevatorCode());
            }
            //海尔摄像头
            if (tblCamera.getICameraType() != null
                    && tblCamera.getICameraType() == CameraConstants.CameraType.HAIER.getType()) {
                // 摄像头类型 1：海康，2：雄迈，3：海尔
                String rest = haierCameraClient.getCameraHlsUrlByElevatorId(tblCamera.getVElevatorId());
                JSONObject messageJSON = JSONObject.parseObject(rest);
                JSONObject data = messageJSON.getJSONObject("data");
                hlsUrl = data.getString("flvPlayUrl");
            }
            return Result.success(new VedioResponse(hlsUrl), "成功");
        }
        return Result.success(new VedioResponse(""), "成功");
    }
    // CHECKSTYLE:ON

    /**
     * 获取海康hls播放流地址
     *
     * @param vCloudNumber 摄像头序列号
     * @param expireTime   失效时间 s
     * @return 结果
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

    /**
     * 上海市智慧电梯平台使用API向电梯远程监测系统请求电梯的实时数据
     *
     * @param access_token token
     * @param json         电梯注册码JSON
     * @return 运行数据
     */
    @RequestMapping("/media/getRealtimeData")
    public Result getRealtimeData(@RequestParam("access_token") String access_token, @RequestBody String json) {

        if (!access_token.equals(redisUtils.get(RedisKeyUtils.getYiDianTokenCacheKey()))) {
            return new Result("token过期", null, 22000002);
        }

        String registerNumber = JSON.parseObject(json).getString("registerNumber");
        String key = RedisKeyUtils.getDataAccountKey("yidian", registerNumber);
        String runDate = redisUtils.get(key);
        if (StringUtils.hasText(runDate)) {
            return Result.success(JSON.parseObject(runDate, JSONObject.class), "成功");
        }
        return Result.error("设备离线");
    }

    /**
     * 上海市智慧电梯平台将接收数据的 URL 传递给电梯远程监测系统 （弃用，使用 5.8 getHisAlarmFile 接口）
     */
    @Deprecated
    @RequestMapping("/media/getHisVideoFileByAlarmCode")
    public Result getHisVideoFileByAlarmCode(@RequestBody VedioRequest vedioRequest, HttpServletRequest request) {
        String registerNumber = vedioRequest.getRegisterNumber();
        String failureNo = vedioRequest.getAlarmCode();
        failureNo = failureNo.replace("shmx-", "");
        // 故障录制视频
        String videoUrl = tblSysFileService.getVideoUrl(failureNo, 2, 1);
        return Result.success(new VedioResponse(videoUrl), "成功");
    }

    /**
     * 5.8 获取历史告警文件（图片、视频）URL
     *
     * @param access_token token
     * @param requestBody  查询参数
     * @return 视频图片地址
     */
    @RequestMapping("/media/getHisAlarmFile")
    public GetHisAlarmFileRespVO getHisAlarmFile(@RequestParam("access_token") String access_token,
                                                 @RequestBody GetHisAlarmFileReqVO requestBody) {

        if (!access_token.equals(redisUtils.get(RedisKeyUtils.getYiDianTokenCacheKey()))) {
            return new GetHisAlarmFileRespVO(22000002, "false", null);
        }

        //告警编号
        String failureNo = requestBody.getAlarmCode().replace("shmx-", "");

        List<TblSysFile> tblSysFileList = tblSysFileService.getByBusinessId(failureNo);

        if (tblSysFileList != null && tblSysFileList.size() > 0) {

            GetHisAlarmFileRespVO.VideoPicUrls videoPicUrls = new GetHisAlarmFileRespVO.VideoPicUrls();
            videoPicUrls.setExpiration(-1);

            tblSysFileList.forEach(tblSysFile -> {
                //图片
                if (tblSysFile.getVFileType().equals("0")) {
                    if (videoPicUrls.getImageUrls() == null) {
                        videoPicUrls.setImageUrls(new ArrayList<>());
                    }
                    videoPicUrls.getImageUrls().add(tblSysFile.getVUrl());
                }
                //视频
                if (tblSysFile.getVFileType().equals("1")) {
                    videoPicUrls.setUrl(tblSysFile.getVUrl());
                }
            });

            return new GetHisAlarmFileRespVO(0, "true", videoPicUrls);
        }

        return new GetHisAlarmFileRespVO(22100002, "false", null);
    }

    private String createToken(String userName, String password) throws Exception {
        String token = "eyJ0eXBlIjoiSldUIiwiYWxnIjoiQUVTIn0="
                + ".eyJwYXNzd29yZCI6IjExIiwidXNlck5hbWUiOiIxMSIsImlhdCI6MTU1Mzc1NzE1NjA5MH0="
                + ".Rjc0QzVCMzI3MDRGQ0EwMkZFRjcyMTlBNkNBNzg0MEM=";
        // 根据用户名密码生成token
        return token;
    }

    private int getExpiresTime(int days) {
        Date a = new Date();
        int expiresTime = (int) (a.getTime() / 1000) + days * 24 * 60 * 60;
        return expiresTime;
    }

    /**
     * 上海智慧电梯平台-摄像头基本信息
     */
    @PostMapping("/camerainfo")
    public Result getCameraInfo(@RequestBody JSONObject requestBody) {

        List<String> registerNumbers = (List<String>) requestBody.get("registerNumbers");

        return bizCameraService.getCameraInfoByRegisterNumber(registerNumbers);

    }
}
