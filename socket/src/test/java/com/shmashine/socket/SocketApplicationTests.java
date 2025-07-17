package com.shmashine.socket;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.fastjson2.JSONObject;
import com.baidu.aip.bodyanalysis.AipBodyAnalysis;
import com.shmashine.common.constants.ServiceConstants;
import com.shmashine.common.utils.SnowFlakeUtils;
import com.shmashine.common.utils.TimeUtils;
import com.shmashine.socket.camera.service.TblCameraServiceI;
import com.shmashine.socket.fault.entity.TblFaultDefinition;
import com.shmashine.socket.fault.service.TblFaultDefinitionServiceI;
import com.shmashine.socket.kafka.KafkaProducer;
import com.shmashine.socket.message.MessageHandle;
import com.shmashine.socket.message.bean.MessageData;
import com.shmashine.socket.redis.utils.RedisUtils;

@SpringBootTest(classes = SocketApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SocketApplicationTests {

    @Autowired
    private TblFaultDefinitionServiceI faultDefinitionService;

    @Autowired
    private MessageHandle messageHandle;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private TblCameraServiceI cameraService;

    @Autowired
    private RedisUtils redisUtils;


    @Test
    void sendFaultMessage() {
        String message = "{\"elevatorCode\":\"MX3737\",\"ST\":\"add\",\"TY\":\"Fault\",\"sensorType\":\"CarRoof\","
                + "\"fault_type\":20,\"time\":\"2020-06-30 13:30:27\"}";
        JSONObject messageJson = JSONObject.parseObject(message);
        MessageData messageData = new MessageData();
        messageData.setElevatorCode(messageJson.getString("elevatorCode"));
        messageData.setST(messageJson.getString("ST"));
        messageData.setSensorType(messageJson.getString("sensorType"));
        messageData.setMessageJson(messageJson);
        messageHandle.faultMessageHandle(messageData);
    }


    @Test
    void updateFaultDefinitionId() {
        List<TblFaultDefinition> elevatorList = faultDefinitionService.list(new HashMap<>());
        for (TblFaultDefinition elevator : elevatorList) {
            long nextId = SnowFlakeUtils.nextLongId();
            String id = elevator.getVFaultDefinitionId();
            faultDefinitionService.updateFaultDefinitionId(id, nextId);
            System.out.printf("update %s --- elevatorId %s\n", id, nextId);
        }
    }


    @Test
    void test() {
        faceRecognition("MX3390");
    }

    /**
     * 调用百度算法识别是否有人
     *
     * @param elevatorCode 电梯编号
     * @return -1 没有视频源 or 返回结果解析错误；0 没有识别到人；other 人数
     */
    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    int faceRecognition(String elevatorCode) {
        try {
            String rtmpUrl = cameraService.getRtmpUrlByElevatorCode(elevatorCode);
            String hlsUrl = cameraService.getHlsUrlByElevatorCode(elevatorCode);
            // TODO test
            String path = "D:\\Desktop\\" + TimeUtils.getTenBitTimestamp() + ".jpg";

            if (!StringUtils.isNotBlank(rtmpUrl) && !StringUtils.isNotBlank(rtmpUrl)) {
                // 没有视频源返回 -1
                return -1;
            }

            String sourceUrl = "";
            if (StringUtils.isNotBlank(rtmpUrl)) {
                sourceUrl = rtmpUrl;
            } else if (StringUtils.isNotBlank(hlsUrl)) {
                sourceUrl = hlsUrl;
            }

            // 截取视频图片
            String command = "ffmpeg -i " + sourceUrl + " -vframes 1 -y -f image2 -t 0.001 " + path;
            try {
                Process process = Runtime.getRuntime().exec(command);
                process.waitFor();
                System.out.printf("%s --- %s --- 困人 --- Baidu识别中...\n", TimeUtils.nowTime(), elevatorCode);
            } catch (IOException | InterruptedException ioException) {
                ioException.printStackTrace();
            }

            // 初始化一个AipBodyAnalysis
            AipBodyAnalysis client = new AipBodyAnalysis(ServiceConstants.APP_ID, ServiceConstants.API_KEY,
                    ServiceConstants.SECRET_KEY);

            // 可选：设置网络连接参数
            client.setConnectionTimeoutInMillis(2000);
            client.setSocketTimeoutInMillis(10000);

            // 调用接口
            org.json.JSONObject res = client.bodyAnalysis(path, new HashMap<>());
            if (!res.has("error_code")) {
                int personNum = 0;
                JSONArray personInfo = res.getJSONArray("person_info");
                if (personInfo.length() == 0) {
                    System.out.printf("%s --- %s --- 困人 --- Baidu识别结果: %s\n", TimeUtils.nowTime(), elevatorCode, 0);
                    return -1;
                }

                double score = personInfo.getJSONObject(0).getJSONObject("location").getDouble("score");
                if (score < 0.03) {
                    System.out.printf("%s --- %s --- 困人 --- Baidu识别结果: score: %s\n",
                            TimeUtils.nowTime(), elevatorCode, score);
                    return -1;
                }

                for (int i = 0; i < personInfo.length(); i++) {
                    double countScore = 0.0;
                    org.json.JSONObject jsonObject = personInfo.getJSONObject(i);
                    org.json.JSONObject bodyParts = jsonObject.getJSONObject("body_parts");
                    String[] names = org.json.JSONObject.getNames(bodyParts);
                    for (String name : names) {
                        org.json.JSONObject soc = bodyParts.getJSONObject(name);
                        countScore += soc.getDouble("score");
                    }
                    if (countScore / 21 > 0.2) {
                        personNum++;
                    }
                }
                System.out.printf("%s --- %s --- 困人 --- Baidu识别结果: %s\n", TimeUtils.nowTime(), elevatorCode, personNum);
                return personNum;
            } else {
                System.out.printf("%s --- %s --- 困人 --- Baidu识别结果: errorCode: %s\n",
                        TimeUtils.nowTime(), elevatorCode, res.getString("error_code"));
                return -1;
            }

        } catch (Exception e) {
            System.out.printf("faceRecognition: [%s] exception: [%s]", elevatorCode, e.getMessage());
        }
        return -1;
    }


}
