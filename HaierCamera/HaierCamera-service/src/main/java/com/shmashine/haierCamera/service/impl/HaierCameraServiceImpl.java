package com.shmashine.haierCamera.service.impl;


import java.util.Base64;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import cn.hutool.json.JSONObject;

import com.shmashine.haierCamera.constants.HaierCameraConstants;
import com.shmashine.haierCamera.dao.CameraDao;
import com.shmashine.haierCamera.dao.ElevatorDao;
import com.shmashine.haierCamera.entity.HaierCameraFault;
import com.shmashine.haierCamera.entity.HaierCameraResponseResult;
import com.shmashine.haierCamera.entity.TblElevator;
import com.shmashine.haierCamera.kafka.KafkaProducer;
import com.shmashine.haierCamera.kafka.KafkaTopicConstants;
import com.shmashine.haierCamera.service.HaierCameraService;
import com.shmashine.haierCamera.utils.HaierCameraUtils;
import com.shmashine.haierCamera.utils.JsonUtils;
import com.shmashine.haierCamera.utils.SnowFlakeUtils;


/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/10/18 15:00
 */
@Service
public class HaierCameraServiceImpl implements HaierCameraService {

    private static Logger haierCameraLogger = LoggerFactory.getLogger("haierCameraLogger");

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Resource
    private CameraDao cameraDao;

    @Resource
    private ElevatorDao elevatorDao;

    @Autowired
    private HaierCameraUtils haierCameraUtils;

    @Override
    public String getCameraHlsUrlByElevatorId(String elevatorId) {

        //获取电梯注册码
        String registerCode = cameraDao.getRegisterCodeById(elevatorId);
        //获取token
        String token = haierCameraUtils.getToken();

        String url = getUrl(registerCode, token, 180L);
        return url;
    }

    @Override
    public HaierCameraResponseResult pushFault(HaierCameraFault haierCameraFault) {

        if (!StringUtils.isEmpty(haierCameraFault.getRegisterCode()) && "10101".equals(haierCameraFault.getFaultCode())) {

            String id = SnowFlakeUtils.nextStrId();
            TblElevator elevator = elevatorDao.queryEleByResCode(haierCameraFault.getRegisterCode());

            final Base64.Encoder encoder = Base64.getEncoder();
            byte floor = toBytes(Integer.parseInt(haierCameraFault.getFloor()))[0];
            byte[] x = {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, floor, 0, 0, 0, 0, 0};
            //编码
            final String encodedText = encoder.encodeToString(x);

            String type = haierCameraFault.getType() == 1 ? "add" : "disappear";

            String message =
                    "{\"elevatorCode\":\"" + elevator.getVElevatorCode() + "\"," +
                            "\"ST\":\"" + type + "\"," +
                            "\"uncivilizedBehaviorFlag\":1," +
                            "\"D\":\"" + encodedText + "\"," +
                            "\"TY\":\"Fault\"," +
                            "\"sensorType\":\"CarRoof\"," +
                            "\"fault_type\":37," +
                            "\"faultId\":\"" + id + "\"," +
                            "\"time\":\"" + haierCameraFault.getCreateTime() + "\"," +
                            "\"faultName\":\"电动车乘梯\"}";

            kafkaProducer.sendMessageToKafka(KafkaTopicConstants.PRE_FAULT_TOPIC, message);

            haierCameraLogger.info("------故障推送fault,message:{}", JsonUtils.toJson(haierCameraFault));
            return HaierCameraResponseResult.success();

        } else {
            haierCameraLogger.info("-------注册码为空或者故障code错误，registerCode：{}", haierCameraFault.getRegisterCode());
            return HaierCameraResponseResult.error("registerCode is null or faultCode error");
        }
    }


    /**
     * 获取海尔摄像头直播流
     *
     * @param registerCode 唯一注册码
     * @param token
     * @param expireTime   流失效时间默认3分钟
     * @return
     */
    private String getUrl(String registerCode, String token, Long expireTime) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("token", token);

        JSONObject reqBody = new JSONObject();
        reqBody.set("registerCode", registerCode);
        reqBody.set("enable", 1);
        reqBody.set("expireTime", expireTime);

        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(reqBody, httpHeaders);

        String result = null;
        try {
            result = restTemplate.postForObject(HaierCameraConstants.REALTIMEVOD_URl, httpEntity, String.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static byte[] toBytes(int number) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) number;
        bytes[1] = (byte) (number >> 8);
        bytes[2] = (byte) (number >> 16);
        bytes[3] = (byte) (number >> 24);
        return bytes;
    }
}
