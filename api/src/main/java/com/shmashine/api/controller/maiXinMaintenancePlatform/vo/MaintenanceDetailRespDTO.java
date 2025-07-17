package com.shmashine.api.controller.maiXinMaintenancePlatform.vo;

import java.util.List;

import lombok.Data;

/**
 * @author  jiangheng
 * @version 2024/1/17 16:31
 * @description: com.shmashine.api.controller.maiXinMaintenancePlatform.vo
 */
@Data
public class MaintenanceDetailRespDTO {

    /**
     * 维保详情
     */
    private MaintenanceResultDTO maintenanceDetail;

    /**
     * 保养项
     */
    private List<MaintenanceItemDTO> maintenanceItems;

    /**
     * 保养图片信息
     */
    private MaintenancePicUrls maintenancePicUrls;

    @Data
    public static class MaintenancePicUrls {

        /**
         * 机房 维保前后
         */
        private List<String> preMotorRoom;
        private List<String> afterMotorRoom;

        /**
         * 轿顶
         */
        private List<String> preCarRoof;
        private List<String> afterCarRoof;

        /**
         * 轿厢
         */
        private List<String> preCarRoom;
        private List<String> afterCarRoom;

        /**
         * -层门
         */
        private List<String> preFloorDoor;
        private List<String> afterFloorDoor;

        /**
         * 层站
         */
        private List<String> preFloorStation;
        private List<String> afterFloorStation;

        /**
         * 底坑
         */
        private List<String> preBottomHole;
        private List<String> afterBottomHole;

    }

}
