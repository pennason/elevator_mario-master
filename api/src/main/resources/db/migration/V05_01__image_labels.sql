use
`elevator_master`;

CREATE TABLE `image_labels`
(
    `id`            bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `fault_id`      varchar(50) NOT NULL COMMENT '自定义唯一ID，如故障ID',
    `elevator_id`   varchar(20)   DEFAULT '' COMMENT '电梯唯一ID',
    `elevator_code` varchar(20)   DEFAULT '' COMMENT '电梯编号',
    `collect_time`  datetime      DEFAULT NULL COMMENT '记录时间',
    `file_id`       varchar(20)   DEFAULT '' COMMENT '文件记录ID',
    `file_oss_url`  varchar(1000) DEFAULT '' COMMENT '文件地址, 上传至自己阿里云平台的地址',
    `mark_type`     int(2) unsigned DEFAULT '37' COMMENT '标注类型， 37：助动车',
    `mark_need`     int(2) unsigned DEFAULT '0' COMMENT '记录执行状态 0:无需标注， 1：已标注',
    `mark_content`  text COMMENT '标注内容',
    `create_time`   datetime      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_time`   datetime      DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_fault_id` (`fault_id`),
    KEY             `idx_elevator_code` (`elevator_code`),
    KEY             `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='图像标注记录';


ALTER TABLE tbl_fault_uncivilized_behavior37
    ADD has_mark_label int(1) DEFAULT 0 NULL COMMENT '是否已图像标注0：未标注，1：已标注';
