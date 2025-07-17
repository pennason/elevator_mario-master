// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 16:26
 * @since v1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TyslImageDeviceGroupListResponseDTO
        extends TyslResponseDTO<TyslImageDeviceGroupListResponseDTO.ImageDeviceGroupListDTO> {

    /**
     * 设备图
     */
    @Data
    public static class ImageDeviceGroupListDTO implements Serializable {
        /**
         * 查询总数
         */
        private Integer total;
        /**
         * 当前响应数量
         */
        private Integer num;
        /**
         * sort值数组,⽤于下次查询使⽤
         */
        private String sortArr;
        /**
         * 分⻚列表
         */
        private List<DeviceImageGroupDataDtoBody> list;
    }

    /**
     * 设备图
     */
    @Data
    public static class DeviceImageGroupDataDtoBody implements Serializable {
        /**
         * 背景图下载地址
         */
        private String capturePictureUrl;
        /**
         * 抓拍时间，格式: yyyy-MM-dd HH:mm:ss
         */
        private String captureTime;
        /**
         * 组id
         */
        private String imageGroupId;
        /**
         * 消息集合，数量不可⼤于10
         */
        private List<DeviceImageGroupDataDtoBodySubItem> subItemList;
    }

    /**
     * 图片
     */
    @Data
    public static class DeviceImageGroupDataDtoBodySubItem implements Serializable {
        /**
         * 消息类型：1、⼈脸抓拍；2、⻋牌抓拍
         */
        private Integer shotType;
        /**
         * 图⽚id
         */
        private String objectId;
        /**
         * 对象值，如⻋牌字符串
         */
        private String objectValue;
        /**
         * ⼩图下载地址（有效期⼗分钟）
         */
        private String objectUrl;
    }
}
