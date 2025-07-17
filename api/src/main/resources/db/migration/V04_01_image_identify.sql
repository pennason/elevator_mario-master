use
`elevator_master`;

CREATE TABLE `tbl_camera_image_identify`
(
    `id`              bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `task_custom_id`  varchar(50) NOT NULL COMMENT '自定义唯一ID，如故障ID',
    `elevator_id`     varchar(20)   DEFAULT '' COMMENT '电梯唯一ID',
    `elevator_code`   varchar(20)   DEFAULT '' COMMENT '电梯编号',
    `collect_time`    varchar(20)   DEFAULT '' COMMENT '采集时间',
    `floor`           varchar(10)   DEFAULT '' COMMENT '所在楼层',
    `oss_url`         varchar(1000) DEFAULT '' COMMENT '文件地址, 上传至自己阿里云平台的地址',
    `status`          int(2) unsigned DEFAULT '0' COMMENT '记录执行状态 0:初始状态，1：待识别，2：识别中， 3：已识别',
    `identify_type`   int(10) unsigned DEFAULT '1' COMMENT '业务类型 1：自研电动车识别， 2：自研乘梯人员（数量，年龄等），3：自研姿态识别',
    `identify_status` int(2) unsigned DEFAULT '0' COMMENT '识别状态 0:无结果，1：成功，2：失败',
    `identify_result` varchar(255)  DEFAULT '' COMMENT '识别结果',
    `create_time`     datetime      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_time`     datetime      DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_task_custom_id` (`task_custom_id`),
    KEY               `idx_elevator_code` (`elevator_code`),
    KEY               `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='自研图像识别记录与相关结果';