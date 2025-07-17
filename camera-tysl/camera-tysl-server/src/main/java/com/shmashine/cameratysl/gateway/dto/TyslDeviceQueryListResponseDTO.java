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
 * @version v1.0  -  2023/8/23 15:45
 * @since v1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TyslDeviceQueryListResponseDTO extends TyslResponseDTO<TyslDeviceQueryListResponseDTO.PageInfo> {

    /**
     * 分页
     */
    @Data
    public static class PageInfo implements Serializable {
        /**
         * 当前页
         */
        private Integer page;
        /**
         * 每页条数
         */
        private Integer pageSize;
        /**
         * 总数
         */
        private Integer total;
        /**
         * 设备信息列表
         */
        private List<DeviceInfo> deviceInfoList;
    }

    /**
     * 设备信息
     */
    @Data
    public static class DeviceInfo implements Serializable {
        /**
         * 设备标识
         */
        private String guid;
        /**
         * 设备名称
         */
        private String name;
        /**
         * 通道编号
         */
        private String channelNo;
        /**
         * ⼚商编号
         */
        private String vendorCode;
        /**
         * 设备状态： -1:删除 0:离线 1:在线 2:故障 3:新增
         */
        private Integer status;
    }
}
