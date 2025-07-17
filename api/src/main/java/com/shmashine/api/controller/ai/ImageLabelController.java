// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.controller.ai;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSON;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.service.ai.ImageLabelServiceI;
import com.shmashine.common.dto.ImageLabelMarkRequestDTO;
import com.shmashine.common.enums.CameraImageIdentifyEnum;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 图像标注
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/11/9 15:28
 * @since v1.0
 */

@Slf4j
@RestController
@RequestMapping("/ai/image-label")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Tag(name = "图像标注", description = "图像标注相关接口 - powered by ChenXue")
public class ImageLabelController extends BaseRequestEntity {
    private final ImageLabelServiceI imageLabelService;

    @Operation(summary = "获取未标注的电动车识别图片")
    @GetMapping("/list-no-label-images/electric-bike")
    public ResponseResult listUnmarkImagesForElectricBike() {
        return ResponseResult.successObj(imageLabelService.getNoLabelRoundImages(CameraImageIdentifyEnum.IMAGE_LABEL_ELECTRIC_BIKE));
    }

    @Operation(summary = "获取图像标注分类")
    @GetMapping("/list-label-classify")
    public ResponseResult listLabelClassify() {
        return imageLabelService.listLabelClassify();
    }

    @Operation(summary = "存储图像标注信息")
    @PostMapping("/save-image-label")
    public ResponseResult saveImageLabel(@RequestBody @Valid ImageLabelMarkRequestDTO requestDTO) {
        return imageLabelService.saveImageLabel(requestDTO);
    }

    @Operation(summary = "根据故障ID获取图像标注信息")
    @GetMapping("/electric-bike/{faultId}")
    public ResponseResult getElectricBikeImageLabelByFaultId(@PathVariable("faultId") String faultId) {
        var res = imageLabelService.getElectricBikeImageLabelByFaultId(faultId);
        if (res == null) {
            return ResponseResult.successObj(Map.of());
        }
        return ResponseResult.successObj(JSON.parse(res.getMarkContent()));
    }

    @Operation(summary = "根据ID获取图像标注信息")
    @GetMapping("/detail/{id}")
    public ResponseResult getElectricBikeImageLabelByFaultId(@PathVariable("id") Long id) {
        var res = imageLabelService.getImageLabelById(id);
        if (res == null) {
            return ResponseResult.successObj(Map.of());
        }
        return ResponseResult.successObj(JSON.parse(res.getMarkContent()));
    }

}
