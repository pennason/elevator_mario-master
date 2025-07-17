use
`elevator_master`;

CREATE TABLE `tbl_camera_extend_info`
(
    `id`            bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `cloud_number`  varchar(64)  DEFAULT NULL COMMENT '云平台序列号',
    `platform_id`   varchar(50)  DEFAULT NULL COMMENT '上级或下级平台ID',
    `platform_name` varchar(255) DEFAULT NULL COMMENT '上级或下级平台名称',
    `sup_or_sub`    int(10) DEFAULT 1 COMMENT '1：对接上级平台，2：对接下级平台',
    `elevator_code` varchar(20)  DEFAULT NULL COMMENT '电梯编号',
    `guid`          varchar(25)  DEFAULT NULL COMMENT '本系统设备通道唯⼀标识',
    `gbid`          varchar(50)  DEFAULT NULL COMMENT '国标id',
    `ctei`          varchar(50)  DEFAULT NULL COMMENT '设备CTEI码',
    `camera_type`   int(2) DEFAULT 0 COMMENT '摄像头类型 1：海康萤石平台，2：雄迈平台，3：海尔平台，4：海康云眸, 5天翼云眼，6中兴 com.shmashine.common.enums.CameraTypeEnum',
    `channel_seq`   int(10) DEFAULT 0 COMMENT '通道序号',
    `device_code`   varchar(50)  DEFAULT NULL COMMENT '设备编号（国标级联编号）',
    `vendor_code`   varchar(20)  DEFAULT NULL COMMENT '设备所属平台编号。⼤华:QQY、中兴:TYBD、天翼云眼TYYY',
    `online_state`  int(2) DEFAULT 1 COMMENT ' 0:离线 1:在线',
    `status`        int(2) DEFAULT 1 COMMENT ' -1:删除 0:离线 1:在线 2:故障 3:新增',
    `change_time`   varchar(20) COMMENT '事件发生时间',
    `create_time`   datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_time`   datetime     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_cloud_number_platform` (`cloud_number`,`platform_id`),
    KEY             `uk_elevator_code` (`elevator_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT '摄像头扩展信息，比如与天翼视联的对接信息';