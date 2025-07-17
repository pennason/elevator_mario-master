use
`elevator_master`;

CREATE TABLE `tbl_camera_download_task`
(
    `id`                   bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `elevator_code`        varchar(50)  DEFAULT NULL COMMENT '电梯码',
    `task_type`            tinyint(2) DEFAULT NULL COMMENT '任务类型 1:告警，2：夜间守护，3：群租，0：其他：参考com.shmashine.common.enums.CameraTaskTypeEnum',
    `task_custom_id`       varchar(50)  DEFAULT NULL COMMENT '自定义唯一ID，如故障ID',
    `media_type`           varchar(10)  DEFAULT NULL COMMENT '文件类型 JPG, MP4, JPEG, M3U8 参考：CameraMediaTypeEnum',
    `camera_type`          tinyint(2) DEFAULT NULL COMMENT '摄像头类型 1：海康，2：雄迈 com.shmashine.common.enums.CameraTypeEnum',
    `cloud_number`         varchar(50)  DEFAULT NULL COMMENT '云平台序列号，通过该序号获取摄像头视频',
    `task_custom_type`     tinyint(5) DEFAULT '0' COMMENT '自定义类型，如故障类型',
    `collect_time`         varchar(20)  DEFAULT NULL COMMENT '采集时间 yyyy-MM-dd HH:mm:ss',
    `start_time`           varchar(20)  DEFAULT NULL COMMENT '历史视频下载开始时间 yyyyMMddHHmmss',
    `end_time`             varchar(20)  DEFAULT NULL COMMENT '历史视频下载结束时间 yyyyMMddHHmmss',
    `floor`                varchar(10)  DEFAULT NULL COMMENT '所在楼层',
    `file_status`          tinyint(2) DEFAULT 0 COMMENT '下载执行状态 0：待下载 1：下载成功 2:开始处理，3：下载中（请求成功）, 4:待上传OSS  5：请求失败（等待重试） 6：文件上传阿里解析失败',
    `cloud_task_id`        varchar(50)  DEFAULT NULL COMMENT '远程执行任务ID，如萤石的下载任务ID',
    `request_failed_count` int(2) DEFAULT '0' COMMENT '请求失败次数标记',
    `upload_failed_count`  int(2) DEFAULT '0' COMMENT '上传失败次数标记',
    `return_code`          int(5) DEFAULT NULL COMMENT '返回码',
    `err_message`          varchar(255) DEFAULT NULL COMMENT '失败原因',
    `source_url`           text         DEFAULT NULL COMMENT '文件地址, 远程地址，如萤石云返回的地址',
    `oss_url`              text         DEFAULT NULL COMMENT '文件地址, 上传至自己阿里云平台的地址',
    `extend_info`          text         DEFAULT NULL COMMENT '其他扩展信息，可以使用JSON字符串记录一些扩展信息',
    `create_time`          datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_time`          datetime     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_elevator_task_custom` (`elevator_code`, `task_type`, `task_custom_id`),
    KEY                    `ix_task_custom_id` (`task_custom_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT '普通下载摄像头视频图片任务， tbl_hkcamera_download_report是下载告警视频和图片';