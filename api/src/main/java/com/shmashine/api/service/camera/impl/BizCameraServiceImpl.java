package com.shmashine.api.service.camera.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.api.config.exception.BizException;
import com.shmashine.api.dao.BizCameraDao;
import com.shmashine.api.dao.CameraAlarmConfigDao;
import com.shmashine.api.entity.CameraAlarmConfig;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.auth.Result;
import com.shmashine.api.module.camera.CameraInfoResult;
import com.shmashine.api.service.camera.BizCameraService;
import com.shmashine.api.util.CameraUtils;
import com.shmashine.common.constants.CameraConstants;
import com.shmashine.common.entity.TblCamera;
import com.shmashine.common.utils.RequestUtil;

/**
 * 摄像头相关接口
 *
 * @author little.li
 */
@Service
public class BizCameraServiceImpl implements BizCameraService {

    private final BizCameraDao bizCameraDao;

    private final CameraAlarmConfigDao cameraAlarmConfigDao;

    /**
     * appKey
     */
    private static final String APP_KEY = "8374cfb69acd473d8b4a65c8837c364a";
    /**
     * appSecret
     */
    private static final String APP_SECRET = "25f086df116c78899863c7fc0b8e24ae";

    /**
     * appTokenUrl
     */
    private static final String APP_TOKEN_URL = "https://open.ys7.com/api/lapp/token/get";

    /**
     * 设备布撤防
     */
    private static final String DEFENCE_SET = "https://open.ys7.com/api/lapp/device/defence/set";

    @Autowired
    public BizCameraServiceImpl(BizCameraDao bizCameraDao, CameraAlarmConfigDao cameraAlarmConfigDao) {
        this.bizCameraDao = bizCameraDao;
        this.cameraAlarmConfigDao = cameraAlarmConfigDao;
    }


    @Override
    public TblCamera getByElevatorId(String elevatorId) {
        return bizCameraDao.getByElevatorId(elevatorId);
    }

    @Override
    public TblCamera getByElevatorCode(String code) {
        return bizCameraDao.getByElevatorCode(code);
    }

    /**
     * 获取萤石云token
     */
    @Override
    public String getEzopenToken() {
        // 拼接请求参数
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(CameraConstants.Ezopen.APP_TOKEN_URL)
                .queryParam("appKey", CameraConstants.Ezopen.APP_KEY)
                .queryParam("appSecret", CameraConstants.Ezopen.APP_SECRET);

        Map<String, Object> queryMap = new HashMap<>();
        ResponseEntity<String> map = RequestUtil.sendPost(builder, queryMap);
        JSONObject body = JSONObject.parseObject(map.getBody());
        return body.getJSONObject("data").getString("accessToken");
    }

    /**
     * 获取天翼物联信息
     */
    @Override
    public HashMap<String, String> getHkCameraInfo(String vCameraId) {

        HashMap<String, String> info = bizCameraDao.getHkCameraInfo(vCameraId);
        if (info == null) {
            return null;
        }

        String tenantNo = info.get("tenantNo");
        String tenantKey = info.get("tenantKey");
        String deviceNo = info.get("deviceNo");

        String result = CameraUtils.getUrl(tenantNo, tenantKey, deviceNo);

        String messageJson = JSONObject.parseObject(result).getString("result");
        JSONObject jsonObject = JSONObject.parseObject(messageJson);
        String rtmpUrl = jsonObject.getString("rtmpUrl");
        String hlsHttp = jsonObject.getString("hlsHttp");
        info.put("rtmpUrl", rtmpUrl);
        info.put("hlsHttp", hlsHttp);

        return info;
    }

    @Override
    public Result getCameraInfoByRegisterNumber(List<String> registerNumbers) {

        if (registerNumbers.size() == 0) {
            return Result.error("registerNumbers不为空");
        }

        List<CameraInfoResult> resultObj = bizCameraDao.getCameraInfoByRegisterNumbers(registerNumbers);

        return Result.success(resultObj, "ok");
    }

    @Override
    public List<CameraAlarmConfig> getCameraAlarmConfig(String elevatorId) {
        return cameraAlarmConfigDao.getByElevatorId(elevatorId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addCameraAlarmConfig(CameraAlarmConfig cameraAlarmConfig) {

        //设备布防
        defenceSet(cameraAlarmConfig.getDevSerial());
        //新增
        long id = IdUtil.getSnowflakeNextId();
        cameraAlarmConfig.setId(id);
        cameraAlarmConfigDao.addCameraAlarmConfig(cameraAlarmConfig);

        return String.valueOf(id);
    }

    @Override
    public String delCameraAlarmConfig(String id) {
        cameraAlarmConfigDao.delCameraAlarmConfig(id);
        return "success";
    }

    @Override
    public String updateCameraAlarmConfig(CameraAlarmConfig cameraAlarmConfig) {
        cameraAlarmConfigDao.updateCameraAlarmConfig(cameraAlarmConfig);
        return "success";
    }


    /**
     * 获取Token
     *
     * @return token
     */
    private static String getAccessToken() {

        // 构建请求参数
        Map<String, Object> requestParam = Map.of("appKey", APP_KEY,
                "appSecret", APP_SECRET);

        String resp = HttpUtil.post(APP_TOKEN_URL, requestParam, 6000);

        return JSON.parseObject(resp).getJSONObject("data").getString("accessToken");
    }


    /**
     * 设备布防
     */
    private static void defenceSet(String deviceSerial) {

        // 构建请求参数
        Map<String, Object> requestParam = Map.of("accessToken", getAccessToken(),
                "deviceSerial", deviceSerial,
                "isDefence", 1);

        String resp = HttpUtil.post(DEFENCE_SET, requestParam, 6000);

        if (!"200".equals(JSON.parseObject(resp).getString("code"))) {
            throw new BizException(ResponseResult.error(JSON.parseObject(resp).getString("msg")));
        }

    }


}