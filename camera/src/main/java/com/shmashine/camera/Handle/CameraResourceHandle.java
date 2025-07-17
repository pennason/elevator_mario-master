package com.shmashine.camera.Handle;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.hutool.core.util.StrUtil;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.camera.service.CameraServer;
import com.shmashine.camera.vo.CameraResourceReqVO;
import com.shmashine.camera.vo.CameraResourceRespVO;
import com.shmashine.cameratysl.client.RemoteCameraTyslClient;
import com.shmashine.common.dto.CamareMediaDownloadRequestDTO;
import com.shmashine.common.entity.TblCameraCascadePlatformEntity;
import com.shmashine.common.enums.CameraMediaTypeEnum;
import com.shmashine.common.enums.CameraTaskTypeEnum;
import com.shmashine.common.enums.CameraTypeEnum;
import com.shmashine.common.utils.CameraUtils;
import com.shmashine.hikYunMou.client.RemoteHikCloudClient;

/**
 * 获取摄像头资源
 *
 * @author  jiangheng
 * @version 2023/3/28 14:29
 */
@Component
public class CameraResourceHandle {

    @Resource
    private RemoteHikCloudClient hikCloudClient;

    @Resource
    private RemoteCameraTyslClient cameraTyslClient;

    @Resource
    private CameraServer cameraServer;

    /**
     * 获取直播流
     *
     * @param reqVO 摄像头信息
     * @return 直播流地址
     */
    // CHECKSTYLE:OFF
    public CameraResourceRespVO getPreviewURL(CameraResourceReqVO reqVO) {

        //摄像头类型 1：海康萤石平台，2：雄迈平台，3：海尔平台，4：海康云眸
        Integer cameraType = reqVO.getCameraType();

        // 1：海康萤石平台，2：雄迈平台，3：海尔平台
        if (cameraType <= 3) {

            Long expireTime = 120L;
            if (reqVO.getExpireTime() != null) {
                expireTime = reqVO.getExpireTime();
            }

            Integer protocol = switch (reqVO.getProtocol()) {
                case "ezopen" -> 1;
                case "hls" -> 2;
                case "rtmp" -> 3;
                case "flv" -> 4;
                default -> 2;
            };

            return CameraResourceRespVO.builder()
                    .url(CameraUtils.getHaiKangUrl(reqVO.getCameraSerial(), expireTime, protocol))
                    .streamToken(CameraUtils.getEzopenToken())
                    .build();
        }

        //海康云眸
        if (CameraTypeEnum.HIK_CLOUD.getCode().equals(cameraType)) {
            String protocol = changeProtocolForHikCloud(reqVO.getProtocol());

            String expireTime = null;
            if (reqVO.getExpireTime() != null) {
                expireTime = String.valueOf(reqVO.getExpireTime());
            }

            //获取流地址
            String finalProtocol = protocol;
            String finalExpireTime = expireTime;
            CompletableFuture<String> future1 = CompletableFuture
                    .supplyAsync(() -> {
                        String resp = hikCloudClient.previewURLs(reqVO.getCameraSerial(), finalProtocol,
                                reqVO.getQuality(), finalExpireTime);
                        var respObj = JSON.parseObject(resp);
                        String url = respObj.getString("data");
                        return url;
                    });

            //获取流token
            CompletableFuture<String> future2 = CompletableFuture
                    .supplyAsync(() -> {
                        String resp = hikCloudClient.getStreamToken();
                        var respObj = JSON.parseObject(resp);
                        String streamToken = respObj.getString("data");
                        return streamToken;
                    });

            CompletableFuture<CameraResourceRespVO> result = future1
                    .thenCombine(future2, (url, streamToken) -> CameraResourceRespVO.builder()
                            .url(url)
                            .streamToken(streamToken)
                            .build());

            return result.join();
        }

        // 5. 天翼云眼，6.中兴
        if (CameraTypeEnum.TYYY.getCode().equals(cameraType)
                || CameraTypeEnum.TYBD.getCode().equals(cameraType)) {
            var protocol = reqVO.getProtocol();
            var protoString = "RTSP";
            if (StringUtils.hasText(protocol) && protocol.equals("hls")) {
                protoString = "HLS";
            }
            var res = cameraTyslClient.getCameraStreamUrl(reqVO.getElevatorCode(), protoString);
            return CameraResourceRespVO.builder()
                    .url(res.getData())
                    .build();
        }

        return null;
    }
    // CHECKSTYLE:ON

    /**
     * 获取回放流
     */
    public CameraResourceRespVO getPlaybackURL(CameraResourceReqVO reqVO) {
        //摄像头类型 1：海康萤石平台，2：雄迈平台，3：海尔平台，4：海康云眸
        Integer cameraType = reqVO.getCameraType();

        // 1：海康萤石平台，2：雄迈平台，3：海尔平台
        if (cameraType <= 3) {
            return CameraResourceRespVO.builder().url("").build();
        }

        //海康云眸
        if (CameraTypeEnum.HIK_CLOUD.getCode().equals(cameraType)) {
            String protocol = changeProtocolForHikCloud(reqVO.getProtocol());
            String expireTime = null;
            if (reqVO.getExpireTime() != null) {
                expireTime = String.valueOf(reqVO.getExpireTime());
            }

            String resp = hikCloudClient.playbackURLs(reqVO.getCameraSerial(), protocol, reqVO.getQuality(),
                    reqVO.getStartTime(), reqVO.getStopTime(), expireTime);

            var respObj = JSON.parseObject(resp);
            String url = respObj.getString("data");

            return CameraResourceRespVO.builder().url(url).build();
        }
        // 5. 天翼云眼，6.中兴
        if (CameraTypeEnum.TYYY.getCode().equals(cameraType)
                || CameraTypeEnum.TYBD.getCode().equals(cameraType)) {
            var res = cameraTyslClient.getVideoPlaybackUrl(CamareMediaDownloadRequestDTO.builder()
                    .elevatorCode(reqVO.getElevatorCode())
                    .collectTime(reqVO.getOccurTime())
                    .startTime(reqVO.getStartTime())
                    .endTime(reqVO.getStopTime())
                    .taskType(CameraTaskTypeEnum.FAULT)
                    .taskCustomId(reqVO.getFaultId())
                    .taskCustomType(Integer.valueOf(reqVO.getFaultType()))
                    .mediaType(CameraMediaTypeEnum.MP4)
                    .build());
            return CameraResourceRespVO.builder()
                    .url(res.getData())
                    .build();
        }


        return null;
    }

    /**
     * 获取图片
     */
    public Object getPictureURL(CameraResourceReqVO reqVO) {

        //摄像头类型 1：海康萤石平台，2：雄迈平台，3：海尔平台，4：海康云眸
        Integer cameraType = reqVO.getCameraType();

        // 1：海康萤石平台，2：雄迈平台，3：海尔平台
        if (cameraType <= 3) {
            return CameraResourceRespVO.builder().url("").build();
        }

        //海康云眸
        if (CameraTypeEnum.HIK_CLOUD.getCode().equals(cameraType)) {

            String resp = hikCloudClient.pictureURL(reqVO.getCameraSerial());
            var respObj = JSON.parseObject(resp);
            String url = respObj.getString("data");
            return CameraResourceRespVO.builder().url(url).build();

        }

        return null;
    }

    public JSONObject getHIkTalkStreamToken(String channelCode) {

        //根据级联编码获取摄像头信息
        TblCameraCascadePlatformEntity camera = cameraServer.queryCameraInfoByChannelCode(channelCode);

        //海康云眸
        if (CameraTypeEnum.HIK_CLOUD.getCode().equals(camera.getCameraType())) {

            //获取流token
            String streamToken = null;
            try {
                String resp = hikCloudClient.getStreamToken();
                var respObj = JSON.parseObject(resp);
                streamToken = respObj.getString("data");
            } catch (Exception e) {
                e.printStackTrace();
            }
            var res = new JSONObject();
            res.put("streamToken", streamToken);
            res.put("scameraSerialNumber", camera.getCloudNumber());

            return res;
        }

        return null;
    }

    private String changeProtocolForHikCloud(String protocol) {
        if (StrUtil.isNotEmpty(protocol)) {
            protocol = switch (protocol) {
                case "rtmp" -> "3";
                case "flv" -> "4";
                default -> "2";
            };
        }
        return protocol;
    }
}
