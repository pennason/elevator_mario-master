// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 14:41
 * @since v1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TyslDeviceInfoResultResponseDTO
        extends PageResponseDTO<TyslDeviceInfoResultResponseDTO.DeviceInfoResultDTO> {

    /**
     * 详情
     */
    @Data
    public static class DeviceInfoResultDTO implements Serializable {
        /**
         * 本系统设备通道唯⼀标识
         */
        private String guid;
        /**
         * 通道名称
         */
        private String name;
        /**
         * 地址
         */
        private String address;
        /**
         * 省份
         */
        private String province;
        /**
         * 市
         */
        private String city;
        /**
         * 区
         */
        private String district;
        /**
         * 街镇
         */
        private String town;
        /**
         * ⼩区
         */
        private String community;
        /**
         * 区局
         */
        private String station;
        /**
         * 分局
         */
        private String subStation;
        /**
         * 设备状态： -1:删除 0:离线 1:在线 2:故障 3:新增
         */
        private Integer status;
        /**
         * 国标编号
         */
        private String gbCode;
        /**
         * 对应各平台的通道标识，映射关系如下： ⼤华：通道号 channelId 中兴平台：通道编号 guid 天翼云眼：⽆通道标识，暂⽤设备编号取代
         */
        private String channelNo;
        /**
         * 对应各平台的设备标识，映射关系如下： ⼤华：设备编号 deviceCode 中兴平台：设备编号 puid 天翼云眼：设备编号 deviceCode
         */
        private String deviceCode;
        /**
         * 通道序号，统⼀从0开始，中兴平台后续定制开发
         */
        private Integer channelSeq;
        /**
         * ⽤户标识，对应中兴平台⽤户标识
         */
        private String identity;
        /**
         * ⽤户群组，对应中兴平台⽤户群组
         */
        private String identityGroup;
        /**
         * 设备所属平台编号。⼤华:QQY、中兴:TYBD、天翼云眼TYYY
         */
        private String vendorCode;
        /**
         * 设备型号
         */
        private String deviceModel;
        /**
         * 设备sn号
         */
        private String deviceSn;
        /**
         * 设备⼚商
         */
        private String deviceManufacturer;
        /**
         * 摄像头类型 （其他:0、枪机:1、球机:2、半球:3）
         */
        private Integer cameraType;
        /**
         * 设备接⼊类型（其他:0、DVR:1、IPC:2、NVR:3) 天翼云眼：参考 com.shmashine.cameratysl.enums.TyslDeviceTypeTyyyEnum
         */
        private Integer deviceType;
        /**
         * 设备协议类型，GB28181:1、ONVIF:2
         */
        private Integer protocol;
        /**
         * 经度
         */
        private String lng;
        /**
         * 纬度
         */
        private String lat;
        /**
         * IP地址
         */
        private String ip;
        /**
         * 云存状态（1:有效、0:⽆效）
         */
        private Integer cloudStatus;
        /**
         * 绑定状态（1:已绑定、0:待绑定）
         */
        private Integer bandStatus;
        /**
         * 最近的绑定时间，格式：yyyy-MM-dd HH:mm:ss
         */
        private String importTime;
        /**
         * 云存套餐信息
         */
        private String packageInfo;
        /**
         * 平台Id
         */
        private Integer platformid;
        /**
         * 平台名称
         */
        private String platformName;
        /**
         * ⼚商id
         */
        private Integer factoryId;
        /**
         * 设备类型
         */
        private String deviceFactory;
        /**
         * 设备固件版本
         */
        private String firmwareVersion;
        /**
         * 国标id
         */
        private String gbId;
        /**
         * 全链接区域名称
         */
        private String fullRegionName;
        /**
         * 设备CTEI码
         */
        private String ctei;
        /**
         * 是否⾃⼰绑定的设备
         */
        private Integer isSelfBind;
    }
}
