// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.impl;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson2.JSON;
import com.shmashine.cameratysl.gateway.TyslGateway;
import com.shmashine.cameratysl.gateway.dto.TyslDeviceAddressInformationResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslDeviceBindUserResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslDeviceCloudFileListResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslDeviceInfoResultResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslDevicePlaybackUrlResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslDeviceResourceResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslDeviceStreamUrlResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslImageDeviceGroupListResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslImageDeviceImageListResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslImageImageDetailResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslImageSubscribeResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslMessageSubsListResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslMessageSubscribeResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslTokenResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslVoiceTokenResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslVoiceWssResponseDTO;
import com.shmashine.cameratysl.gateway.dto.requests.DeviceCloudFileListRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.DeviceInfoRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.DevicePlaybackControlRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.DevicePlaybackCutVideoRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.DevicePlaybackUrlRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.DevicePtzControlRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.DeviceStreamUrlRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.ImageDeviceImageDataRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.ImageDeviceImageGroupRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.ImageImageDetailRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.ImageSubscribeRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.ImageUnsubscribeRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.MessageSubscribeRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.MessageUnsubscribeRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.PageRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.VoiceIntercomRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.VoiceWssRequestDTO;
import com.shmashine.cameratysl.properties.TyslProperties;
import com.shmashine.cameratysl.utils.RedisUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 11:23
 * @since v1.0
 */

@Slf4j
@Primary
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TyslGatewayImpl implements TyslGateway {
    private final TyslProperties properties;
    private final RedisUtils redisUtils;
    private final RestTemplate restTemplate;
    private static final String REDIS_ACCESS_TOKEN_PRE = "ACCESS_TOKEN:TYSL:";

    @Override
    public String getAccessToken() {
        var redisKey = REDIS_ACCESS_TOKEN_PRE + properties.getAppId();
        var res = redisUtils.getCacheObject(redisKey);
        if (null != res && StringUtils.hasText(res.toString())) {
            return res.toString();
        }
        return getAccessTokenFromRemote();
    }

    @Override
    public TyslDeviceInfoResultResponseDTO listDeviceInfo(PageRequestDTO requestDTO) {
        var uri = TyslProperties.URI_DEVICE_LIST_POST;
        var res = sendPostRequest(uri, null, JSON.toJSONString(requestDTO));
        return JSON.parseObject(JSON.toJSONString(res), TyslDeviceInfoResultResponseDTO.class);
    }

    @Override
    public TyslDeviceInfoResultResponseDTO getDeviceInfo(DeviceInfoRequestDTO requestDTO) {
        var uri = TyslProperties.URI_DEVICE_INFO_POST;
        var res = sendPostRequest(uri, null, JSON.toJSONString(requestDTO));
        return JSON.parseObject(JSON.toJSONString(res), TyslDeviceInfoResultResponseDTO.class);
    }

    @Override
    public TyslDeviceStreamUrlResponseDTO getDeviceStreamUrl(DeviceStreamUrlRequestDTO requestDTO) {
        var uri = TyslProperties.URI_DEVICE_STREAM_URL_POST;
        var res = sendPostRequest(uri, null, JSON.toJSONString(requestDTO));
        return JSON.parseObject(JSON.toJSONString(res), TyslDeviceStreamUrlResponseDTO.class);
    }

    @Override
    public TyslDevicePlaybackUrlResponseDTO listDevicePlaybackUrl(DevicePlaybackUrlRequestDTO requestDTO) {
        var uri = TyslProperties.URI_DEVICE_PLAYBACK_URL_POST;
        var res = sendPostRequest(uri, null, JSON.toJSONString(requestDTO));
        return JSON.parseObject(JSON.toJSONString(res), TyslDevicePlaybackUrlResponseDTO.class);
    }

    @Override
    public TyslResponseDTO<String> getCuttingVideoDownloadUrl(DevicePlaybackCutVideoRequestDTO requestDTO) {
        var uri = TyslProperties.URI_DEVICE_CUTTING_VIDEO_DOWNLOAD_URL_POST;
        return sendPostRequest(uri, null, JSON.toJSONString(requestDTO));
    }

    @Override
    public TyslResponseDTO<Boolean> playbackControl(DevicePlaybackControlRequestDTO request) {
        var uri = TyslProperties.URI_PLAYBACK_CONTROL_POST;
        return sendPostRequest(uri, null, JSON.toJSONString(request));
    }

    @Override
    public TyslDeviceCloudFileListResponseDTO listDeviceCloudFile(DeviceCloudFileListRequestDTO requestDTO) {
        var uri = TyslProperties.URI_DEVICE_CLOUD_FILE_LIST_POST;
        var res = sendPostRequest(uri, null, JSON.toJSONString(requestDTO));
        return JSON.parseObject(JSON.toJSONString(res), TyslDeviceCloudFileListResponseDTO.class);
    }

    @Override
    public TyslDeviceBindUserResponseDTO getDeviceBindUser(DeviceInfoRequestDTO requestDTO) {
        var uri = TyslProperties.URI_DEVICE_BIND_USER_POST;
        var res = sendPostRequest(uri, null, JSON.toJSONString(requestDTO));
        return parseTyslResponseDTO(res, TyslDeviceBindUserResponseDTO.class);
    }

    @Override
    public TyslDeviceResourceResponseDTO getDeviceResource(DeviceInfoRequestDTO requestDTO) {
        var uri = TyslProperties.URI_DEVICE_INFO_POST;
        var res = sendPostRequest(uri, null, JSON.toJSONString(requestDTO));
        return parseTyslResponseDTO(res, TyslDeviceResourceResponseDTO.class);
    }

    @Override
    public TyslDeviceAddressInformationResponseDTO getDeviceAddressInformation(DeviceInfoRequestDTO requestDTO) {
        var uri = TyslProperties.URI_DEVICE_ADDRESS_INFO_POST;
        var res = sendPostRequest(uri, null, JSON.toJSONString(requestDTO));
        return parseTyslResponseDTO(res, TyslDeviceAddressInformationResponseDTO.class);
    }

    @Override
    public TyslResponseDTO<Object> devicePtzControl(DevicePtzControlRequestDTO requestDTO) {
        var uri = TyslProperties.URI_DEVICE_PTZ_CONTROL_POST;
        return sendPostRequest(uri, null, JSON.toJSONString(requestDTO));
    }

    @Override
    public TyslMessageSubscribeResponseDTO subscribeMessage(MessageSubscribeRequestDTO requestDTO) {
        var uri = TyslProperties.URI_MESSAGE_SUBSCRIBE_POST;
        var res = sendPostRequest(uri, null, JSON.toJSONString(requestDTO));
        return parseTyslResponseDTO(res, TyslMessageSubscribeResponseDTO.class);
    }

    @Override
    public TyslResponseDTO<Object> unsubscribeMessage(MessageUnsubscribeRequestDTO requestDTO) {
        var uri = TyslProperties.URI_MESSAGE_UNSUBSCRIBE_POST;
        return sendPostRequest(uri, null, JSON.toJSONString(requestDTO));
    }

    @Override
    public TyslMessageSubsListResponseDTO listMessageSubs() {
        var uri = TyslProperties.URI_MESSAGE_SUBSCRIBE_LIST_POST;
        var res = sendPostRequest(uri, null, "{}");
        return parseTyslResponseDTO(res, TyslMessageSubsListResponseDTO.class);
    }

    @Override
    public TyslImageDeviceImageListResponseDTO listImageDeviceImage(ImageDeviceImageDataRequestDTO requestDTO) {
        var uri = TyslProperties.URI_IMAGE_DEVICE_IMAGE_DATA_LIST_POST;
        var res = sendPostRequest(uri, null, JSON.toJSONString(requestDTO));
        return parseTyslResponseDTO(res, TyslImageDeviceImageListResponseDTO.class);
    }

    @Override
    public TyslImageDeviceGroupListResponseDTO listImageDeviceGroup(ImageDeviceImageGroupRequestDTO requestDTO) {
        var uri = TyslProperties.URI_IMAGE_DEVICE_IMAGE_GROUP_LIST_POST;
        var res = sendPostRequest(uri, null, JSON.toJSONString(requestDTO));
        return parseTyslResponseDTO(res, TyslImageDeviceGroupListResponseDTO.class);
    }

    @Override
    public TyslImageImageDetailResponseDTO getImageDetail(ImageImageDetailRequestDTO requestDTO) {
        var uri = TyslProperties.URI_IMAGE_IMAGE_DETAIL_POST;
        var res = sendPostRequest(uri, null, JSON.toJSONString(requestDTO));
        return parseTyslResponseDTO(res, TyslImageImageDetailResponseDTO.class);
    }

    @Override
    public TyslImageSubscribeResponseDTO subscribeImage(ImageSubscribeRequestDTO requestDTO) {
        var uri = TyslProperties.URI_IMAGE_SUBSCRIBE_POST;
        var res = sendPostRequest(uri, null, JSON.toJSONString(requestDTO));
        return parseTyslResponseDTO(res, TyslImageSubscribeResponseDTO.class);
    }

    @Override
    public TyslImageSubscribeResponseDTO unsubscribeImage(ImageUnsubscribeRequestDTO requestDTO) {
        var uri = TyslProperties.URI_IMAGE_UNSUBSCRIBE_POST;
        var res = sendPostRequest(uri, null, JSON.toJSONString(requestDTO));
        return parseTyslResponseDTO(res, TyslImageSubscribeResponseDTO.class);
    }

    @Override
    public TyslResponseDTO<Object> voiceIntercom(VoiceIntercomRequestDTO requestDTO) {
        /*var token = getAccessToken();
        var url = TyslProperties.BASE_URL + TyslProperties.URI_VOICE_INTERCOM_POST;
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("transactionId", buildTransactionId("TYSL-POST-FORM"));
        headers.set("token", token);

        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("guid", requestDTO.getGuid());
        form.add("voiceData", requestDTO.getVoiceData().getResource());

        var entity = new HttpEntity<>(form, headers);
        log.info("tysl voiceIntercom url:{}, header:{}", url, JSON.toJSONString(headers));
        var res = restTemplate.postForEntity(url, entity, TyslResponseDTO.class);
        //var res = restTemplate.exchange(url, HttpMethod.POST, entity, TyslResponseDTO.class);
        log.info("tysl voiceIntercom uri:{}, request:{}, response:{}", url, requestDTO, JSON.toJSONString(res));
        return res.getBody();*/

        var uri = TyslProperties.URI_VOICE_INTERCOM_POST;
        var map = Map.of("guid", requestDTO.getGuid(), "voiceData", requestDTO.getVoiceData().getResource());
        return sendPostForm(uri, map);
    }

    @Override
    public TyslVoiceWssResponseDTO voiceWss(VoiceWssRequestDTO requestDTO) {
        var uri = TyslProperties.URI_VOICE_TALKBACK_TOKEN_POST;
        var res = sendPostRequest(uri, null, JSON.toJSONString(requestDTO));
        return parseTyslResponseDTO(res, TyslVoiceWssResponseDTO.class);
    }

    @Override
    public TyslVoiceTokenResponseDTO voiceStreamToken(String guid) {
        var uri = TyslProperties.URI_VOICE_STREAM_TOKEN_GET;
        var res = sendGetRequest(uri, null, Map.of("guid", guid));
        return parseTyslResponseDTO(res, TyslVoiceTokenResponseDTO.class);
    }


    ///////////////// 私有方法 /////////////////

    private TyslResponseDTO sendPostForm(String uri, Map<String, Object> formMap) {
        var token = getAccessToken();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("transactionId", buildTransactionId("TYSL-POST-FORM"));
        headers.set("token", token);

        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        if (!formMap.isEmpty()) {
            for (var item : formMap.entrySet()) {
                form.add(item.getKey(), item.getValue());
            }
        }
        var url = TyslProperties.BASE_URL + uri;
        var entity = new HttpEntity<>(form, headers);
        log.info("tysl sendPostForm url:{}, header:{}", url, JSON.toJSONString(headers));
        var res = restTemplate.postForEntity(url, entity, TyslResponseDTO.class);
        //var res = restTemplate.exchange(url, HttpMethod.POST, entity, TyslResponseDTO.class);
        log.info("tysl sendPostForm uri:{}, request:{}, response:{}", url, formMap, JSON.toJSONString(res));
        return res.getBody();
    }

    private TyslResponseDTO sendPostRequest(String uri, HttpHeaders headers, String requestBody) {
        var token = getAccessToken();
        if (null == headers) {
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        headers.set("transactionId", buildTransactionId("TYSL-POST"));
        headers.set("token", token);
        var url = TyslProperties.BASE_URL + uri;

        var entity = new HttpEntity<>(requestBody, headers);
        log.info("tysl sendPostRequest uri:{}, request:{}, ", url, JSON.toJSONString(entity));
        var res = restTemplate.exchange(url, HttpMethod.POST, entity, TyslResponseDTO.class);
        log.info("tysl sendPostRequest uri:{}, response:{}", url, JSON.toJSONString(res));
        return res.getBody();
        /*if (null == res.getBody()) {
            log.error("tysl sendPostRequest error body is null, uri:{}, request:{}, response:{}",
                    url, JSON.toJSONString(entity), JSON.toJSONString(res));
            return null;
        }
        if (clazz.isAssignableFrom(TyslResponseDTO.class)) {
            return JSON.parseObject(JSON.toJSONString(res.getBody()), clazz);
        }
        if (HttpStatus.OK.value() != res.getBody().getCode()) {
            log.error("tysl sendPostRequest error, uri:{}, request:{}, response:{}",
                    url, JSON.toJSONString(entity), JSON.toJSONString(res));
            return null;
        }
        log.info("tysl sendPostRequest uri:{}, request:{}, response:{}",
                url, JSON.toJSONString(entity), JSON.toJSONString(res));
        if (null == res.getBody().getData()) {
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(res.getBody().getData()), clazz);*/
    }

    private TyslResponseDTO sendGetRequest(String uri, HttpHeaders headers, Map<String, Object> uriVariables) {
        var token = getAccessToken();
        if (null == headers) {
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        headers.set("transactionId", buildTransactionId("TYSL-POST"));
        headers.set("token", token);
        var url = TyslProperties.BASE_URL + uri;

        var entity = new HttpEntity<>(null, headers);
        var res = restTemplate.exchange(url, HttpMethod.GET, entity, TyslResponseDTO.class, uriVariables);
        log.info("tysl sendGetRequest uri:{} {}, response:{}", url, uriVariables, JSON.toJSONString(res));
        return res.getBody();
    }

    private <T extends TyslResponseDTO> T parseTyslResponseDTO(TyslResponseDTO res, Class<T> clazz) {
        if (null == res) {
            return null;
        }
        var returnData = JSON.parseObject(JSON.toJSONString(res), clazz);
        return returnData;
    }

    private String getAccessTokenFromRemote() {
        var timestamp = String.valueOf(System.currentTimeMillis());
        var headers = new HttpHeaders();
        headers.set("transactionId", buildTransactionId("TYSL-TOKEN"));
        headers.set("appKey", buildAppKey());
        headers.set("sign", buildSign(timestamp));

        var entity = new HttpEntity<>(headers);
        var uri = TyslProperties.BASE_URL + TyslProperties.URI_ACCESS_TOKEN_GET;

        var res = restTemplate.exchange(uri, HttpMethod.GET, entity, TyslTokenResponseDTO.class, properties.getAppId());
        log.info("tysl getAccessTokenFromRemote uri:{}, request:{}, response:{}",
                uri.replace("{appId}", properties.getAppId()), JSON.toJSONString(entity), JSON.toJSONString(res));
        if (null == res.getBody()) {
            return null;
        }
        if (HttpStatus.OK.value() != res.getBody().getCode()) {
            log.error("tysl getAccessTokenFromRemote error, code:{}, msg:{}",
                    res.getBody().getCode(), res.getBody().getMsg());
            return null;
        }
        var tokenInfo = res.getBody().getData();
        if (null == tokenInfo) {
            log.error("tysl getAccessTokenFromRemote error, tokenInfo is null");
            return null;
        }
        var accessToken = tokenInfo.getAccessToken();
        var expiresTime = tokenInfo.getExpiresTime();
        var expiresIn = 24 * 3600L - 300L;
        if (null != expiresTime) {
            expiresIn = (expiresTime - System.currentTimeMillis()) / 1000L - 300L;
        }
        var redisKey = REDIS_ACCESS_TOKEN_PRE + properties.getAppId();
        redisUtils.setCacheObject(redisKey, accessToken, expiresIn, TimeUnit.SECONDS);
        return accessToken;
    }

    private String buildTransactionId(String prefix) {
        return (StringUtils.hasText(prefix) ? prefix : "") + UUID.randomUUID().toString();
    }

    private String buildAppKey() {
        return DigestUtils.sha256Hex(properties.getAppKey());
    }

    /**
     * 构建签名
     *
     * @param timestamp 毫秒时间戳
     * @return 签名
     */
    private String buildSign(String timestamp) {
        try {
            var secretKey = properties.getAppId().substring(0, 8)
                    + properties.getAppKey().substring(properties.getAppKey().length() - 8);
            var playText = properties.getAppId() + timestamp;   // System.currentTimeMillis()
            var raw = secretKey.getBytes(StandardCharsets.UTF_8);
            var keySpec = new SecretKeySpec(raw, "AES");
            var cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(1, keySpec);
            var p = playText.getBytes(StandardCharsets.UTF_8);
            var result = cipher.doFinal(p);
            return Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
