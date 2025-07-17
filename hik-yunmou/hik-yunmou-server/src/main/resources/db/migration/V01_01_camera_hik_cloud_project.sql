use
`elevator_master`;

CREATE TABLE `tbl_camera_hik_cloud_project`
(
    `id`                    bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `project_code`          varchar(50) DEFAULT NULL COMMENT '项目编码',
    `project_name`          varchar(50) DEFAULT NULL COMMENT '项目名称',
    `hik_cloud_project_id`  varchar(50) DEFAULT NULL COMMENT '云眸上的项目ID',
    `hik_cloud_expire_days` int(10) DEFAULT NULL COMMENT '项目中文件过期时间，单位：天， 不可自己修改，需要更新云眸的项目管理',
    `hik_cloud_flow_limit`  bigint(20) DEFAULT NULL COMMENT '下载流量限制，单位字节， 不可自己修改，需要更新云眸的项目管理',
    `create_time`           datetime    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_time`           datetime    DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_project_code` (`project_code`),
    KEY                     `ix_hik_cloud_project_id` (`hik_cloud_project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT '海康云眸项目列表，因为海康的任务都需要指定项目ID';


CREATE TABLE `tbl_camera_cascade_platform`
(
    `id`            bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `cloud_number`  varchar(64)  DEFAULT NULL COMMENT '云平台序列号',
    `platform_id`   varchar(50)  DEFAULT NULL COMMENT '上级或下级平台ID',
    `platform_name` varchar(255) DEFAULT NULL COMMENT '上级或下级平台名称',
    `sup_or_sub`    int(10) DEFAULT 1 COMMENT '1：对接上级平台，2：对接下级平台',
    `elevator_code` varchar(20)  DEFAULT NULL COMMENT '电梯编号',
    `camera_type`   int(2) DEFAULT NULL COMMENT '摄像头类型 1：海康萤石平台，2：雄迈平台，3：海尔平台，4：海康云眸 com.shmashine.common.enums.CameraTypeEnum',
    `channel_id`    varchar(50)  DEFAULT NULL COMMENT '通道ID',
    `channel_no`    int(10) DEFAULT 1 COMMENT '通道号',
    `channel_code`  varchar(50)  DEFAULT NULL COMMENT '通道级联编码',
    `create_time`   datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_time`   datetime     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_cloud_number_platform` (`cloud_number`,`platform_id`),
    KEY             `uk_elevator_code` (`elevator_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT '海康摄像头国标级联';