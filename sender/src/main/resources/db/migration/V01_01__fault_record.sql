#
DROP TABLE IF EXISTS `tbl_sender_fault`;
CREATE TABLE `tbl_sender_fault`
(
    `id`            bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `fault_id`      varchar(32)                                     DEFAULT '' COMMENT '告警ID',
    `elevator_code` varchar(20)                                     DEFAULT '' COMMENT '电梯编号',
    `push_govern`   varchar(30)                                     DEFAULT '' COMMENT '需要推送的平台',
    `entrap`        int(4) DEFAULT 0 COMMENT '是否困人， 0：不是，1：是困人',
    `fault_message` text COMMENT '原告警消息 FaultMessage的信息',
    `need_photo`    int(4) DEFAULT '1' COMMENT '是否需要取证照片，1：需要，0：不需要',
    `need_video`    int(4) DEFAULT '1' COMMENT '是否需要取证视频，1：需要，0：不需要',
    `url_photo`     varchar(1024)                                   DEFAULT '' COMMENT '取证照片URL',
    `url_video`     varchar(1024)                                   DEFAULT '' COMMENT '取证视频URL',
    `finished`      int(4) DEFAULT '0' COMMENT '是否已经完成推送，0：未完成，1：已完成',
    `created_time`  datetime                                        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`  datetime                                        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `created_user`  varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'System' COMMENT '创建人',
    `updated_user`  varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'System' COMMENT '修改人',
    PRIMARY KEY (`id`),
    KEY             `ix_fault_id` (`fault_id`),
    KEY             `ix_elevator_code` (`elevator_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='需要补充推送告警数据的记录';