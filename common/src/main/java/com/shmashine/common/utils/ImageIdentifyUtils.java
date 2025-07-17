// Copyright (C) 2024 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.http.HttpRequest;

import com.alibaba.fastjson2.JSONObject;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/10/23 10:16
 * @since v1.0
 */

@Slf4j
public class ImageIdentifyUtils {
    public static final String IDENTIFY_TYPE_PERSON = "person";
    public static final String IDENTIFY_TYPE_MOTOR_CYCLE = "motorcycle";


    public static void identifyImage(String taskCustomId, String ossUrl, String identifyType) {
        identifyImage(taskCustomId, ossUrl, identifyType, false);
    }

    public static void identifyImage(String taskCustomId, String ossUrl, String identifyType, Boolean useInternal) {

        ossUrl = useInternal
                ? OssInternalUtils.changeToInternalUrl(ossUrl)
                : OssInternalUtils.changeToExternalUrl(ossUrl);

        //拼接请求参数
        var url = "http://172.31.183.101:10089/?type=" + identifyType + "&url=" + ossUrl + "&faultId=" + taskCustomId;

        //var res = HttpUtil.get(url, 500);
        //log.info("identifyImage request result {}", res);

        //异步请求
        try {
            HttpRequest.get(url).timeout(500).executeAsync();
        } catch (Exception e) {
            //
        }
        /*HttpClient.newHttpClient()
                .sendAsync(java.net.http.HttpRequest.newBuilder(URI.create(url))
                                .timeout(Duration.ofMillis(200L))
                                .build(),
                        java.net.http.HttpResponse.BodyHandlers.discarding());*/
    }


    /**
     * 绘制并填充多边形
     *
     * @param srcImagePath 源图像路径
     * @param points       坐标数组
     * @param imageFormat  写入图形格式
     * @param toPath       目标路径
     */
    public static void drawAndAlphaPolygon(String srcImagePath, List<JSONObject> points, String imageFormat,
                                           String toPath) {
        drawAndAlphaPolygon(srcImagePath, points, imageFormat, toPath, false);
    }

    public static void drawAndAlphaPolygon(String srcImagePath, List<JSONObject> points, String imageFormat,
                                           String toPath, Boolean useInternal) {
        try (var fos = new FileOutputStream(toPath)) {
            //获取图片
            srcImagePath = useInternal
                    ? OssInternalUtils.changeToInternalUrl(srcImagePath)
                    : OssInternalUtils.changeToExternalUrl(srcImagePath);
            var image = ImageIO.read(new URL(srcImagePath));
            //根据xy点坐标绘制闭合多边形
            var g2d = image.createGraphics();
            g2d.setColor(Color.BLACK);

            for (var point : points) {
                var pointsX = Arrays.stream(point.getObject("x", Integer[].class)).mapToInt(Integer::intValue)
                        .toArray();
                var pointsY = Arrays.stream(point.getObject("y", Integer[].class)).mapToInt(Integer::intValue)
                        .toArray();
                g2d.fillPolygon(pointsX, pointsY, pointsX.length);
            }
            ImageIO.write(image, imageFormat, fos);
            g2d.dispose();
        } catch (RuntimeException | IOException e) {
            log.error("drawAndAlphaPolygon error ", e);
        }
    }


    /**
     * 获取图片分辨率
     *
     * @param imgPath 图片地址
     * @return 数组
     */

    public static Integer[] getImgSize(String imgPath) {
        return getImgSize(imgPath, false);
    }

    public static Integer[] getImgSize(String imgPath, Boolean useInternal) {
        Integer[] size = new Integer[2];
        try {
            imgPath = useInternal
                    ? OssInternalUtils.changeToInternalUrl(imgPath)
                    : OssInternalUtils.changeToExternalUrl(imgPath);
            BufferedImage image = ImageIO.read(new URL(imgPath));
            size[0] = image.getWidth();
            size[1] = image.getHeight();
        } catch (IOException e) {
            log.error("getImageSize {} error {}", imgPath, ExceptionUtil.stacktraceToString(e));
        }
        return size;
    }
}
