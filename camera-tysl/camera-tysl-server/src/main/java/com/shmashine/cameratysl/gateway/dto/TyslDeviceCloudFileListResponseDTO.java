// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 15:26
 * @since v1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TyslDeviceCloudFileListResponseDTO extends PageResponseDTO<TyslDeviceCloudFileListResponseDTO.RecordsDTO> {

    /**
     * 详情
     */
    @Data
    public static class RecordsDTO implements Serializable {
        /**
         * 录像⽂件id（天翼云眼、中兴）
         */
        private String fileId;
        /**
         * 录像开始时间,yyyy-MM-dd HH:mm:ss
         */
        private String beginTime;
        /**
         * 录像结束时间,yyyy-MM-dd HH:mm:ss
         */
        private String endTime;
        /**
         * 码流类型 ⼤华：main--主码流，extral--辅码流1
         */
        private String videoStream;
        /**
         * ⽂件⼤⼩
         */
        private String fileSize;
        /**
         * ⼤华：是否被锁定,设备录像不会被锁定。0--不锁定，1--锁定
         */
        private Integer locked;
        /**
         * ⼤华：存放位置
         */
        private String location;
        /**
         * ⽂件名称（⼤华、天翼云眼）
         */
        private String name;
        /**
         * 天翼云眼：⾸帧截图下载地址，以云存开始时间-云存结束时间命名，如 yyyyMMddHHmmss-yyyyMMddHHmmss_ALARM.jpg
         */
        private String iconUrl;
        /**
         * 录像状态： 中兴：正在录像：150，录像完成：200，待删除：900，待时间段删除：950 其他：录像正常--201
         */
        private String status;
        /**
         * 中兴：码流编号，1:主码流1 （主码流[1]/⼦码流1[2]/⼦码流2[3]）
         */
        private String streamId;
    }
}
