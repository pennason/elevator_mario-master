use
`elevator_master`;

CREATE TABLE `tbl_sys_user_wecom`
(
    `id`              bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`         varchar(55) DEFAULT NULL COMMENT '麦信IOT平台用户ID',
    `wecom_corp_id`   varchar(55) DEFAULT 'ww55dbfe2f0317f687' COMMENT '企业微信的CORP_ID',
    `wecom_corp_name` varchar(55) DEFAULT '上海麦信数据科技有限公司' COMMENT '企业名称',
    `wecom_agent_id`  varchar(55) DEFAULT '1000002' COMMENT '企业微信应用ID',
    `wecom_user_id`   varchar(55) DEFAULT NULL COMMENT '企业微信用户ID',
    `wecom_user_name` varchar(55) DEFAULT NULL COMMENT '企业微信用户名称',
    `create_time`     datetime    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`       varchar(55) DEFAULT NULL COMMENT '创建人',
    `modify_time`     datetime    DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `modify_by`       varchar(55) DEFAULT NULL COMMENT '修改人',
    `deleted`         int(10) DEFAULT 0 COMMENT '是否被删除， 1：已删除，0：正常',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_project_day` (`user_id`,`wecom_corp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='平台用户与企业微信用户之间关系';