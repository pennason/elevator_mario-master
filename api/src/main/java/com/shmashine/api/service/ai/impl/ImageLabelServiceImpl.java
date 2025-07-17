// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.service.ai.impl;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson2.JSON;
import com.shmashine.api.dao.BizFaultTempDao;
import com.shmashine.api.dao.ImageLabelMapper;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.properties.EndpointProperties;
import com.shmashine.api.service.ai.ImageLabelServiceI;
import com.shmashine.common.dto.ImageLabelMarkRequestDTO;
import com.shmashine.common.dto.ImageLabelNotMarkDTO;
import com.shmashine.common.entity.ImageLabelsEntity;
import com.shmashine.common.enums.CameraImageIdentifyEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/11/9 15:31
 * @since v1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ImageLabelServiceImpl implements ImageLabelServiceI {
    private final ImageLabelMapper imageLabelMapper;
    private final BizFaultTempDao faultTempDao;
    private final RestTemplate restTemplate;
    private final EndpointProperties endpointProperties;

    private static final Integer LABEL_IMAGE_PAGE_SIZE = 10;

    /**
     * 图像标注服务器， 保存标注信息的接口
     */
    private static final String URI_POST_IMAGE_LABEL_SAVE = "/api/image-label-save";
    private static final String URI_GET_IMAGE_LABEL_CLASSIFY = "/api/list-image-label-classes";

    @Override
    public ImageLabelsEntity getImageLabelById(Long id) {
        return imageLabelMapper.getById(id);
    }

    @Override
    public ImageLabelsEntity getElectricBikeImageLabelByFaultId(String faultId) {
        return imageLabelMapper.getByFaultId(faultId);
    }

    @Override
    public List<ImageLabelNotMarkDTO> getNoLabelRoundImages(CameraImageIdentifyEnum markType) {
        // 助动车标注
        if (CameraImageIdentifyEnum.IMAGE_LABEL_ELECTRIC_BIKE.equals(markType)) {
            // 获取 最近 200张 随机选择其中10张
            var res = imageLabelMapper.getNoLabelRoundImagesForElectricBike(200);
            if (res == null || res.size() <= LABEL_IMAGE_PAGE_SIZE) {
                return res;
            }
            // 随机获取指定范围内的数据
            var startRow = (new Random()).nextInt(res.size() - LABEL_IMAGE_PAGE_SIZE);
            return res.subList(startRow, startRow + LABEL_IMAGE_PAGE_SIZE);
        }
        return null;
    }

    @Override
    public ResponseResult listLabelClassify() {
        var url = endpointProperties.getAiImageLabel() + URI_GET_IMAGE_LABEL_CLASSIFY;
        var res = restTemplate.getForObject(url, String.class);
        log.info("aiImageLabel classify url: {}, response: {}", url, res);
        return JSON.parseObject(res, ResponseResult.class);
    }

    @Override
    public ResponseResult saveImageLabel(ImageLabelMarkRequestDTO requestDTO) {
        // 一、 助动车的标注
        if (CameraImageIdentifyEnum.IMAGE_LABEL_ELECTRIC_BIKE.getCode().equals(requestDTO.getMarkType())) {
            // 1. 从助动车表中获取相关信息
            var imageLabel = imageLabelMapper.getSourceByFaultId(requestDTO.getFaultId());
            if (imageLabel == null) {
                return ResponseResult.error("故障不存在");
            }
            // 2. 更新故障表中的标注状态
            faultTempDao.updateMarkLabel(requestDTO.getFaultId(), 1);

            // 3. 存储标注信息 1.不需要标注的情况
            if (CollectionUtils.isEmpty(requestDTO.getLabels())) {
                imageLabel.setMarkNeed(0);
                imageLabel.setMarkContent(null);
                imageLabelMapper.insertEntity(imageLabel);
                return ResponseResult.success();
            }
            // 3. 存储标注信息  2. 需要标注的情况
            imageLabel.setMarkNeed(1);
            imageLabel.setMarkContent(JSON.toJSONString(requestDTO));
            imageLabelMapper.insertEntity(imageLabel);

            // 4. 发送标注请求到图像标注服务器
            return sendMarkRequestToImageLabelServer(requestDTO);
        }

        return ResponseResult.error("不支持的类型");
    }

    private ResponseResult sendMarkRequestToImageLabelServer(ImageLabelMarkRequestDTO requestDTO) {
        var url = endpointProperties.getAiImageLabel() + URI_POST_IMAGE_LABEL_SAVE;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        var request = JSON.toJSONString(requestDTO);

        var httpEntity = new HttpEntity<>(request, httpHeaders);
        var res = restTemplate.postForObject(url, httpEntity, String.class);
        log.info("aiImageLabel url: {}, request: {}, response: {}", url, request, res);
        return JSON.parseObject(res, ResponseResult.class);
    }
}
