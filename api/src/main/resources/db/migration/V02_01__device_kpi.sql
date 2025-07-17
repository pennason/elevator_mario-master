use
`elevator_master`;

CREATE TABLE `kpi_project_iot`
(
    `id`                        bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `project_id`                varchar(20)  DEFAULT NULL COMMENT '项目ID',
    `day`                       varchar(10)  DEFAULT NULL COMMENT '年月日yyyy-mm-dd',
    `project_name`              varchar(100) DEFAULT NULL COMMENT '项目名称',
    `elevator_total`            int(10) DEFAULT NULL COMMENT '电梯总数',
    `elevator_offline_max`      int(10) DEFAULT NULL COMMENT '电梯当日最大离线数',
    `elevator_offline_realtime` int(10) DEFAULT NULL COMMENT '电梯当日实时离线数',
    `elevator_fault_max`        int(10) DEFAULT NULL COMMENT '电梯当日最大故障数',
    `elevator_fault_realtime`   int(10) DEFAULT NULL COMMENT '电梯当日实时故障数',
    `camera_total`              int(10) DEFAULT NULL COMMENT '摄像头总数',
    `camera_offline_max`        int(10) DEFAULT NULL COMMENT '摄像头当日最大离线数',
    `camera_offline_realtime`   int(10) DEFAULT NULL COMMENT '摄像头当日实时离线数',
    `create_time`               datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_time`               datetime     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_project_day` (`project_id`,`day`),
    KEY                         `idx_day` (`day`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='按项目统计电梯设备相关KPI数据';

CREATE TABLE `kpi_project_north_push`
(
    `id`                        bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `project_id`                varchar(20)  DEFAULT NULL COMMENT '项目ID',
    `day`                       varchar(10)  DEFAULT NULL COMMENT '年月日yyyy-mm-dd',
    `project_name`              varchar(100) DEFAULT NULL COMMENT '项目名称',
    `elevator_total`            int(10) DEFAULT NULL COMMENT '电梯总数',
    `elevator_offline_max`      int(10) DEFAULT NULL COMMENT '电梯当日最大离线数',
    `elevator_offline_realtime` int(10) DEFAULT NULL COMMENT '电梯当日实时离线数',
    `create_time`               datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_time`               datetime     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_project_day` (`project_id`,`day`),
    KEY                         `idx_day` (`day`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='按项目北向推送相关KPI数据';

